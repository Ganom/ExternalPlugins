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
package net.runelite.client.plugins.tickeater;

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
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Projectile;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;


@PluginDescriptor(
	name = "Tick Eater",
	description = "Tick Eater",
	type = PluginType.EXTERNAL
)

@Slf4j
public class TickEater extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private TickEaterConfig config;

	@Inject
	private TabUtils tabUtils;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ConfigManager configManager;

	private Flexo flexo;
	private BlockingQueue queue = new ArrayBlockingQueue(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());
	private int ticksRemaining;
	private List<Projectile> projectiles = new ArrayList<>();

	@Provides
	TickEaterConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TickEaterConfig.class);
	}

	@Subscribe
	public void onProjectileSpawned(ProjectileSpawned event)
	{
		Projectile projectile = event.getProjectile();

		for (int p : projectiles())
		{
			if (projectile.getId() == p && projectile.getInteracting() == client.getLocalPlayer())
			{
				projectiles.add(projectile);
			}
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		Actor actor = event.getActor();

		for (int a : animations())
		{
			if (actor.getAnimation() == a && actor.getInteracting() == client.getLocalPlayer())
			{
				foodHandler(getFood());
			}
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!projectiles.isEmpty())
		{
			for (Projectile p : projectiles)
			{
				if ((p.getRemainingCycles() / 30) == 0)
				{
					foodHandler(getFood());
				}
			}
		}
	}

	private void foodHandler(List<WidgetItem> list)
	{
		if (list.isEmpty())
		{
			return;
		}
		executorService.submit(() -> {
			if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
			{
				flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
			}
			WidgetItem items = list.iterator().next();
			log.info("Grabbing Bounds and CP of: " + itemManager.getItemDefinition(items.getId()).getName());
			Point point = getClickPoint(items.getCanvasBounds());
			leftClick(point.getX(), point.getY());
			try
			{
				Thread.sleep(70);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		});
	}

	private int[] projectiles()
	{
		return Arrays.stream(config.projectiles().split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
	}

	private int[] animations()
	{
		return Arrays.stream(config.animations().split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
	}

	private List<WidgetItem> getFood()
	{
		int[] food = Arrays.stream(config.food().split(","))
			.map(String::trim).mapToInt(Integer::parseInt).toArray();
		return getItems(food);
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

	private void moveMouse(int x, int y)
	{
		MouseEvent mouseEntered = new MouseEvent(this.client.getCanvas(), 504, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseEntered);
		MouseEvent mouseExited = new MouseEvent(this.client.getCanvas(), 505, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseExited);
		MouseEvent mouseMoved = new MouseEvent(this.client.getCanvas(), 503, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseMoved);
	}

	private void leftClick(int x, int y)
	{
		if (client.isStretchedEnabled())
		{
			double scalingfactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
			Point p = this.client.getMouseCanvasPosition();
			if (p.getX() != x || p.getY() != y)
			{
				this.moveMouse(x, y);
			}
			double scale = 1 + (scalingfactor / 100);
			log.info("Scale: " + Double.toString(scale));

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

	private Point getClickPoint(Rectangle rect)
	{
		double scalingfactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
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
