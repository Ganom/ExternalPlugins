/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.olmswapper;

import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.util.Text;
import net.runelite.api.vars.InterfaceTab;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.utils.ExtUtils;
import net.runelite.client.plugins.externals.utils.Tab;
import org.jetbrains.annotations.NotNull;
import org.pf4j.Extension;


@Extension
@PluginDescriptor(
	name = "Olm Pray Swapper",
	description = "Automatically swaps prayers for CoX",
	tags = {"prayer", "olm", "bot", "swap"},
	type = PluginType.UTILITY
)
@Slf4j
@SuppressWarnings("unused")
@PluginDependency(ExtUtils.class)
public class OlmSwapper extends Plugin
{
	private static final String MAGE = "the great olm fires a sphere of magical power your way";
	private static final String RANGE = "the great olm fires a sphere of accuracy and dexterity your way";
	private static final String MELEE = "the great olm fires a sphere of aggression your way";

	@Inject
	private Client client;

	@Inject
	private OlmSwapperConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private ExtUtils utils;

	private ExecutorService executor;
	private boolean swapMage;
	private boolean swapRange;
	private boolean swapMelee;
	private Robot robot;

	@Provides
	OlmSwapperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OlmSwapperConfig.class);
	}

	@Override
	protected void startUp() throws AWTException
	{
		executor = Executors.newSingleThreadExecutor();
		robot = new Robot();
	}

	@Override
	protected void shutDown()
	{
		executor.shutdown();
		robot = null;
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted event)
	{
		if (event.getCommand().equalsIgnoreCase("olm"))
		{
			switch (Text.standardize(event.getArguments()[0]))
			{
				case "mage":
					eventBus.post(ProjectileMoved.class, projBuilder(1339));
					break;
				case "range":
					eventBus.post(ProjectileMoved.class, projBuilder(1340));
					break;
			}
		}
	}

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
		else if (swapMelee)
		{
			clickPrayer(Prayer.PROTECT_FROM_MELEE);
			swapMelee = false;
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		String msg = Text.standardize(event.getMessage());

		if (msg.startsWith(MAGE))
		{
			swapMage = true;
		}
		else if (msg.startsWith(RANGE))
		{
			swapRange = true;
		}
		else if (msg.startsWith(MELEE))
		{
			swapMelee = true;
		}
	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved event)
	{
		if (!config.swapAutos())
		{
			return;
		}

		int id = event.getProjectile().getId();

		switch (id)
		{
			case 1339:
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
				{
					swapMage = true;
				}
				break;
			case 1340:
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
			log.error("Olm: Unable to find prayer widget.");
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
