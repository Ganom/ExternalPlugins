/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.neverlog;

import java.util.Random;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;

@PluginDescriptor(
	name = "Never Log",
	description = "Enable this and you will never log out",
	type = PluginType.EXTERNAL
)
@SuppressWarnings("unused")
public class NeverLog extends Plugin
{
	@Inject
	private Client client;

	private Random random = new Random();
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
			client.setMouseIdleTicks(0);
			client.setKeyboardIdleTicks(0);
			randomDelay = randomDelay();
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
}