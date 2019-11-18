/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.olmswapper;

import com.google.inject.Provides;
import java.awt.Rectangle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.util.Text;
import net.runelite.api.vars.InterfaceTab;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.olmswapper.utils.ExtUtils;
import net.runelite.client.plugins.externals.olmswapper.utils.Tab;
import net.runelite.client.plugins.externals.olmswapper.utils.TabUtils;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;


@PluginDescriptor(
	name = "Olm Pray Swapper",
	description = "Automatically swaps prayers for CoX",
	tags = {"prayer", "olm", "bot", "swap"},
	type = PluginType.EXTERNAL
)

@Slf4j
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
	private TabUtils tabUtils;
	private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());
	private Flexo flexo;
	private boolean swapMage;
	private boolean swapRange;


	@Provides
	OlmSwapperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OlmSwapperConfig.class);
	}

	@Override
	protected void startUp()
	{
		addSubscriptions();
		Flexo.client = client;
		executorService.submit(() ->
		{
			flexo = null;
			try
			{
				flexo = new Flexo();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	@Override
	protected void shutDown()
	{
		flexo = null;
		eventBus.unregister(this);
	}

	private void addSubscriptions()
	{
		eventBus.subscribe(ChatMessage.class, this, this::onChatMessage);
		eventBus.subscribe(ProjectileMoved.class, this, this::onProjectileMoved);
		eventBus.subscribe(GameTick.class, this, this::onGameTick);
	}

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
		if (client.isPrayerActive(prayer))
		{
			return;
		}

		final Widget widget = client.getWidget(prayer.getWidgetInfo());

		if (widget == null)
		{
			return;
		}

		final Rectangle bounds = widget.getBounds();

		executorService.submit(() ->
		{
			if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.PRAYER.getId())
			{
				flexo.keyPress(tabUtils.getTabHotkey(Tab.PRAYER));
			}

			ExtUtils.handleSwitch(bounds, config.actionType(), flexo, client, configManager.getConfig(StretchedModeConfig.class).scalingFactor(), (int) getMillis());

			if (client.isPrayerActive(prayer))
			{
				flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
			}
		});
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}
}