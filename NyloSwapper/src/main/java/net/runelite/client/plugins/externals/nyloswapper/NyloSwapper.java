/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.nyloswapper;

import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.NpcDefinitionChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.utils.ExtUtils;
import net.runelite.client.plugins.externals.utils.Tab;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "Nylo Swapper",
	description = "Nylo Swapper",
	tags = {"tob", "theatre", "blood", "cheats"},
	type = PluginType.UTILITY
)
@Slf4j
@SuppressWarnings("unused")
@PluginDependency(ExtUtils.class)
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
	@Inject
	private ExtUtils utils;

	private Robot robot;
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
		return utils.getItems(utils.stringToIntArray(config.mage()));
	}

	private List<WidgetItem> getRange()
	{
		return utils.getItems(utils.stringToIntArray(config.range()));
	}

	private List<WidgetItem> getMelee()
	{
		return utils.getItems(utils.stringToIntArray(config.melee()));
	}

	@Override
	protected void startUp() throws AWTException
	{
		robot = new Robot();
		reset();
	}

	@Override
	protected void shutDown()
	{
		reset();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			reset();
		}
	}

	@Subscribe
	public void onNpcDefinitionChanged(NpcDefinitionChanged event)
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
				executorService.submit(() -> handleNylo(
					getMelee(),
					Prayer.PROTECT_FROM_MELEE,
					Prayer.PIETY,
					bounds,
					"Melee Nylo Detected"
				));
			}
			break;
			case NpcID.NYLOCAS_VASILIAS_8356:
			{
				final Rectangle bounds = npc.getConvexHull().getBounds();
				executorService.submit(() -> handleNylo(
					getRange(),
					Prayer.PROTECT_FROM_MISSILES,
					Prayer.RIGOUR,
					bounds,
					"Ranged Nylo Detected"
				));
			}
			break;
			case NpcID.NYLOCAS_VASILIAS_8357:
			{
				final Rectangle bounds = npc.getConvexHull().getBounds();
				executorService.submit(() -> handleNylo(
					getMage(),
					Prayer.PROTECT_FROM_MAGIC,
					Prayer.AUGURY,
					bounds,
					"Mage Nylo Detected"
				));
			}
			break;
		}
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted event)
	{
		if (!event.getCommand().equalsIgnoreCase("nylo") || event.getArguments().length < 1)
		{
			return;
		}

		switch (event.getArguments()[1])
		{
			case "mage":
				executorService.submit(() -> handleNylo(getMage(), Prayer.PROTECT_FROM_MAGIC, Prayer.AUGURY, null, "Mage Nylo Detected"));
				break;
			case "range":
				executorService.submit(() -> handleNylo(getRange(), Prayer.PROTECT_FROM_MISSILES, Prayer.RIGOUR, null, "Ranged Nylo Detected"));
				break;
			case "melee":
				executorService.submit(() -> handleNylo(getMelee(), Prayer.PROTECT_FROM_MELEE, Prayer.PIETY, null, "Melee Nylo Detected"));
				break;
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
			utils.click(nyloBounds);
			try
			{
				Thread.sleep(getMillis());
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		if (config.backToInventory())
		{
			robot.keyPress(utils.getTabHotkey(Tab.INVENTORY));
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
		clickWidget(client.getBoostedSkillLevel(Skill.MAGIC) >= 94 ? WidgetInfo.SPELL_ICE_BARRAGE : WidgetInfo.SPELL_ICE_BURST, Tab.SPELLBOOK);
		log.info(logger);
	}

	private void clickItem(List<WidgetItem> itemList)
	{
		if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
		{
			robot.keyPress(utils.getTabHotkey(Tab.INVENTORY));
		}
		if (itemList.isEmpty())
		{
			return;
		}
		for (WidgetItem item : itemList)
		{
			if (item != null)
			{
				utils.click(item.getCanvasBounds());
				try
				{
					Thread.sleep(getMillis());
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private void clickWidget(WidgetInfo widgetInfo, Tab tab)
	{
		robot.keyPress(utils.getTabHotkey(tab));

		if (widgetInfo != null)
		{
			Widget widget = client.getWidget(widgetInfo);

			if (widget != null)
			{
				utils.click(widget.getBounds());
				try
				{
					Thread.sleep(getMillis());
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private void reset()
	{
		robot = null;
		nylo = null;
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}
}
