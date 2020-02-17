/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.olmswapper;

import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.util.Text;
import net.runelite.api.vars.InterfaceTab;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.utils.ExtUtils;
import net.runelite.client.plugins.externals.utils.Tab;
import org.pf4j.Extension;


@Extension
@PluginDescriptor(
	name = "Olm Pray Swapper",
	description = "Automatically swaps prayers for CoX",
	tags = {"prayer", "olm", "bot", "swap"},
	type = PluginType.UTILITY
)
@Slf4j
@SuppressWarnings("unused")
@PluginDependency(ExtUtils.class)
public class OlmSwapper extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private OlmSwapperConfig config;
	@Inject
	private ConfigManager configManager;
	@Inject
	private EventBus eventBus;
	@Inject
	private ExtUtils utils;

	private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());

	private Robot robot;
	private boolean swapMage;
	private boolean swapRange;


	@Provides
	OlmSwapperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OlmSwapperConfig.class);
	}

	@Override
	protected void startUp() throws AWTException
	{
		robot = new Robot();
	}

	@Override
	protected void shutDown()
	{
		robot = null;
	}

	@Subscribe
	private void onGameTick(GameTick event)
	{
		if (swapMage)
		{
			clickPrayer(Prayer.PROTECT_FROM_MAGIC);
			swapMage = false;
		}
		else if (swapRange)
		{
			clickPrayer(Prayer.PROTECT_FROM_MISSILES);
			swapRange = false;
		}
	}

	@Subscribe
	private void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		switch (Text.standardize(event.getMessageNode().getValue()))
		{
			case "the great olm fires a sphere of aggression your way. your prayers have been sapped.":
			case "the great olm fires a sphere of aggression your way.":
				clickPrayer(Prayer.PROTECT_FROM_MELEE);
				break;
			case "the great olm fires a sphere of magical power your way. your prayers have been sapped.":
			case "the great olm fires a sphere of magical power your way.":
				clickPrayer(Prayer.PROTECT_FROM_MAGIC);
				break;
			case "the great olm fires a sphere of accuracy and dexterity your way. your prayers have been sapped.":
			case "the great olm fires a sphere of accuracy and dexterity your way.":
				clickPrayer(Prayer.PROTECT_FROM_MISSILES);
				break;
		}
	}

	@Subscribe
	private void onProjectileMoved(ProjectileMoved event)
	{
		if (!config.swapAutos())
		{
			return;
		}

		int id = event.getProjectile().getId();

		switch (id)
		{
			case 1339:
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
				{
					swapMage = true;
				}
				break;
			case 1340:
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES))
				{
					swapRange = true;
				}
				break;
		}
	}

	private void clickPrayer(Prayer prayer)
	{
		if (client.isPrayerActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) < 1)
		{
			return;
		}

		final Widget widget = client.getWidget(prayer.getWidgetInfo());

		if (widget == null)
		{
			log.error("Olm: Unable to find prayer widget.");
			return;
		}

		final Rectangle bounds = widget.getBounds();

		executorService.submit(() ->
		{
			if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.PRAYER.getId())
			{
				robot.keyPress(utils.getTabHotkey(Tab.PRAYER));
			}

			utils.click(bounds);
			log.debug("Olm: clicking bounds {}", bounds);

			if (client.isPrayerActive(prayer))
			{
				robot.keyPress(utils.getTabHotkey(Tab.INVENTORY));
			}

			try
			{
				Thread.sleep(getMillis());
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		});
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}
}
