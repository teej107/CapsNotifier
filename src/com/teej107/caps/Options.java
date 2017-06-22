package com.teej107.caps;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.prefs.Preferences;

/**
 * Created by teej107 on 12/17/2016.
 */
public class Options extends JDialog implements ActionListener
{
	private static Rectangle BOUNDS = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

	private static String WINDOW_LOCATION = "window location";
	private static String IMAGE_LOCATION = "image location";
	private static String BACKGROUND_COLOR = "background color";
	private static String OPACITY = "opacity";

	private static Options OPTIONS = new Options();

	private Preferences prefs;
	private Image image;
	private JPanel panel;
	private JButton changeFileButton, changeColorButton, resetImageButton, exitButton;
	private ColorChooser colorChooser;

	private Options()
	{
		prefs = Preferences.userNodeForPackage(getClass());
		setIconImage(new File(prefs.get(IMAGE_LOCATION, "teej")));

		setLocationByPlatform(true);

		panel = new JPanel(){
			@Override
			public Insets getInsets()
			{
				return new Insets(5, 5, 5, 5);
			}

			@Override
			public Component add(Component comp)
			{
				comp.setFocusable(false);
				return super.add(comp);
			}
		};
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		colorChooser = new ColorChooser();

		changeFileButton = new JButton("Change Image");
		changeFileButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.showDialog(Options.this, "Select Image");
				if(fileChooser.getSelectedFile() != null)
				{
					setIconImage(fileChooser.getSelectedFile());
					CapsNotification.updateImage();
				}

			}
		});
		panel.add(changeFileButton);
		panel.add(Box.createVerticalStrut(5));
		resetImageButton = new JButton("Reset Image");
		resetImageButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				setIconImage((File) null);
			}
		});
		panel.add(resetImageButton);
		panel.add(Box.createVerticalStrut(5));
		changeColorButton = new JButton("Change Background Color");
		changeColorButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				setBackgroundColor(colorChooser.showNow(getBackgroundColor()));
				CapsNotification.updateImage();
			}
		});
		panel.add(changeColorButton);
		panel.add(Box.createVerticalStrut(10));
		panel.add(new OpacityPanel(this));
		panel.add(Box.createVerticalStrut(10));
		ButtonGroup group = new ButtonGroup();
		for(WindowLocation wl : WindowLocation.values())
		{
			JRadioButton rb = new JRadioButton(wl.toString());
			rb.setName(wl.name());
			rb.addActionListener(this);
			rb.setSelected(getWindowLocation() == wl);
			group.add(rb);
		}

		Enumeration<AbstractButton> enumeration = group.getElements();
		while(enumeration.hasMoreElements())
		{
			panel.add(enumeration.nextElement());
		}
		panel.add(Box.createVerticalStrut(15));
		exitButton = new JButton("Quit Service");
		exitButton.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				Main.exit();
			}
		});
		panel.add(exitButton);

		setResizable(false);
		setMinimumSize(new Dimension(250, 200));
		setMaximumSize(new Dimension(250, 200));
		setTitle(Main.APP_NAME + " Options");
		setContentPane(panel);
		pack();
	}

	public static Options getInstance()
	{
		return OPTIONS;
	}

	public Color getBackgroundColor()
	{
		return new Color(prefs.getInt(BACKGROUND_COLOR, new Color(244, 176,0).getRGB()));
	}

	public void setBackgroundColor(Color color)
	{
		prefs.putInt(BACKGROUND_COLOR, color.getRGB());
	}

	public WindowLocation getWindowLocation()
	{
		String name = prefs.get(WINDOW_LOCATION, WindowLocation.BOTTOM_RIGHT.name());
		try
		{
			return WindowLocation.valueOf(name);
		}
		catch (IllegalArgumentException e)
		{
			return WindowLocation.BOTTOM_RIGHT;
		}
	}

	public void setWindowLocation(WindowLocation location)
	{
		prefs.put(WINDOW_LOCATION, location.name());
		CapsNotification.setWindowLocation(location);
	}

	public int getWindowOpacity()
	{
		return prefs.getInt(OPACITY, 100);
	}

	public void setWindowOpacity(int n)
	{
		prefs.putInt(OPACITY, Math.min(Math.max(n, 0), 100));
		CapsNotification.updateImage();
	}

	public Dimension getWindowSize()
	{
		return new Dimension(image.getWidth(null), image.getHeight(null));
	}

	public Image getIconImage()
	{
		return image;
	}

	public void setIconImage(File file)
	{
		if(file == null)
		{
			prefs.put(IMAGE_LOCATION, "teej");
			image = getDefaultImage();
		}
		else
		{
			if(file.exists())
			{
				try
				{
					image = ImageIO.read(file);
					prefs.put(IMAGE_LOCATION, file.getAbsolutePath());
				}
				catch (IOException e)
				{
					image = getDefaultImage();
					JOptionPane.showMessageDialog(null, "Unable to set image @ " + file.getAbsolutePath());
				}
			}
			else
			{
				image = getDefaultImage();
			}
		}
		if(panel != null)
		{
			CapsNotification.updateImage();
		}
	}

	private Image getDefaultImage()
	{
		try
		{
			return ImageIO.read(getClass().getResource("/assets/caps lock.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		setWindowLocation(WindowLocation.valueOf(((JComponent) e.getSource()).getName()));
	}

	public enum WindowLocation
	{
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT;

		public Point getLocation(Dimension offset)
		{
			switch (this)
			{
				case TOP_LEFT:
					return new Point(BOUNDS.x, BOUNDS.y);
				case TOP_RIGHT:
					return new Point(BOUNDS.x + BOUNDS.width - offset.width, BOUNDS.y);
				case BOTTOM_LEFT:
					return new Point(BOUNDS.x, BOUNDS.y + BOUNDS.height - offset.height);
				case BOTTOM_RIGHT:
					return new Point(BOUNDS.x + BOUNDS.width - offset.width, BOUNDS.y + BOUNDS.height - offset.height);
			}
			return new Point();
		}

		@Override
		public String toString()
		{
			String[] arr = name().split("_");
			StringBuilder sb = new StringBuilder();
			for(String s : arr)
			{
				sb.append(s.charAt(0)).append(s.substring(1).toLowerCase()).append(' ');
			}
			return sb.toString().trim();
		}
	}
}
