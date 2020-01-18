/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.itemdropper;

import com.google.inject.Provides;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.InventoryWidgetItemQuery;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import static net.runelite.client.plugins.externals.itemdropper.ExtUtils.stringToIntArray;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.util.HotkeyListener;
import org.apache.commons.lang3.tuple.Pair;

@PluginDescriptor(
	name = "Item Dropper",
	description = "Drops selected items for you.",
	tags = {"item", "drop", "dropper", "bot"},
	type = PluginType.EXTERNAL
)
@Slf4j
@SuppressWarnings("unused")
public class ItemDropper extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private ConfigManager configManager;
	@Inject
	private ItemDropperConfig config;
	@Inject
	private KeyManager keyManager;
	@Inject
	private MenuManager menuManager;
	@Inject
	private ItemManager itemManager;

	private final List<WidgetItem> items = new ArrayList<>();

	private Flexo flexo;
	private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 25, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());

	private final HotkeyListener toggle = new HotkeyListener(() -> config.toggle())
	{
		@Override
		public void hotkeyPressed()
		{
			List<WidgetItem> list = new InventoryWidgetItemQuery()
				.idEquals(stringToIntArray(config.items()))
				.result(client)
				.list;

			items.addAll(list);
		}
	};

	@Provides
	ItemDropperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ItemDropperConfig.class);
	}

	@Override
	protected void startUp()
	{
		Flexo.client = client;
		keyManager.registerKeyListener(toggle);
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
		keyManager.unregisterKeyListener(toggle);
		flexo = null;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!items.isEmpty())
		{
			return;
		}

		dropItems(items);
		items.clear();
	}

	private void dropItems(List<WidgetItem> dropList)
	{
		List<Pair<Rectangle, String>> pairList = new ArrayList<>();

		for (WidgetItem item : dropList)
		{
			final String name = itemManager.getItemDefinition(item.getId()).getName();
			final Pair<Rectangle, String> pair = Pair.of(item.getCanvasBounds(), name);
			setEntry(name, false);
			pairList.add(pair);
		}

		executorService.submit(() ->
		{
			for (Pair<Rectangle, String> pair : pairList)
			{
				ExtUtils.handleSwitch(
					pair.getLeft(),
					config.actionType(),
					flexo,
					client,
					configManager.getConfig(StretchedModeConfig.class).scalingFactor(),
					(int) getMillis());
			}
		});

		pairList.forEach(pair -> setEntry(pair.getRight(), true));
	}

	private void setEntry(String name, boolean remove)
	{
		if (remove)
		{
			menuManager.removePriorityEntry("drop", name);
			menuManager.removePriorityEntry("release", name);
			menuManager.removePriorityEntry("destroy", name);
			return;
		}
		menuManager.addPriorityEntry("drop", name);
		menuManager.addPriorityEntry("release", name);
		menuManager.addPriorityEntry("destroy", name);
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}
}
