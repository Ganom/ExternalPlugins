/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, TomC <https://github.com/tomcylke>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.oneclick;

import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;
import net.runelite.client.plugins.externals.oneclick.comparables.misc.Blank;
import net.runelite.client.plugins.externals.oneclick.comparables.misc.Custom;
import net.runelite.client.plugins.externals.oneclick.comparables.misc.Healer;
import net.runelite.client.plugins.externals.oneclick.comparables.skilling.Spell;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "One Click",
	description = "OP One Click methods.",
	type = PluginType.UTILITY
)
@Getter
@Slf4j
public class OneClickPlugin extends Plugin
{
	private static final String MAGIC_IMBUE_EXPIRED_MESSAGE = "Your Magic Imbue charge has ended.";
	private static final String MAGIC_IMBUE_MESSAGE = "You are charged to combine runes!";

	@Inject
	private Client client;

	@Inject
	private OneClickConfig config;

	private final Custom custom = new Custom();
	private final Map<Integer, String> targetMap = new HashMap<>();

	private ClickCompare comparable = new Blank();
	private boolean enableImbue;
	private boolean imbue;

	@Setter
	private boolean tick;

	@Provides
	OneClickConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OneClickConfig.class);
	}

	@Override
	protected void startUp()
	{
		custom.setClient(client);
		custom.setPlugin(this);
		updateConfig();
	}

	@Override
	protected void shutDown()
	{

	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN && imbue)
		{
			imbue = false;
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!"oneclick".equals(event.getGroup()))
		{
			return;
		}
		updateConfig();
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		switch (event.getMessage())
		{
			case MAGIC_IMBUE_MESSAGE:
				imbue = true;
				break;
			case MAGIC_IMBUE_EXPIRED_MESSAGE:
				imbue = false;
				break;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		tick = false;

		if (comparable instanceof Healer)
		{
			Widget widget = client.getWidget(WidgetInfo.BA_HEAL_LISTEN_TEXT);

			if (widget != null && widget.getText() != null)
			{
				((Healer) comparable).setRoleText(widget.getText().trim());
			}
			else
			{
				((Healer) comparable).setRoleText("");
			}
		}
	}

	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{
		if (comparable instanceof Spell)
		{
			((Spell) comparable).onMenuOpened(event);
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		targetMap.put(event.getIdentifier(), event.getTarget());

		//todo unsure if this is actually needed now that we insert entries.
		/*switch (type)
		{
			case SEED_SET:
			case BA_HEALER:
				if (event.getOpcode() == MenuOpcode.WALK.getId())
				{
					MenuEntry menuEntry = client.getLeftClickMenuEntry();
					menuEntry.setOpcode(MenuOpcode.WALK.getId() + MENU_ACTION_DEPRIORITIZE_OFFSET);
					client.setLeftClickMenuEntry(menuEntry);
				}
				break;
			default:
				break;
		}*/

		if (config.customInvSwap() && custom.isEntryValid(event))
		{
			custom.modifyEntry(event);
			return;
		}

		if (comparable == null)
		{
			throw new AssertionError("This should not be possible.");
		}

		if (comparable.isEntryValid(event))
		{
			comparable.modifyEntry(event);
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (tick)
		{
			event.consume();
			return;
		}

		if (event.getTarget() == null)
		{
			return;
		}

		if (config.customInvSwap() && custom.isClickValid(event))
		{
			custom.modifyClick(event);
			return;
		}

		if (comparable == null)
		{
			throw new AssertionError("This should not be possible.");
		}

		if (comparable.isClickValid(event))
		{
			comparable.modifyClick(event);
		}
	}

	private void updateConfig()
	{
		enableImbue = config.isUsingImbue();
		custom.updateMap(config.swaps());
		Types type = config.getType();
		if (type == Types.SPELL)
		{
			comparable = config.getSpells().getComparable();
			comparable.setClient(client);
			comparable.setPlugin(this);
			if (comparable instanceof Spell)
			{
				((Spell) comparable).setSpellSelection(config.getSpells());
			}
		}
		else
		{
			comparable = type.getComparable();
			comparable.setClient(client);
			comparable.setPlugin(this);
		}
	}
}
