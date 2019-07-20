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
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.gearswapper.utils.ExtUtils;
import net.runelite.client.plugins.gearswapper.utils.Tab;
import net.runelite.client.plugins.gearswapper.utils.TabUtils;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
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
	private ItemManager itemManager;
	@Inject
	private EventBus eventBus;

	private BlockingQueue queue = new ArrayBlockingQueue(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 2, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());
	private double scalingfactor;
	private Flexo flexo;

	private final HotkeyListener mage = new HotkeyListener(() -> config.hotkeyMage())
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> executeSwap(ExtUtils.getItems(ExtUtils.stringToIntArray(config.mageSet()), client),
				ExtUtils.getEquippedItems(ExtUtils.stringToIntArray(config.removeMageSet()), client)));
			log.info("Mage Hotkey Pressed");
		}
	};
	private final HotkeyListener range = new HotkeyListener(() -> config.hotkeyRange())
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> executeSwap(ExtUtils.getItems(ExtUtils.stringToIntArray(config.rangeSet()), client),
				ExtUtils.getEquippedItems(ExtUtils.stringToIntArray(config.removeRangeSet()), client)));
			log.info("Range Hotkey Pressed");
		}
	};
	private final HotkeyListener melee = new HotkeyListener(() -> config.hotkeyMelee())
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> executeSwap(ExtUtils.getItems(ExtUtils.stringToIntArray(config.meleeSet()), client),
				ExtUtils.getEquippedItems(ExtUtils.stringToIntArray(config.removeMeleeSet()), client)));
			log.info("Melee Hotkey Pressed");
		}
	};
	private final HotkeyListener util = new HotkeyListener(() -> config.hotkeyUtil())
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> equipItem(ExtUtils.getItems(ExtUtils.stringToIntArray(config.util()), client)));
			log.info("Util Hotkey Pressed");
		}
	};

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
		scalingfactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
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
		eventBus.subscribe(ConfigChanged.class, this, this::onConfigChanged);
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
			scalingfactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
		}
	}

	private void equipItem(List<WidgetItem> items)
	{
		if (items.isEmpty())
		{
			return;
		}

		if (client.getVar(VarClientInt.PLAYER_INVENTORY_OPENED) != 3)
		{
			flexo.keyPress(TabUtils.getTabHotkey(Tab.INVENTORY, client));
			flexo.delay(25);
		}

		for (WidgetItem item : items)
		{
			if (item != null)
			{
				if (item.getCanvasBounds() != null)
				{
					ExtUtils.handleSwitch(item.getCanvasBounds(), config.actionType(), flexo, client, scalingfactor, (int) getMillis());
				}
			}
		}
	}

	private void removeItem(List<Widget> items)
	{
		if (items.isEmpty())
		{
			return;
		}

		if (client.getVar(VarClientInt.PLAYER_INVENTORY_OPENED) != 4)
		{
			flexo.keyPress(TabUtils.getTabHotkey(Tab.EQUIPMENT, client));
			flexo.delay(35);
		}

		for (Widget item : items)
		{
			if (item != null)
			{
				if (item.getBounds() != null)
				{
					ExtUtils.handleSwitch(item.getBounds(), config.actionType(), flexo, client, scalingfactor, (int) getMillis());
				}
			}
		}
	}

	private void executeSwap(List<WidgetItem> equip, List<Widget> remove)
	{
		equipItem(equip);

		if (client.getVar(VarClientInt.PLAYER_INVENTORY_OPENED) != 4 && remove.size() > 0)
		{
			flexo.keyPress(TabUtils.getTabHotkey(Tab.EQUIPMENT, client));
		}

		removeItem(remove);

		if (client.getVar(VarClientInt.PLAYER_INVENTORY_OPENED) != 3)
		{
			flexo.keyPress(TabUtils.getTabHotkey(Tab.INVENTORY, client));
		}
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}
}
