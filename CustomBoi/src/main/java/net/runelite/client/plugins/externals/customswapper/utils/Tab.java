/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.customswapper.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Varbits;

@Getter
@AllArgsConstructor
public enum Tab
{
	COMBAT(Varbits.COMBAT_TAB_HOTKEY),
	EXP(Varbits.STATS_TAB_HOTKEY),
	QUESTS(Varbits.QUESTS_TAB_HOTKEY),
	INVENTORY(Varbits.INVENTORY_TAB_HOTKEY),
	EQUIPMENT(Varbits.EQUIPMENT_TAB_HOTKEY),
	PRAYER(Varbits.PRAYER_TAB_HOTKEY),
	SPELLBOOK(Varbits.SPELLBOOK_TAB_HOTKEY),
	CLAN(Varbits.CLAN_TAB_HOTKEY),
	FRIENDS(Varbits.FRIENDS_TAB_HOTKEY),
	SETTINGS(Varbits.OPTIONS_TAB_HOTKEY),
	EMOTES(Varbits.EMOTES_TAB_HOTKEY),
	MUSIC(Varbits.MUSIC_TAB_HOTKEY),
	LOGOUT(Varbits.LOGOUT_TAB_HOTKEY),
	ACCOUNT(Varbits.ACCOUNT_MANAGEMENT_TAB_HOTKEY);

	private final Varbits varbit;
}
