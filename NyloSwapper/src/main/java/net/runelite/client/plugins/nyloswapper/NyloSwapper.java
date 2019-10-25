/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.nyloswapper;

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
import net.runelite.api.Skill;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.NpcDefinitionChanged;
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
import net.runelite.client.plugins.nyloswapper.utils.ExtUtils;
import net.runelite.client.plugins.nyloswapper.utils.Tab;
import net.runelite.client.plugins.nyloswapper.utils.TabUtils;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;

@PluginDescriptor(
	name = "Nylo Swapper",
	description = "Nylo Swapper",
	tags = {"tob", "theatre", "blood", "cheats"},
	enabledByDefault = false,
	type = PluginType.EXTERNAL
)

@Slf4j
public class NyloSwapper extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private NyloSwapperConfig config;
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
	private NPC nylo;

	@Provides
	NyloSwapperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NyloSwapperConfig.class);
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
		reset();
		eventBus.unregister(this);
	}

	private void addSubscriptions()
	{
		eventBus.subscribe(GameStateChanged.class, this, this::onGameStateChanged);
		eventBus.subscribe(NpcDefinitionChanged.class, this, this::onNpcDefinitionChanged);
		eventBus.subscribe(ChatMessage.class, this, this::onChatMessage);
	}

	private void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			reset();
		}
	}

	private void onNpcDefinitionChanged(NpcDefinitionChanged event)
	{
		final NPC npc = event.getNpc();

		if (nylo == null)
		{
			return;
		}

		switch (npc.getId())
		{
			case NpcID.NYLOCAS_VASILIAS_8355:
			{
				final Rectangle bounds = npc.getConvexHull().getBounds();
				executorService.submit(() -> handleNylo(getMelee(), Prayer.PROTECT_FROM_MELEE, Prayer.PIETY, bounds, "Melee Nylo Detected"));
			}
			break;
			case NpcID.NYLOCAS_VASILIAS_8356:
			{
				final Rectangle bounds = npc.getConvexHull().getBounds();
				executorService.submit(() -> handleNylo(getRange(), Prayer.PROTECT_FROM_MISSILES, Prayer.RIGOUR, bounds, "Ranged Nylo Detected"));
			}
			break;
			case NpcID.NYLOCAS_VASILIAS_8357:
			{
				final Rectangle bounds = npc.getConvexHull().getBounds();
				executorService.submit(() -> handleNylo(getMage(), Prayer.PROTECT_FROM_MAGIC, Prayer.AUGURY, bounds, "Mage Nylo Detected"));
			}
			break;
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
		flexo = null;
		nylo = null;
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}
}
