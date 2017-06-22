package com.teej107.caps;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by teej107 on 12/17/2016.
 */
public class CapsLockListener implements NativeKeyListener
{
	private boolean state;

	public CapsLockListener()
	{
		state = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
		CapsNotification.setState(state);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent)
	{

	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent)
	{
		if(nativeKeyEvent.getKeyCode() == 58)
		{
			state = !state;
			CapsNotification.setState(state);
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent)
	{

	}
}
