/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.neverlog;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "Never Log",
	description = "Enable this and you will never log out",
	tags = "ganom"
)
@SuppressWarnings("unused")
public class NeverLog extends Plugin
{
	@Inject
	private Client client;

	private final Random random = new Random();
	private long randomDelay;

	@Override
	protected void startUp()
	{
		randomDelay = randomDelay();
	}

	@Override
	protected void shutDown()
	{

	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (checkIdleLogout())
		{
			randomDelay = randomDelay();
			Executors.newSingleThreadExecutor()
				.submit(this::pressKey);
		}
	}

	private boolean checkIdleLogout()
	{
		int idleClientTicks = client.getKeyboardIdleTicks();

		if (client.getMouseIdleTicks() < idleClientTicks)
		{
			idleClientTicks = client.getMouseIdleTicks();
		}

		return idleClientTicks >= randomDelay;
	}

	private long randomDelay()
	{
		return (long) clamp(
			Math.round(random.nextGaussian() * 8000)
		);
	}

	private static double clamp(double val)
	{
		return Math.max(1, Math.min(13000, val));
	}

	private void pressKey()
	{
		KeyEvent keyPress = new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), BUTTON1_DOWN_MASK, KeyEvent.VK_BACK_SPACE);
		this.client.getCanvas().dispatchEvent(keyPress);
		KeyEvent keyRelease = new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE);
		this.client.getCanvas().dispatchEvent(keyRelease);
		KeyEvent keyTyped = new KeyEvent(this.client.getCanvas(), KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE);
		this.client.getCanvas().dispatchEvent(keyTyped);
	}
}
