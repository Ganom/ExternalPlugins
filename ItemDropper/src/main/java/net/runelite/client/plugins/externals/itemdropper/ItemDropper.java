/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.itemdropper;

import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.queries.InventoryWidgetItemQuery;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
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
	private final Set<Integer> ids = new HashSet<>();
	private final Set<String> names = new HashSet<>();

	private boolean iterating;
	private int iterTicks;

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
				.idEquals(ids)
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
		try
		{
			flexo = new Flexo();
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
		updateConfig();
	}

	@Override
	protected void shutDown()
	{
		keyManager.unregisterKeyListener(toggle);
		flexo = null;
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
					clearNames();
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
			clearNames();
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

	private void dropItems(List<WidgetItem> dropList)
	{
		iterating = true;

		for (String name : names)
		{
			menuManager.addPriorityEntry("drop", name);
			menuManager.addPriorityEntry("release", name);
			menuManager.addPriorityEntry("destroy", name);
		}

		List<Rectangle> rects = new ArrayList<>();

		for (WidgetItem item : dropList)
		{
			rects.add(item.getCanvasBounds());
		}

		executorService.submit(() ->
		{
			for (Rectangle rect : rects)
			{
				ExtUtils.handleSwitch(
					rect,
					config.actionType(),
					flexo,
					client,
					configManager.getConfig(StretchedModeConfig.class).scalingFactor()
				);

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

		for (int i : stringToIntArray(config.items()))
		{
			ids.add(i);
		}

		clearNames();

		if (client.getGameState() == GameState.LOGGED_IN)
		{
			names.clear();

			for (int i : ids)
			{
				final String name = Text.standardize(itemManager.getItemDefinition(i).getName());
				names.add(name);
			}
		}
	}

	private void clearNames()
	{
		for (String name : names)
		{
			menuManager.removePriorityEntry("drop", name);
			menuManager.removePriorityEntry("release", name);
			menuManager.removePriorityEntry("destroy", name);
		}
	}
}
