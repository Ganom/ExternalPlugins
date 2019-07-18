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
import java.util.ArrayList;
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
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.plugins.tickeater.utils.ExtUtils;
import net.runelite.client.plugins.tickeater.utils.Tab;
import net.runelite.client.plugins.tickeater.utils.TabUtils;


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
	private ItemManager itemManager;
	@Inject
	private ConfigManager configManager;
	@Inject
	private EventBus eventBus;
	private Flexo flexo;
	private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());
	private int ticksRemaining;
	private List<Projectile> projectiles = new ArrayList<>();

	@Provides
	TickEaterConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TickEaterConfig.class);
	}

	@Override
	protected void startUp()
	{
		addSubscriptions();
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

	@Override
	protected void shutDown()
	{
		flexo = null;
		eventBus.unregister(this);
	}

	private void addSubscriptions()
	{
		eventBus.subscribe(ProjectileSpawned.class, this, this::onProjectileSpawned);
		eventBus.subscribe(AnimationChanged.class, this, this::onAnimationChanged);
		eventBus.subscribe(GameTick.class, this, this::onGameTick);
	}

	private void onProjectileSpawned(ProjectileSpawned event)
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

	private void onAnimationChanged(AnimationChanged event)
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

	private void onGameTick(GameTick event)
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

		executorService.submit(() ->
		{
			if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
			{
				flexo.keyPress(TabUtils.getTabHotkey(Tab.INVENTORY, client));
			}

			WidgetItem items = list.iterator().next();

			Point point = ExtUtils.getClickPoint(items.getCanvasBounds(), configManager.getConfig(StretchedModeConfig.class).scalingFactor(), client.isStretchedEnabled());
			ExtUtils.leftClick(point.getX(), point.getY(), client, configManager.getConfig(StretchedModeConfig.class).scalingFactor());

			flexo.delay(60);
		});
	}

	private int[] projectiles()
	{
		return ExtUtils.stringToIntArray(config.projectiles());
	}

	private int[] animations()
	{
		return ExtUtils.stringToIntArray(config.animations());
	}

	private List<WidgetItem> getFood()
	{
		return ExtUtils.getItems(ExtUtils.stringToIntArray(config.food()), client);
	}
}
