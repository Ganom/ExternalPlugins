/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.itemdropper;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemDefinition;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import static net.runelite.client.plugins.itemdropper.ExtUtils.getItems;
import static net.runelite.client.plugins.itemdropper.ExtUtils.stringToIntArray;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
	name = "Item Dropper",
	description = "Drops selected items for you.",
	tags = {"item", "drop", "dropper", "bot"},
	type = PluginType.EXTERNAL
)
@Slf4j
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
	private Flexo flexo;
	private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 25, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());
	private final HotkeyListener toggle = new HotkeyListener(() -> config.toggle())
	{
		@Override
		public void hotkeyPressed()
		{
			List<WidgetItem> items = new ArrayList<>(getItems(stringToIntArray(config.items()), client));
			if (items.isEmpty())
			{
				log.debug("Item list is empty.");
				return;
			}
			dropItems(items);
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

	private void dropItems(List<WidgetItem> dropList)
	{
		executorService.submit(() ->
		{
			for (WidgetItem item : dropList)
			{
				ItemDefinition itemDef = itemManager.getItemDefinition(item.getId());
				final String name = itemDef.getName();
				menuManager.addPriorityEntry("Drop", name);
				ExtUtils.handleSwitch(item.getCanvasBounds(), config.actionType(), flexo, client, configManager.getConfig(StretchedModeConfig.class).scalingFactor(), (int) getMillis());
				menuManager.removePriorityEntry("Drop", name);
			}
		});
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}
}
