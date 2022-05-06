/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.itemdropper;

import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.externals.utils.ExtUtils;
import net.runelite.client.util.HotkeyListener;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "Item Dropper",
	description = "Drops selected items for you.",
	tags = {"item", "drop", "dropper", "bot", "ganom"}
)
@Slf4j
@SuppressWarnings("unused")
@PluginDependency(ExtUtils.class)
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
	@Inject
	private ClientThread clientThread;
	@Inject
	private ExtUtils utils;

	private final List<Widget> items = new ArrayList<>();
	private final Set<Integer> ids = new HashSet<>();

	private boolean iterating;
	private int iterTicks;

	private Robot robot;
	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private final ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 25, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());

	private final HotkeyListener toggle = new HotkeyListener(() -> config.toggle())
	{
		@Override
		public void hotkeyPressed()
		{
			clientThread.invoke(() -> buildItemList());
		}
	};

	@Provides
	ItemDropperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ItemDropperConfig.class);
	}

	@Override
	protected void startUp() throws AWTException
	{
		robot = new Robot();
		keyManager.registerKeyListener(toggle);
		updateConfig();
	}

	@Override
	protected void shutDown()
	{
		keyManager.unregisterKeyListener(toggle);
		robot = null;
	}

	private void buildItemList()
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		items.clear();

		if (inventoryWidget == null)
		{
			return;
		}

		Arrays.stream(inventoryWidget.getDynamicChildren())
			.filter(item -> ids.contains(item.getItemId()))
			.forEach(items::add);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("ItemDropperConfig"))
		{
			return;
		}

		updateConfig();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (items.isEmpty())
		{
			if (iterating)
			{
				iterTicks++;
				if (iterTicks > 10)
				{
					iterating = false;
				}
			}
			else
			{
				if (iterTicks > 0)
				{
					iterTicks = 0;
				}
			}
			return;
		}

		dropItems(items);
		items.clear();
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getItemContainer() != client.getItemContainer(InventoryID.INVENTORY))
		{
			return;
		}

		int quant = 0;

		for (Item item : event.getItemContainer().getItems())
		{
			if (ids.contains(item.getId()))
			{
				quant++;
			}
		}

		if (iterating && quant == 0)
		{
			iterating = false;
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			updateConfig();
		}
	}

	private void dropItems(List<Widget> dropList)
	{
		iterating = true;

		List<Rectangle> rects = dropList.stream()
			.map(Widget::getBounds)
			.collect(Collectors.toList());

		executorService.submit(() ->
		{
			for (Rectangle rect : rects)
			{
				utils.click(rect);

				try
				{
					Thread.sleep((int) getMillis());
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}

	private void updateConfig()
	{
		ids.clear();

		for (int i : utils.stringToIntArray(config.items()))
		{
			ids.add(i);
		}
	}
}
