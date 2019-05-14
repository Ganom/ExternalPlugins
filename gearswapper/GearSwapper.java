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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
	name = "Gear Swapper",
	description = "Gear Swap on Hot Key",
	tags = {"gear", "swap", "helper"},
	type = PluginType.EXTERNAL
)
public class GearSwapper extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private ConfigManager externalConfig;
	@Inject
	private GearSwapperConfig config;
	@Inject
	private KeyManager keyManager;
	@Getter
	private Widget widget;
	@Inject
	private TabUtils tabUtils;
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private double scalingfactor;
	private Flexo flexo;

	@Provides
	GearSwapperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GearSwapperConfig.class);
	}

	private final HotkeyListener mage = new HotkeyListener(() -> config.hotkeyMage())
	{
		@Override
		public void hotkeyPressed()
		{
			executeMage();
		}
	};

	private final HotkeyListener range = new HotkeyListener(() -> config.hotkeyRange())
	{
		@Override
		public void hotkeyPressed()
		{
			executeRange();
		}
	};

	private final HotkeyListener melee = new HotkeyListener(() -> config.hotkeyMelee())
	{
		@Override
		public void hotkeyPressed()
		{
			executeMelee();
		}
	};

	private final HotkeyListener util = new HotkeyListener(() -> config.hotkeyUtil())
	{
		@Override
		public void hotkeyPressed()
		{
			executeUtil();
		}
	};

	protected void startUp()
	{
		keyManager.registerKeyListener(mage);
		keyManager.registerKeyListener(range);
		keyManager.registerKeyListener(melee);
		keyManager.registerKeyListener(util);
		scalingfactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
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
		keyManager.unregisterKeyListener(mage);
		keyManager.unregisterKeyListener(range);
		keyManager.unregisterKeyListener(melee);
		keyManager.unregisterKeyListener(util);
	}

	private List<WidgetItem> getMage()
	{
		String gear = config.mainhandMage() + "," + config.offhandMage() + "," +
			config.helmetMage() + "," + config.capeMage() + "," + config.neckMage() + "," +
			config.bodyMage() + "," + config.legsMage() + "," + config.ringMage() + "," +
			config.bootsMage() + "," + config.glovesMage();
		int[] gearIds = Arrays.stream(gear.split(","))
			.map(String::trim).mapToInt(Integer::parseInt).toArray();
		return getItems(gearIds);
	}

	private List<WidgetItem> getRange()
	{
		String gear = config.mainhandRange() + "," + config.offhandRange() + "," +
			config.helmetRange() + "," + config.capeRange() + "," + config.neckRange() + "," +
			config.bodyRange() + "," + config.legsRange() + "," + config.ringRange() + "," +
			config.bootsRange() + "," + config.glovesRange();
		int[] gearIds = Arrays.stream(gear.split(","))
			.map(String::trim).mapToInt(Integer::parseInt).toArray();
		return getItems(gearIds);
	}

	private List<WidgetItem> getMelee()
	{
		String gear = config.mainhandMelee() + "," + config.offhandMelee() + "," +
			config.helmetMelee() + "," + config.capeMelee() + "," + config.neckMelee() + "," +
			config.bodyMelee() + "," + config.legsMelee() + "," + config.ringMelee() + "," +
			config.bootsMelee() + "," + config.glovesMelee();
		int[] gearIds = Arrays.stream(gear.split(","))
			.map(String::trim).mapToInt(Integer::parseInt).toArray();
		return getItems(gearIds);
	}

	private List<WidgetItem> getUtil()
	{
		String gear = config.util();
		int[] gearIds = Arrays.stream(gear.split(","))
			.map(String::trim).mapToInt(Integer::parseInt).toArray();
		return getItems(gearIds);
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

	private Point getClickPoint(Rectangle2D rect)
	{
		if (client.isStretchedEnabled())
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + (rand * 2) + rect.getWidth() / 2);
			int y = (int) (rect.getY() + (rand * 2) + rect.getHeight() / 2);
			double scale = 1 + (scalingfactor / 100);
			return new Point((int) (x * scale), (int) (y * scale));
		}
		else
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + (rand * 2) + rect.getWidth() / 2);
			int y = (int) (rect.getY() + (rand * 2) + rect.getHeight() / 2);
			return new Point(x, y);
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		scalingfactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
	}

	private void executeMelee()
	{
		executorService.submit(() -> {
			for (WidgetItem melee : getMelee())
			{
				if (getMelee().isEmpty())
				{
					break;
				}
				if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
				{
					flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
					try
					{
						Thread.sleep(20);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				if (melee.getCanvasBounds().getY() <= 0)
				{
					break;
				}
				if (!getMelee().isEmpty())
				{
					Rectangle boundsMelee = melee.getCanvasBounds();
					Point clickPointMelee = getClickPoint(boundsMelee);
					if (!config.flexoMode())
					{
						leftClick(clickPointMelee.getX(), clickPointMelee.getY());
						try
						{
							Thread.sleep(getMillis());
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						flexo.mouseMove(clickPointMelee.getX(), clickPointMelee.getY());
						flexo.mousePressAndRelease(1);
					}
				}
			}
		});
	}

	private void executeUtil()
	{
		executorService.submit(() -> {
			for (WidgetItem util : getUtil())
			{
				if (getUtil().isEmpty())
				{
					break;
				}
				if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
				{
					flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
					try
					{
						Thread.sleep(20);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				if (util.getCanvasBounds().getY() <= 0)
				{
					break;
				}
				if (!getUtil().isEmpty())
				{
					Rectangle boundsUtil = util.getCanvasBounds();
					Point clickPointUtil = getClickPoint(boundsUtil);
					if (!config.flexoMode())
					{
						leftClick(clickPointUtil.getX(), clickPointUtil.getY());
						try
						{
							Thread.sleep(getMillis());
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						flexo.mouseMove(clickPointUtil.getX(), clickPointUtil.getY());
						flexo.mousePressAndRelease(1);
					}
				}
			}
		});
	}

	private void executeRange()
	{
		executorService.submit(() -> {
			for (WidgetItem Range : getRange())
			{
				if (getRange().isEmpty())
				{
					break;
				}
				if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
				{
					flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
					try
					{
						Thread.sleep(20);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				if (Range.getCanvasBounds().getY() <= 0)
				{
					break;
				}
				if (!getRange().isEmpty())
				{
					Rectangle boundsRange = Range.getCanvasBounds();
					Point clickPointRange = getClickPoint(boundsRange);
					if (!config.flexoMode())
					{
						leftClick(clickPointRange.getX(), clickPointRange.getY());
						try
						{
							Thread.sleep(getMillis());
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						flexo.mouseMove(clickPointRange.getX(), clickPointRange.getY());
						flexo.mousePressAndRelease(1);
					}
				}
			}
		});
	}

	private void executeMage()
	{
		executorService.submit(() -> {
			for (WidgetItem Mage : getMage())
			{
				if (getMage().isEmpty())
				{
					break;
				}
				if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
				{
					flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
					try
					{
						Thread.sleep(20);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				if (Mage.getCanvasBounds().getY() <= 0)
				{
					break;
				}
				if (!getMage().isEmpty())
				{
					Rectangle boundsMage = Mage.getCanvasBounds();
					Point clickPointMage = getClickPoint(boundsMage);
					if (!config.flexoMode())
					{
						leftClick(clickPointMage.getX(), clickPointMage.getY());
						try
						{
							Thread.sleep(getMillis());
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						flexo.mouseMove(clickPointMage.getX(), clickPointMage.getY());
						flexo.mousePressAndRelease(1);
					}
				}
			}
		});
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}

	private void leftClick(int x, int y)
	{
		if (client.isStretchedEnabled())
		{
			Point p = this.client.getMouseCanvasPosition();
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
			Point p = this.client.getMouseCanvasPosition();
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
}
