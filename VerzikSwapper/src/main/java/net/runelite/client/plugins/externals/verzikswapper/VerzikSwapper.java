/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * Copyright (c) 2019-2020, andrewterra <https://github.com/andrewterra>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.verzikswapper;

import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.vars.InterfaceTab;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.utils.ExtUtils;
import net.runelite.client.plugins.externals.utils.Tab;
import net.runelite.client.util.HotkeyListener;
import org.jetbrains.annotations.NotNull;
import org.pf4j.Extension;


@Extension
@PluginDescriptor(
	name = "Verzik Pray Swapper",
	description = "Automatically swaps prayers for ToB P3 Verzik",
	tags = {"prayer", "verzik", "bot", "swap"},
	type = PluginType.UTILITY
)
@Slf4j
@SuppressWarnings("unused")
@PluginDependency(ExtUtils.class)
public class VerzikSwapper extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private VerzikSwapperConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private ExtUtils utils;

	@Inject
	private KeyManager keyManager;

	private ExecutorService executor;
	private boolean swapMage;
	private boolean swapRange;
	private Robot robot;

	@Provides
	VerzikSwapperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(VerzikSwapperConfig.class);
	}

	@Override
	protected void startUp() throws AWTException
	{
		executor = Executors.newSingleThreadExecutor();
		keyManager.registerKeyListener(hotkeyListenerRange);
		keyManager.registerKeyListener(hotkeyListenerMage);
		robot = new Robot();
	}

	@Override
	protected void shutDown()
	{
		executor.shutdown();
		keyManager.unregisterKeyListener(hotkeyListenerRange);
		keyManager.unregisterKeyListener(hotkeyListenerMage);

		robot = null;
	}

	// used for testing range Projectile
	private HotkeyListener hotkeyListenerRange = new HotkeyListener(() -> config.testRange())
	{
		@Override
		public void hotkeyPressed()
		{
			eventBus.post(ProjectileMoved.class, projBuilder(1593));
		}
	};

	// used for testing mage Projectile
	private HotkeyListener hotkeyListenerMage = new HotkeyListener(() -> config.testMage())
	{
		@Override
		public void hotkeyPressed()
		{
			eventBus.post(ProjectileMoved.class, projBuilder(1594));
		}
	};

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (swapMage)
		{
			clickPrayer(Prayer.PROTECT_FROM_MAGIC);
			swapMage = false;
		}
		else if (swapRange)
		{
			clickPrayer(Prayer.PROTECT_FROM_MISSILES);
			swapRange = false;
		}
	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved event)
	{
		int id = event.getProjectile().getId();

		switch (id)
		{
			case 1594:
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
				{
					swapMage = true;
				}
				break;
			case 1593:
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES))
				{
					swapRange = true;
				}
				break;
		}
	}

	private void clickPrayer(Prayer prayer)
	{
		if (client.isPrayerActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) < 1)
		{
			return;
		}

		final Widget widget = client.getWidget(prayer.getWidgetInfo());

		if (widget == null)
		{
			log.error("Verzik: Unable to find prayer widget.");
			return;
		}

		final Rectangle bounds = widget.getBounds();

		executor.submit(() ->
		{
			if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.PRAYER.getId())
			{
				robot.keyPress(utils.getTabHotkey(Tab.PRAYER));
				try
				{
					Thread.sleep(20);
				}
				catch (InterruptedException e)
				{
					return;
				}
			}

			utils.click(bounds);

			try
			{
				Thread.sleep(getMillis());
			}
			catch (InterruptedException e)
			{
				return;
			}

			if (client.isPrayerActive(prayer))
			{
				robot.keyPress(utils.getTabHotkey(Tab.INVENTORY));
			}

			try
			{
				Thread.sleep(getMillis());
			}
			catch (InterruptedException ignored)
			{
			}
		});
	}

	public int getMillis()
	{
		return (int) (Math.random() * config.randLow() + config.randHigh());
	}

	@NotNull
	private ProjectileMoved projBuilder(int id)
	{
		ProjectileMoved moved = new ProjectileMoved();
		moved.setPosition(null);
		moved.setProjectile(new TestProjectile(id));
		moved.setZ(0);
		return moved;
	}
}
