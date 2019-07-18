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
package net.runelite.client.plugins.tobcheats;

import com.google.inject.Provides;
import java.awt.Rectangle;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Prayer;
import net.runelite.api.Projectile;
import net.runelite.api.Skill;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDefinitionChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.plugins.tobcheats.utils.ExtUtils;
import net.runelite.client.plugins.tobcheats.utils.Tab;
import net.runelite.client.plugins.tobcheats.utils.TabUtils;

@PluginDescriptor(
	name = "ToB Cheats",
	description = "ToB Cheats",
	tags = {"tob", "theatre", "blood", "cheats"},
	enabledByDefault = false,
	type = PluginType.EXTERNAL
)

@Slf4j
public class ToBCheats extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private ToBCheatsConfig config;
	@Inject
	private ConfigManager configManager;
	@Inject
	private ItemManager itemManager;
	@Inject
	private EventBus eventBus;
	private Flexo flexo;
	private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 2, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());
	private NPC maiden;
	private NPC nylo;
	private boolean lock1;
	private boolean lock2;
	private boolean lock3;

	@Provides
	ToBCheatsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ToBCheatsConfig.class);
	}

	private List<WidgetItem> getMage()
	{
		return ExtUtils.getItems(ExtUtils.stringToIntArray(config.mage()), client);
	}

	private List<WidgetItem> getRange()
	{
		return ExtUtils.getItems(ExtUtils.stringToIntArray(config.range()), client);
	}

	private List<WidgetItem> getMelee()
	{
		return ExtUtils.getItems(ExtUtils.stringToIntArray(config.melee()), client);
	}

	@Override
	protected void startUp()
	{
		addSubscriptions();
		reset();
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
		reset();
		eventBus.unregister(this);
	}

	private void addSubscriptions()
	{
		eventBus.subscribe(NpcSpawned.class, this, this::onNpcSpawned);
		eventBus.subscribe(GameStateChanged.class, this, this::onGameStateChanged);
		eventBus.subscribe(NpcDespawned.class, this, this::onNpcDespawned);
		eventBus.subscribe(ProjectileMoved.class, this, this::onProjectileMoved);
		eventBus.subscribe(NpcDefinitionChanged.class, this, this::onNpcDefinitionChanged);
		eventBus.subscribe(GameTick.class, this, this::onGameTick);
		eventBus.subscribe(ChatMessage.class, this, this::onChatMessage);
	}

	private void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			reset();
		}
	}

	private void onNpcSpawned(NpcSpawned event)
	{
		final NPC npc = event.getNpc();

		switch (npc.getId())
		{
			case NpcID.NYLOCAS_VASILIAS:
			case NpcID.NYLOCAS_VASILIAS_8355:
			case NpcID.NYLOCAS_VASILIAS_8356:
			case NpcID.NYLOCAS_VASILIAS_8357:
				nylo = npc;
				log.info("Nylo Detected");
				break;
			case NpcID.THE_MAIDEN_OF_SUGADINTI:
				maiden = npc;
				break;
		}
	}

	private void onNpcDespawned(NpcDespawned event)
	{
		final NPC npc = event.getNpc();

		switch (npc.getId())
		{
			case NpcID.NYLOCAS_VASILIAS:
				nylo = null;
				break;
			case NpcID.THE_MAIDEN_OF_SUGADINTI:
				maiden = null;
				break;
		}
	}

	private void onProjectileMoved(ProjectileMoved event)
	{
		Projectile projectile = event.getProjectile();

		if (config.Verzik())
		{
			switch (projectile.getId())
			{
				case 1591:
				case 1594:
					if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
					{
						executorService.submit(() -> clickWidget(Prayer.PROTECT_FROM_MAGIC.getWidgetInfo(), Tab.PRAYER));
					}
					break;
				case 1593:
					if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES))
					{
						executorService.submit(() -> clickWidget(Prayer.PROTECT_FROM_MISSILES.getWidgetInfo(), Tab.PRAYER));
					}
					break;
			}
		}
	}

	private void onGameTick(GameTick event)
	{
		if (config.maidenSwapper())
		{
			if (maiden != null)
			{
				boolean maidenswap70 = false;
				boolean maidenswap50 = false;
				boolean maidenswap30 = false;
				final float hpPercent = 100 * ((float) maiden.getHealthRatio() / (float) maiden.getHealth());
				if (hpPercent > -1)
				{
					return;
				}
				if (hpPercent <= 70.5 && hpPercent >= 69)
				{
					log.info("Maiden Swap 70 reached.");
					maidenswap70 = true;
				}
				if (hpPercent <= 50.5 && hpPercent >= 49)
				{
					log.info("Maiden Swap 50 reached.");
					maidenswap50 = true;
				}
				if (hpPercent <= 30.5 && hpPercent >= 29)
				{
					log.info("Maiden Swap 30 reached.");
					maidenswap30 = true;
				}
				if (maidenswap70 && !lock1)
				{
					handleBarrage("Maiden Swap 70 locked.");
					lock1 = true;
				}
				if (maidenswap50 && !lock2)
				{
					handleBarrage("Maiden Swap 50 locked.");
					lock2 = true;
				}
				if (maidenswap30 && !lock3)
				{
					handleBarrage("Maiden Swap 30 locked.");
					lock3 = true;
				}
			}
		}
	}

	private void onNpcDefinitionChanged(NpcDefinitionChanged event)
	{
		final NPC npc = event.getNpc();

		if (nylo == null)
		{
			log.error("Nylo is null, and is trying to be reached in Definition Change Event");
			return;
		}

		if (npc == nylo)
		{
			Rectangle bounds = nylo.getConvexHull().getBounds();

			switch (npc.getId())
			{
				case NpcID.NYLOCAS_VASILIAS_8355:
					executorService.submit(() -> handleNylo(getMelee(), Prayer.PROTECT_FROM_MELEE, Prayer.PIETY, bounds, "Melee Nylo Detected"));
					break;
				case NpcID.NYLOCAS_VASILIAS_8356:
					executorService.submit(() -> handleNylo(getRange(), Prayer.PROTECT_FROM_MISSILES, Prayer.RIGOUR, bounds, "Ranged Nylo Detected"));
					break;
				case NpcID.NYLOCAS_VASILIAS_8357:
					executorService.submit(() -> handleNylo(getMage(), Prayer.PROTECT_FROM_MAGIC, Prayer.AUGURY, bounds, "Mage Nylo Detected"));
					break;
			}
		}
	}

	private void onChatMessage(ChatMessage event)
	{
		if (config.testing())
		{
			if (event.getType() == ChatMessageType.PUBLICCHAT)
			{
				switch (event.getMessage().toLowerCase())
				{
					case "1":
						executorService.submit(() -> handleNylo(getMage(), Prayer.PROTECT_FROM_MAGIC, Prayer.AUGURY, null, "Mage Nylo Detected"));
						break;
					case "2":
						executorService.submit(() -> handleNylo(getRange(), Prayer.PROTECT_FROM_MISSILES, Prayer.RIGOUR, null, "Ranged Nylo Detected"));
						break;
					case "3":
						executorService.submit(() -> handleNylo(getMelee(), Prayer.PROTECT_FROM_MELEE, Prayer.PIETY, null, "Melee Nylo Detected"));
						break;
					case "4":
						executorService.submit(() -> handleBarrage("Yeet"));
						break;
				}
			}
		}
	}

	private void handleNylo(List<WidgetItem> itemList, Prayer prayer, Prayer secondPrayer, Rectangle nyloBounds, String logs)
	{
		clickItem(itemList);
		if (!client.isPrayerActive(prayer))
		{
			clickWidget(prayer.getWidgetInfo(), Tab.PRAYER);
		}
		if (!client.isPrayerActive(secondPrayer))
		{
			clickWidget(secondPrayer.getWidgetInfo(), Tab.PRAYER);
		}
		if (nyloBounds != null)
		{
			handleSwitch(nyloBounds);
		}
		if (config.backToInventory())
		{
			flexo.keyPress(TabUtils.getTabHotkey(Tab.INVENTORY, client));
		}
		log.info(logs);
	}

	private void handleBarrage(String logger)
	{
		if (!client.isPrayerActive(Prayer.AUGURY))
		{
			clickWidget(Prayer.AUGURY.getWidgetInfo(), Tab.PRAYER);
		}
		clickItem(getMage());
		clickWidget(client.getBoostedSkillLevel(Skill.MAGIC) >= 94 ? WidgetInfo.SPELL_ICE_BARRAGE : WidgetInfo.SPELL_ICE_BURST, Tab.MAGIC);
		log.info(logger);
	}

	private void clickItem(List<WidgetItem> itemList)
	{
		if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
		{
			flexo.keyPress(TabUtils.getTabHotkey(Tab.INVENTORY, client));
		}
		if (itemList.isEmpty())
		{
			return;
		}
		for (WidgetItem item : itemList)
		{
			if (item != null)
			{
				log.info("Grabbing Bounds and CP of: " + itemManager.getItemDefinition(item.getId()).getName());
				handleSwitch(item.getCanvasBounds());
			}
		}
	}

	private void clickWidget(WidgetInfo widgetInfo, Tab tab)
	{
		flexo.keyPress(TabUtils.getTabHotkey(tab, client));

		if (widgetInfo != null)
		{
			Widget widget = client.getWidget(widgetInfo);

			if (widget != null)
			{
				handleSwitch(widget.getBounds());
			}
		}
	}

	private void handleSwitch(Rectangle rectangle)
	{
		ExtUtils.handleSwitch(rectangle, config.actionType(), flexo, client, configManager.getConfig(StretchedModeConfig.class).scalingFactor(), (int) getMillis());
	}

	private void reset()
	{
		lock1 = false;
		lock2 = false;
		lock3 = false;
		flexo = null;
		maiden = null;
		nylo = null;
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}
}
