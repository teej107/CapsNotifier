package com.teej107.caps;

import javax.swing.*;
import java.awt.*;

/**
 * Created by teej107 on 12/17/2016.
 */
public class CapsNotification extends JWindow
{
	private static final CapsNotification CAPS_N = new CapsNotification();

	private JPanel panel;

	private CapsNotification()
	{
		setSize(Options.getInstance().getWindowSize());
		setLocation(Options.getInstance().getWindowLocation().getLocation(getSize()));
		setOpacity((float)Options.getInstance().getWindowOpacity()/100f);
		setAlwaysOnTop(true);
		setFocusable(false);

		panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g)
			{
				g.setColor(Options.getInstance().getBackgroundColor());
				g.fillRect(0, 0, CapsNotification.this.getWidth(), CapsNotification.this.getHeight());
				g.drawImage(Options.getInstance().getIconImage(), 0, 0, null);
			}
		};

		setContentPane(panel);
	}

	public static void updateImage()
	{
		SwingUtilities.invokeLater(() -> {
			CAPS_N.setSize(Options.getInstance().getWindowSize());
			CAPS_N.setLocation(Options.getInstance().getWindowLocation().getLocation(CAPS_N.getSize()));
			CAPS_N.setOpacity((float)Options.getInstance().getWindowOpacity()/100);
			CAPS_N.validate();
			CAPS_N.repaint();
		});
	}

	public static void setWindowLocation(Options.WindowLocation location)
	{
		SwingUtilities.invokeLater(() -> CAPS_N.setLocation(location.getLocation(CAPS_N.getSize())));
	}

	public static void setState(boolean b)
	{
		SwingUtilities.invokeLater(() -> {CAPS_N.setVisible(b);});
	}
}
