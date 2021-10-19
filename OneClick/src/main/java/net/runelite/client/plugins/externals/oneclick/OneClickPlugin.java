/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, TomC <https://github.com/tomcylke>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.oneclick;

import com.google.inject.Provides;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import static net.runelite.api.MenuAction.MENU_ACTION_DEPRIORITIZE_OFFSET;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;
import net.runelite.client.plugins.externals.oneclick.comparables.misc.Custom;
import net.runelite.client.plugins.externals.oneclick.comparables.misc.Healer;
import net.runelite.client.plugins.externals.oneclick.comparables.skilling.Spell;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
		name = "One Click",
		description = "OP One Click methods.",
		tags = "ganom"
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

	private final Map<Integer, String> targetMap = new HashMap<>();

	private boolean enableImbue;
	private boolean imbue;

	static final int BA_CALL_LISTEN = 7;
	public static final int BA_HEALER_GROUP_ID = 488;

	ArrayList<ClickCompare> comparables = new ArrayList<>();

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

		ClickCompare healer = null;

		for (ClickCompare comp : this.comparables)
		{
			if (comp instanceof Healer) healer = comp;
		}

		if (healer != null)
		{
			Widget widget = client.getWidget(BA_HEALER_GROUP_ID, BA_CALL_LISTEN);

			if (widget != null && widget.getText() != null)
			{
				((Healer) healer).setRoleText(widget.getText().trim());
			}
			else
			{
				((Healer) healer).setRoleText("");
			}
		}
	}

	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{
		ClickCompare spell = null;

		for(ClickCompare comp : this.comparables){
			if(comp instanceof Spell) spell = comp;
		}

		if (spell != null)
		{
			((Spell) spell).onMenuOpened(event);
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (!event.isForceLeftClick())
		{
			targetMap.put(event.getIdentifier(), event.getTarget());
		}

		if (config.deprioritizeWalk())
		{
			if(config.swapHealer() || config.swapTithe()){
				if (event.getOpcode() == MenuAction.WALK.getId())
				{
					MenuEntry menuEntry = client.getLeftClickMenuEntry();
					menuEntry.setOpcode(MenuAction.WALK.getId() + MENU_ACTION_DEPRIORITIZE_OFFSET);
					client.setLeftClickMenuEntry(menuEntry);
				}
			}
		}
		ClickCompare comparable = isEntryValid(event);

		if (comparable != null)
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

		if (event.getMenuTarget() == null)
		{
			return;
		}

		if (comparables.isEmpty())
		{
			return;
		}
		ClickCompare comparable = isClickValid(event);

		if (comparable != null)
		{
			comparable.modifyClick(event);
			return;
		}

		MenuEntry old = new MenuEntry(
			event.getMenuOption(), event.getMenuTarget(), event.getId(), event.getMenuAction().getId(), event.getActionParam(), event.getWidgetId(), false
		);
		MenuEntry tmp = old.clone();

		comparable = isEntryValid(tmp);

		if (comparable.isEntryValid(tmp))
		{
			comparable.backupEntryModify(tmp);
			event.setMenuEntry(tmp);
		}else{
			event.setMenuEntry(old);
			return;
		}

		if (comparable.isClickValid(event))
		{
			comparable.modifyClick(event);
		}else{
			event.setMenuEntry(old);
		}
	}

	private void updateConfig()
	{
		this.comparables = new ArrayList<>();

		enableImbue = config.isUsingImbue();

		if(config.swapSpell()){
			ClickCompare comparable = config.getSpells().getComparable();
			comparable.setClient(client);
			comparable.setPlugin(this);
			if (comparable instanceof Spell)
			{
				((Spell) comparable).setSpellSelection(config.getSpells());
			}
			this.comparables.add(comparable);
		}
		if(config.swapCustom()){
			ClickCompare comparable = Types.CUSTOM.getComparable();
			comparable.setClient(client);
			comparable.setPlugin(this);
			((Custom) comparable).updateMap(config.swaps());
			this.comparables.add(comparable);
		}

		if(config.swapTithe()) addType(Types.SEED_SET);
		if(config.swapHealer()) addType(Types.BA_HEALER);
		if(config.swapBirdhouses()) addType(Types.BIRDHOUSES);
		if(config.swapBones()) addType(Types.BONES);
		if(config.swapCompost()) addType(Types.COMPOST);
		if(config.swapDarts()) addType(Types.DARTS);
		if(config.swapEssence()) addType(Types.DARK_ESSENCE);
		if(config.swapFiremaking()) addType(Types.FIREMAKING);
		if(config.swapKarambwans()) addType(Types.KARAMBWANS);
		if(config.swapLavas()) addType(Types.KARAMBWANS);
		if(config.swapSeaweed()) addType(Types.SEAWEED);
		if(config.swapSmokes()) addType(Types.SMOKE_RUNES);
		if(config.swapSteams()) addType(Types.STEAM_RUNES);
		if(config.swapTar()) addType(Types.HERB_TAR);
		if(config.swapTiara()) addType(Types.TIARA);
	}

	private void addType(Types type){
		ClickCompare comparable = type.getComparable();
		comparable.setClient(client);
		comparable.setPlugin(this);
		this.comparables.add(comparable);
	}

	private ClickCompare isClickValid(MenuOptionClicked event){
		for(ClickCompare comparable : this.comparables){
			if(comparable.isClickValid(event)) return comparable;
		}
		return null;
	}


	private ClickCompare isEntryValid(MenuEntry entry){
		for(ClickCompare comparable : this.comparables){
			if(comparable.isEntryValid(entry)) return comparable;
		}
		return null;
	}

	private ClickCompare getValidComparable(MenuOptionClicked event){
		for(ClickCompare comparable : this.comparables){
			if(comparable.isClickValid(event)) return comparable;
		}
		return null;
	}
}
