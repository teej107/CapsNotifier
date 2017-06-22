package com.teej107.caps;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by teej107 on 12/17/2016.
 */
public class OpacityPanel extends JPanel implements ChangeListener
{
	private Options options;
	private JLabel label;
	private JSlider slider;

	public OpacityPanel(Options options)
	{
		this.options = options;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		label = new JLabel("Current Opacity: " + options.getWindowOpacity());
		slider = new JSlider(0, 100, options.getWindowOpacity());
		slider.setFocusable(false);
		slider.addChangeListener(this);
		add(label);
		add(slider);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		options.setWindowOpacity(slider.getValue());
		label.setText("Current Opacity: " + options.getWindowOpacity());
	}
}
