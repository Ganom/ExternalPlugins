/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.neverlog;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.EventBus;
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
	private static final int LOGOUT_WARNING_MILLIS = (4 * 60 + 40) * 1000; // 4 minutes and 40 seconds
	private static final int LOGOUT_WARNING_CLIENT_TICKS = LOGOUT_WARNING_MILLIS / Constants.CLIENT_TICK_LENGTH;
	@Inject
	private Client client;
	@Inject
	private EventBus eventBus;
	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private final ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1,
		10, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.DiscardPolicy());
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
		eventBus.unregister(this);
	}

	private void onGameTick(GameTick event)
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
			Math.round(random.nextGaussian() * 8000), 7000, 13000
		);
	}

	private static double clamp(double val, double min, double max)
	{
		return Math.max(min, Math.min(max, val));
	}
}
