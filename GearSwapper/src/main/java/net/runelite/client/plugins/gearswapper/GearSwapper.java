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
package net.runelite.client.plugins.gearswapper;

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
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.vars.InterfaceTab;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.gearswapper.utils.ExtUtils;
import static net.runelite.client.plugins.gearswapper.utils.ExtUtils.getEquippedItems;
import static net.runelite.client.plugins.gearswapper.utils.ExtUtils.getItems;
import static net.runelite.client.plugins.gearswapper.utils.ExtUtils.stringToIntArray;
import net.runelite.client.plugins.gearswapper.utils.Tab;
import net.runelite.client.plugins.gearswapper.utils.TabUtils;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.util.Clipboard;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
	name = "Gear Swapper",
	description = "Gear Swap on Hot Key",
	tags = {"gear", "swap", "helper"},
	type = PluginType.EXTERNAL
)
@Slf4j
public class GearSwapper extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private ConfigManager configManager;
	@Inject
	private GearSwapperConfig config;
	@Inject
	private KeyManager keyManager;
	@Inject
	private EventBus eventBus;

	private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 2, 2, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());
	private double scalingFactor;
	private Flexo flexo;

	@Provides
	GearSwapperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GearSwapperConfig.class);
	}

	protected void startUp()
	{
		keyManager.registerKeyListener(mage);
		keyManager.registerKeyListener(range);
		keyManager.registerKeyListener(melee);
		keyManager.registerKeyListener(util);
		scalingFactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
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
		eventBus.subscribe(ConfigChanged.class, this, this::onConfigChanged);
		eventBus.subscribe(CommandExecuted.class, this, this::onCommandExecuted);
	}

	protected void shutDown()
	{
		flexo = null;
		keyManager.unregisterKeyListener(mage);
		keyManager.unregisterKeyListener(range);
		keyManager.unregisterKeyListener(melee);
		keyManager.unregisterKeyListener(util);
		eventBus.unregister(this);
	}

	private void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("stretchedmode"))
		{
			scalingFactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
		}
	}

	private void onCommandExecuted(CommandExecuted commandExecuted)
	{
		if ("copy".equals(commandExecuted.getCommand()))
		{
			final ItemContainer e = client.getItemContainer(InventoryID.EQUIPMENT);

			if (e == null)
			{
				return;
			}

			final StringBuilder sb = new StringBuilder();

			for (Item item : e.getItems())
			{
				if (item.getId() == -1 || item.getId() == 0)
				{
					continue;
				}

				sb.append(item.getId());
				sb.append(",");
			}

			final String string = sb.toString();
			Clipboard.store(string);
		}
	}

	private void handleItem(List<Rectangle> items, InterfaceTab tab)
	{
		if (items.isEmpty())
		{
			return;
		}

		switch (tab)
		{
			case INVENTORY:
				if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.INVENTORY.getId())
				{
					flexo.keyPress(TabUtils.getTabHotkey(Tab.INVENTORY, client));
					flexo.delay(25);
				}
				break;
			case EQUIPMENT:
				if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.EQUIPMENT.getId())
				{
					flexo.keyPress(TabUtils.getTabHotkey(Tab.EQUIPMENT, client));
					flexo.delay(25);
				}
				break;
		}

		for (Rectangle item : items)
		{
			handleSwitch(item);
		}
	}

	private void executeSwap(List<Rectangle> equip, List<Rectangle> remove)
	{
		handleItem(equip, InterfaceTab.INVENTORY);

		if (client.getWidget(WidgetInfo.EQUIPMENT).isHidden() && remove.size() > 0)
		{
			flexo.keyPress(TabUtils.getTabHotkey(Tab.EQUIPMENT, client));
		}

		handleItem(remove, InterfaceTab.EQUIPMENT);

		if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.INVENTORY.getId())
		{
			flexo.keyPress(TabUtils.getTabHotkey(Tab.INVENTORY, client));
			flexo.delay(25);
		}
	}

	private void executeSwap(String equip, String remove)
	{
		final List<Rectangle> eqBounds = new ArrayList<>();
		final List<Rectangle> invBounds = new ArrayList<>();

		for (WidgetItem item : getItems(stringToIntArray(equip), client))
		{
			if (item == null)
			{
				continue;
			}
			invBounds.add(item.getCanvasBounds());
		}

		for (Widget widget : getEquippedItems(stringToIntArray(remove), client))
		{
			if (widget == null)
			{
				continue;
			}
			eqBounds.add(widget.getBounds());
		}

		executorService.submit(() -> executeSwap(invBounds, eqBounds));
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}

	private void handleSwitch(Rectangle rectangle)
	{
		ExtUtils.handleSwitch(rectangle, config.actionType(), flexo, client, scalingFactor, (int) getMillis());
	}

	private final HotkeyListener mage = new HotkeyListener(() -> config.hotkeyMage())
	{
		@Override
		public void hotkeyPressed()
		{
			executeSwap(config.mageSet(), config.removeMageSet());
		}
	};

	private final HotkeyListener range = new HotkeyListener(() -> config.hotkeyRange())
	{
		@Override
		public void hotkeyPressed()
		{
			executeSwap(config.rangeSet(), config.removeRangeSet());
		}
	};

	private final HotkeyListener melee = new HotkeyListener(() -> config.hotkeyMelee())
	{
		@Override
		public void hotkeyPressed()
		{
			executeSwap(config.meleeSet(), config.removeMeleeSet());
		}
	};

	private final HotkeyListener util = new HotkeyListener(() -> config.hotkeyUtil())
	{
		@Override
		public void hotkeyPressed()
		{
			final List<Rectangle> invBounds = new ArrayList<>();

			for (WidgetItem item : getItems(stringToIntArray(config.util()), client))
			{
				if (item == null)
				{
					continue;
				}
				invBounds.add(item.getCanvasBounds());
			}

			executorService.submit(() -> handleItem(invBounds, InterfaceTab.INVENTORY));
		}
	};
}
