/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.olmswapper;

import com.google.inject.Provides;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.olmswapper.utils.ExtUtils;
import net.runelite.client.plugins.olmswapper.utils.Tab;
import net.runelite.client.plugins.olmswapper.utils.TabUtils;
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
	private BlockingQueue queue = new ArrayBlockingQueue(1);
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
		executorService.submit(() -> {
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
			log.info("Protect Magic Being Activated -- Auto Attack");
			executorService.submit(() -> clickPrayer(Prayer.PROTECT_FROM_MAGIC));
			swapMage = false;
		}
		else if (swapRange)
		{
			if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES))
			{
				log.info("Protect Missiles Being Activated -- Auto Attack");
				executorService.submit(() -> clickPrayer(Prayer.PROTECT_FROM_MISSILES));
			}
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
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MELEE))
				{
					log.info("Protect Melee Being Activated");
					executorService.submit(() -> clickPrayer(Prayer.PROTECT_FROM_MELEE));
				}
				break;
			case "the great olm fires a sphere of magical power your way. your prayers have been sapped.":
			case "the great olm fires a sphere of magical power your way.":
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
				{
					log.info("Protect Magic Being Activated");
					executorService.submit(() -> clickPrayer(Prayer.PROTECT_FROM_MAGIC));
				}
				break;
			case "the great olm fires a sphere of accuracy and dexterity your way. your prayers have been sapped.":
			case "the great olm fires a sphere of accuracy and dexterity your way.":
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES))
				{
					log.info("Protect Missiles Being Activated");
					executorService.submit(() -> clickPrayer(Prayer.PROTECT_FROM_MISSILES));
				}
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
		if (prayer == null)
		{
			return;
		}

		if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE).isHidden())
		{
			flexo.keyPress(TabUtils.getTabHotkey(Tab.PRAYER, client));
		}

		Widget widget = client.getWidget(prayer.getWidgetInfo());

		if (widget == null)
		{
			return;
		}

		ExtUtils.handleSwitch(widget.getBounds(), config.actionType(), flexo, client, configManager.getConfig(StretchedModeConfig.class).scalingFactor(), (int) getMillis());

		if (client.isPrayerActive(prayer))
		{
			flexo.keyPress(TabUtils.getTabHotkey(Tab.INVENTORY, client));
		}
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}
}