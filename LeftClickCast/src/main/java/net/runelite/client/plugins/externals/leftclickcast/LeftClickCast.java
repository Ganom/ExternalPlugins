/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.leftclickcast;

import com.google.inject.Provides;
import com.openosrs.client.util.WeaponMap;
import com.openosrs.client.util.WeaponStyle;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import static net.runelite.api.MenuAction.WIDGET_TARGET_ON_NPC;
import static net.runelite.api.MenuAction.WIDGET_TARGET_ON_PLAYER;
import net.runelite.api.NPC;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ChatIconManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.PvPUtil;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "Left Click Cast",
	description = "Casting made even easier.",
	tags = "ganom"
)
@Slf4j
@SuppressWarnings("unused")
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

	@Inject
	private ChatIconManager friendsManager;

	@Inject
	private ClientThread clientThread;

	private final Set<Integer> whitelist = new HashSet<>();

	private boolean isMage;
	private boolean disabled = false;
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

	private final HotkeyListener disable = new HotkeyListener(() -> config.disable())
	{
		@Override
		public void hotkeyPressed()
		{
			disabled = !disabled;
			clientThread.invoke(() ->
				client.addChatMessage(
					ChatMessageType.BROADCAST,
					"",
					"LeftClickCast has been " + (disabled ? "disabled." : "enabled."),
					""
				)
			);
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
		if (client.getGameState() == GameState.LOGGED_IN)
		{
			keyManager.registerKeyListener(spellOneSwap);
			keyManager.registerKeyListener(spellTwoSwap);
			keyManager.registerKeyListener(spellThreeSwap);
			keyManager.registerKeyListener(spellFourSwap);
			keyManager.registerKeyListener(spellFiveSwap);
			keyManager.registerKeyListener(spellSixSwap);
			keyManager.registerKeyListener(disable);
		}
		updateConfig();
	}

	@Override
	public void shutDown()
	{
		keyManager.unregisterKeyListener(spellOneSwap);
		keyManager.unregisterKeyListener(spellTwoSwap);
		keyManager.unregisterKeyListener(spellThreeSwap);
		keyManager.unregisterKeyListener(spellFourSwap);
		keyManager.unregisterKeyListener(spellFiveSwap);
		keyManager.unregisterKeyListener(spellSixSwap);
		keyManager.unregisterKeyListener(disable);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			keyManager.unregisterKeyListener(spellOneSwap);
			keyManager.unregisterKeyListener(spellTwoSwap);
			keyManager.unregisterKeyListener(spellThreeSwap);
			keyManager.unregisterKeyListener(spellFourSwap);
			keyManager.unregisterKeyListener(spellFiveSwap);
			keyManager.unregisterKeyListener(spellSixSwap);
			keyManager.unregisterKeyListener(disable);
			return;
		}
		keyManager.registerKeyListener(spellOneSwap);
		keyManager.registerKeyListener(spellTwoSwap);
		keyManager.registerKeyListener(spellThreeSwap);
		keyManager.registerKeyListener(spellFourSwap);
		keyManager.registerKeyListener(spellFiveSwap);
		keyManager.registerKeyListener(spellSixSwap);
		keyManager.registerKeyListener(disable);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		updateConfig();
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (disabled)
		{
			return;
		}

		if (client.isMenuOpen())
		{
			return;
		}

		if (event.isForceLeftClick())
		{
			return;
		}

		if (event.getType() == MenuAction.PLAYER_SECOND_OPTION.getId() && isMage)
		{
			final String name = Text.standardize(event.getTarget(), true);

			if (!config.disableFriendlyRegionChecks() && (client.getVarbitValue(5314) == 0 && (client.isFriended(name, false))))
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

			setSelectSpell(currentSpell.getSpell());
			client.createMenuEntry(client.getMenuOptionCount())
				.setOption("(P) Left Click " + client.getSelectedSpellName() + " -> ")
				.setTarget(event.getTarget())
				.setType(WIDGET_TARGET_ON_PLAYER)
				.setIdentifier(event.getIdentifier())
				.setParam0(0)
				.setParam1(0)
				.setForceLeftClick(true);
		}
		else if (event.getType() == MenuAction.NPC_SECOND_OPTION.getId() && isMage)
		{
			try
			{
				NPC npc = validateNpc(event.getIdentifier());

				if (npc == null)
				{
					return;
				}

				if (config.disableStaffChecks() && !whitelist.contains(npc.getId()))
				{
					return;
				}

				setSelectSpell(currentSpell.getSpell());
				client.createMenuEntry(client.getMenuOptionCount())
					.setOption("(N) Left Click " + client.getSelectedSpellName() + " -> ")
					.setTarget(event.getTarget())
					.setType(WIDGET_TARGET_ON_NPC)
					.setIdentifier(event.getIdentifier())
					.setParam0(0)
					.setParam1(0)
					.setForceLeftClick(true);
			}
			catch (IndexOutOfBoundsException ignored)
			{
			}
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		final ItemContainer ic = event.getItemContainer();

		if (client.getItemContainer(InventoryID.EQUIPMENT) != ic)
		{
			return;
		}

		isMage = false;

		for (Item item : ic.getItems())
		{
			if (WeaponMap.StyleMap.get(item.getId()) == WeaponStyle.MAGIC)
			{
				isMage = true;
				break;
			}
		}

		if (config.disableStaffChecks())
		{
			isMage = true;
		}
	}

	private void updateConfig()
	{
		whitelist.clear();
		if (config.disableStaffChecks())
		{
			List<String> string = Text.fromCSV(config.whitelist());
			for (String s : string)
			{
				try
				{
					whitelist.add(Integer.parseInt(s));
				}
				catch (NumberFormatException ignored)
				{
				}
			}
		}
	}

	private void setSelectSpell(WidgetInfo info)
	{
		Widget widget = client.getWidget(info);
		if (widget == null)
		{
			log.info("Unable to locate spell widget.");
			return;
		}
		client.setSelectedSpellName("<col=00ff00>" + widget.getName() + "</col>");
		client.setSelectedSpellWidget(widget.getId());
		client.setSelectedSpellChildIndex(-1);
	}

	/**
	 * This method is not ideal, as its going to create a ton of junk
	 * but its the most reliable method i've found so far for validating
	 * NPCs on menu events. Another solution would be to use string
	 * comparison, however most users are used to the id concept
	 * so this was the path of least resistance. I'm open to
	 * suggestions however if anyone wants to offer them.
	 * -Ganom
	 *
	 * @param index Menu event index.
	 * @return {@link NPC} object for comparison.
	 */
	@Nullable
	private NPC validateNpc(int index)
	{
		NPC npc = null;

		for (NPC clientNpc : client.getNpcs())
		{
			if (index == clientNpc.getIndex())
			{
				npc = clientNpc;
				break;
			}
		}

		return npc;
	}
}
