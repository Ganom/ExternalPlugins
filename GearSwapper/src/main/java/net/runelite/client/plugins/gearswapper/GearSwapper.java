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
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
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
	private TabUtils tabUtils;
	@Inject
	private ItemManager itemManager;

	private BlockingQueue queue = new ArrayBlockingQueue(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());
	private double scalingfactor;
	private Flexo flexo;

	private final HotkeyListener mage = new HotkeyListener(() -> config.hotkeyMage())
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> executeSwap(getItems(stringToIntArray(config.mageSet())), getEquippedItems(stringToIntArray(config.removeMageSet()))));
			log.info("Mage Hotkey Pressed");
		}
	};
	private final HotkeyListener range = new HotkeyListener(() -> config.hotkeyRange())
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> executeSwap(getItems(stringToIntArray(config.rangeSet())), getEquippedItems(stringToIntArray(config.removeRangeSet()))));
			log.info("Range Hotkey Pressed");
		}
	};
	private final HotkeyListener melee = new HotkeyListener(() -> config.hotkeyMelee())
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> executeSwap(getItems(stringToIntArray(config.meleeSet())), getEquippedItems(stringToIntArray(config.removeMeleeSet()))));
			log.info("Melee Hotkey Pressed");
		}
	};
	private final HotkeyListener util = new HotkeyListener(() -> config.hotkeyUtil())
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> equipItem(getItems(stringToIntArray(config.util()))));
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
	}

	protected void shutDown()
	{
		flexo = null;
		keyManager.unregisterKeyListener(mage);
		keyManager.unregisterKeyListener(range);
		keyManager.unregisterKeyListener(melee);
		keyManager.unregisterKeyListener(util);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
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

		if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
			flexo.delay(25);
		}

		for (WidgetItem item : items)
		{
			if (item != null)
			{
				if (itemManager.getItemDefinition(item.getId()) != null)
				{
					log.info("Grabbing Bounds and CP of: " + itemManager.getItemDefinition(item.getId()).getName());
				}
				if (item.getCanvasBounds() != null)
				{
					handleSwitch(item.getCanvasBounds());
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

		if (client.getWidget(WidgetInfo.EQUIPMENT).isHidden())
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.EQUIPMENT));
			flexo.delay(35);
		}

		for (Widget item : items)
		{
			if (item != null)
			{
				if (itemManager.getItemDefinition(item.getItemId()) != null)
				{
					log.info("Grabbing Bounds and CP of: " + itemManager.getItemDefinition(item.getId()).getName());
				}
				if (item.getBounds() != null)
				{
					handleSwitch(item.getBounds());
				}
			}
		}
	}

	private void handleSwitch(Rectangle rectangle)
	{
		Point cp = getClickPoint(rectangle);

		if (cp.getX() >= 1)
		{
			switch (config.actionType())
			{
				case FLEXO:
					flexo.mouseMove(cp.getX(), cp.getY());
					flexo.mousePressAndRelease(1);
					break;
				case MOUSEEVENTS:
					leftClick(cp.getX(), cp.getY());
					flexo.delay((int) getMillis());
					break;
			}
		}
	}

	private int[] stringToIntArray(String string)
	{
		return Arrays.stream(string.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
	}

	private void executeSwap(List<WidgetItem> equip, List<Widget> remove)
	{
		equipItem(equip);

		if (client.getWidget(WidgetInfo.EQUIPMENT).isHidden() && remove.size() > 0)
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.EQUIPMENT));
		}

		removeItem(remove);

		if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
		}
	}

	private List<WidgetItem> getItems(int... itemIds)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

		ArrayList<Integer> itemIDs = new ArrayList<>();

		for (int i : itemIds)
		{
			itemIDs.add(i);
		}

		List<WidgetItem> listToReturn = new ArrayList<>();

		for (WidgetItem item : inventoryWidget.getWidgetItems())
		{
			if (itemIDs.contains(item.getId()))
			{
				listToReturn.add(item);
			}
		}

		return listToReturn;
	}

	private List<Widget> getEquippedItems(int... itemIds)
	{
		Widget equipmentWidget = client.getWidget(WidgetInfo.EQUIPMENT);

		ArrayList<Integer> equippedIds = new ArrayList<>();

		for (int i : itemIds)
		{
			equippedIds.add(i);
		}

		List<Widget> equipped = new ArrayList<>();

		if (equipmentWidget.getStaticChildren() != null)
		{
			for (Widget widgets : equipmentWidget.getStaticChildren())
			{
				for (Widget items : widgets.getDynamicChildren())
				{
					if (equippedIds.contains(items.getItemId()))
					{
						equipped.add(items);
					}
				}
			}
		}
		else
		{
			log.error("Static Children is Null!");
		}

		return equipped;
	}

	private void spec()
	{
		if (client.getWidget(WidgetInfo.MINIMAP_SPEC_SPRITE).getSpriteId() == 1607 || client.getWidget(WidgetInfo.MINIMAP_SPEC_SPRITE).getSpriteId() == 1608)
		{
			handleSwitch(client.getWidget(WidgetInfo.MINIMAP_SPEC_ORB).getBounds());
		}
		else
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.ATTACK));
			try
			{
				Thread.sleep(getMillis());
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			handleSwitch(client.getWidget(WidgetInfo.COMBAT_SPEC_CLICKBOX).getBounds());
			if (config.backToInventory())
			{
				flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
			}
		}
	}
	
	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}

	private void leftClick(int x, int y)
	{
		if (client.isStretchedEnabled())
		{
			net.runelite.api.Point p = this.client.getMouseCanvasPosition();
			if (p.getX() != x || p.getY() != y)
			{
				this.moveMouse(x, y);
			}
			double scale = 1 + (scalingfactor / 100);

			MouseEvent mousePressed =
				new MouseEvent(this.client.getCanvas(), 501, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * scale), (int) (this.client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased =
				new MouseEvent(this.client.getCanvas(), 502, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * scale), (int) (this.client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked =
				new MouseEvent(this.client.getCanvas(), 500, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * scale), (int) (this.client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseClicked);
		}
		if (!client.isStretchedEnabled())
		{
			net.runelite.api.Point p = this.client.getMouseCanvasPosition();
			if (p.getX() != x || p.getY() != y)
			{
				this.moveMouse(x, y);
			}
			MouseEvent mousePressed = new MouseEvent(this.client.getCanvas(), 501, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased = new MouseEvent(this.client.getCanvas(), 502, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked = new MouseEvent(this.client.getCanvas(), 500, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseClicked);
		}
	}

	private void moveMouse(int x, int y)
	{
		MouseEvent mouseEntered = new MouseEvent(this.client.getCanvas(), 504, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseEntered);
		MouseEvent mouseExited = new MouseEvent(this.client.getCanvas(), 505, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseExited);
		MouseEvent mouseMoved = new MouseEvent(this.client.getCanvas(), 503, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseMoved);
	}

	private Point getClickPoint(Rectangle rect)
	{
		if (client.isStretchedEnabled())
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
			int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);
			double scale = 1 + (scalingfactor / 100);
			return new Point((int) (x * scale), (int) (y * scale));
		}
		else
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
			int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);
			return new Point(x, y);
		}
	}
}
