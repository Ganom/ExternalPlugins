/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.leftclickcast;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuOpcode;
import net.runelite.api.NPC;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.PvPUtil;

@PluginDescriptor(
	name = "Left Click Cast",
	description = "Casting made even easier.",
	type = PluginType.EXTERNAL
)
@Slf4j
public class LeftClickCast extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private EventBus eventBus;
	@Inject
	private LeftClickConfig config;
	@Inject
	private KeyManager keyManager;

	private boolean maging;
	private Spells currentSpell = Spells.ICE_BARRAGE;

	private final HotkeyListener spellOneSwap = new HotkeyListener(() -> config.spellOneSwap())
	{
		@Override
		public void hotkeyPressed()
		{
			currentSpell = config.spellOne();
		}
	};

	private final HotkeyListener spellTwoSwap = new HotkeyListener(() -> config.spellTwoSwap())
	{
		@Override
		public void hotkeyPressed()
		{
			currentSpell = config.spellTwo();
		}
	};

	private final HotkeyListener spellThreeSwap = new HotkeyListener(() -> config.spellThreeSwap())
	{
		@Override
		public void hotkeyPressed()
		{
			currentSpell = config.spellThree();
		}
	};

	private final HotkeyListener spellFourSwap = new HotkeyListener(() -> config.spellFourSwap())
	{
		@Override
		public void hotkeyPressed()
		{
			currentSpell = config.spellFour();
		}
	};

	private final HotkeyListener spellFiveSwap = new HotkeyListener(() -> config.spellFiveSwap())
	{
		@Override
		public void hotkeyPressed()
		{
			currentSpell = config.spellFive();
		}
	};

	private final HotkeyListener spellSixSwap = new HotkeyListener(() -> config.spellSixSwap())
	{
		@Override
		public void hotkeyPressed()
		{
			currentSpell = config.spellSix();
		}
	};

	@Provides
	LeftClickConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LeftClickConfig.class);
	}

	@Override
	public void startUp()
	{
		addSubscriptions();
		if (client.getGameState() == GameState.LOGGED_IN)
		{
			keyManager.registerKeyListener(spellOneSwap);
			keyManager.registerKeyListener(spellTwoSwap);
			keyManager.registerKeyListener(spellThreeSwap);
			keyManager.registerKeyListener(spellFourSwap);
			keyManager.registerKeyListener(spellFiveSwap);
			keyManager.registerKeyListener(spellSixSwap);
		}
	}

	@Override
	public void shutDown()
	{
		eventBus.unregister(this);
		keyManager.unregisterKeyListener(spellOneSwap);
		keyManager.unregisterKeyListener(spellTwoSwap);
		keyManager.unregisterKeyListener(spellThreeSwap);
		keyManager.unregisterKeyListener(spellFourSwap);
		keyManager.unregisterKeyListener(spellFiveSwap);
		keyManager.unregisterKeyListener(spellSixSwap);
	}

	private void addSubscriptions()
	{
		eventBus.subscribe(MenuOptionClicked.class, this, this::onMenuOptionClicked);
		eventBus.subscribe(ItemContainerChanged.class, this, this::onItemContainerChanged);
		eventBus.subscribe(MenuEntryAdded.class, this, this::onMenuEntryAdded);
		eventBus.subscribe(GameStateChanged.class, this, this::onGameStateChanged);
	}

	private void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			keyManager.unregisterKeyListener(spellOneSwap);
			keyManager.unregisterKeyListener(spellTwoSwap);
			keyManager.unregisterKeyListener(spellThreeSwap);
			keyManager.unregisterKeyListener(spellFourSwap);
			keyManager.unregisterKeyListener(spellFiveSwap);
			keyManager.unregisterKeyListener(spellSixSwap);
			return;
		}
		keyManager.registerKeyListener(spellOneSwap);
		keyManager.registerKeyListener(spellTwoSwap);
		keyManager.registerKeyListener(spellThreeSwap);
		keyManager.registerKeyListener(spellFourSwap);
		keyManager.registerKeyListener(spellFiveSwap);
		keyManager.registerKeyListener(spellSixSwap);
	}

	private void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (event.getOpcode() == MenuOpcode.PLAYER_SECOND_OPTION.getId() && maging)
		{
			final String name = Text.standardize(event.getTarget(), true);

			if (!config.disableFriendlyRegionChecks() && (client.getVar(Varbits.LMS_IN_GAME) == 0 && (client.isFriended(name, false) ||
				client.isClanMember(name))))
			{
				return;
			}

			if (!config.disableFriendlyRegionChecks())
			{
				try
				{
					boolean b = (!PvPUtil.isAttackable(client, client.getCachedPlayers()[event.getIdentifier()]));
				}
				catch (IndexOutOfBoundsException ex)
				{
					return;
				}
			}

			event.setModified();
			setSelectSpell(currentSpell.getSpell());
			event.setOption("(P) Left Click " + client.getSelectedSpellName() + " -> ");
		}
		else if (event.getOpcode() == MenuOpcode.NPC_SECOND_OPTION.getId() && maging)
		{
			try
			{
				NPC npc = client.getCachedNPCs()[event.getParam0()];
			}
			catch (IndexOutOfBoundsException ex)
			{
				return;
			}
			event.setModified();
			setSelectSpell(currentSpell.getSpell());
			event.setOption("(N) Left Click " + client.getSelectedSpellName() + " -> ");
		}
	}

	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getOption().contains("(P)"))
		{
			event.setOpcode(15);
			event.setParam0(0);
			event.setParam1(0);
		}
		else if (event.getOption().contains("(N)"))
		{
			event.setOpcode(8);
			event.setParam0(0);
			event.setParam1(0);
		}
	}

	private void onItemContainerChanged(ItemContainerChanged event)
	{
		final ItemContainer ic = event.getItemContainer();

		if (client.getItemContainer(InventoryID.EQUIPMENT) != ic)
		{
			return;
		}

		maging = false;

		for (Item item : ic.getItems())
		{
			final String name = client.getItemDefinition(item.getId()).getName().toLowerCase();
			if (name.contains("staff") || name.contains("wand"))
			{
				maging = true;
				break;
			}
		}
	}

	private void setSelectSpell(WidgetInfo info)
	{
		final Widget widget = client.getWidget(info);
		client.setSelectedSpellName("<col=00ff00>" + widget.getName() + "</col>");
		client.setSelectedSpellWidget(widget.getId());
		client.setSelectedSpellChildIndex(-1);
	}
}