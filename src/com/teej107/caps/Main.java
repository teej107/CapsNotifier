package com.teej107.caps;

import org.jnativehook.GlobalScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by teej107 on 12/17/2016.
 */
public class Main
{
	public static final String APP_NAME = "Caps Lock Notifier";

	private static TrayIcon tray;

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			Logger.getLogger(GlobalScreen.class.getPackage().getName()).setFilter(record -> false);
			GlobalScreen.registerNativeHook();
			LogManager.getLogManager().reset();
		}
		catch (Exception | Error e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage().replace("(", "\n("), "Unable to start service", JOptionPane.OK_OPTION);
			System.exit(1);
		}
		GlobalScreen.addNativeKeyListener(new CapsLockListener());

		if (SystemTray.isSupported())
		{
			setupTray();
		}
		else
		{
			JOptionPane.showMessageDialog(null,
					"System tray is not supported on this system.\nTo stop this service, go to your task manager and quit it from there.");
		}
		System.out.println(APP_NAME + " is initialized");
	}

	private static void setupTray()
	{
		tray = new TrayIcon(Options.getInstance().getIconImage());
		tray.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getButton() == MouseEvent.BUTTON1)
				{
					Options.getInstance().setVisible(true);
				}
			}
		});
		tray.setToolTip(APP_NAME);
		tray.setImageAutoSize(true);
		PopupMenu popup = new PopupMenu();
		popup.add(APP_NAME);
		popup.addSeparator();
		popup.add("Options");
		popup.add("Exit");
		popup.addActionListener(e -> {
			switch (e.getActionCommand())
			{
				case "Options":
					Options.getInstance().setVisible(true);
					break;
				case "Exit":
					exit();
			}
		});
		tray.setPopupMenu(popup);
		try
		{
			SystemTray.getSystemTray().add(tray);
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	public static void exit()
	{
		SystemTray.getSystemTray().remove(tray);
		System.exit(0);
	}
}
