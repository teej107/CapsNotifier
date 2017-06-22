package com.teej107.caps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by teej107 on 12/17/2016.
 */
public class ColorChooser extends JDialog
{
	private JColorChooser jColorChooser;

	public ColorChooser()
	{
		JPanel panel = new JPanel();
		setContentPane(panel);
		jColorChooser = new JColorChooser();
		panel.setLayout(new BorderLayout());
		panel.add(jColorChooser, BorderLayout.CENTER);
		JButton btn = new JButton("Select Color");
		btn.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				ColorChooser.this.setVisible(false);
			}
		});
		panel.add(btn, BorderLayout.PAGE_END);
		setMinimumSize(new Dimension(450, 350));
		setTitle("Color Chooser");
		setModalityType(ModalityType.APPLICATION_MODAL);
	}

	public Color showNow(Color color)
	{
		jColorChooser.setColor(color);
		setLocationRelativeTo(Options.getInstance());
		setVisible(true);
		return jColorChooser.getColor();
	}
}
