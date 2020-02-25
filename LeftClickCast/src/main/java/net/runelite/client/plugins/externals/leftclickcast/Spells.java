/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.leftclickcast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.widgets.WidgetInfo;

@Getter
@AllArgsConstructor
public enum Spells
{
	BIND("Bind", WidgetInfo.SPELL_BIND),
	CLAWS_OF_GUTHIX("Claws of Guthix", WidgetInfo.SPELL_CLAWS_OF_GUTHIX),
	CRUMBLE("Crumble Undead", WidgetInfo.SPELL_CRUMBLE_UNDEAD),
	EARTH_BLAST("Earth Blast", WidgetInfo.SPELL_EARTH_BLAST),
	EARTH_BOLT("Earth Bolt", WidgetInfo.SPELL_EARTH_BOLT),
	EARTH_STRIKE("Earth Strike", WidgetInfo.SPELL_EARTH_STRIKE),
	EARTH_SURGE("Earth Surge", WidgetInfo.SPELL_EARTH_SURGE),
	EARTH_WAVE("Earth Wave", WidgetInfo.SPELL_EARTH_WAVE),
	ENTANGLE("Entangle", WidgetInfo.SPELL_ENTANGLE),
	FIRE_BLAST("Fire Blast", WidgetInfo.SPELL_FIRE_BLAST),
	FIRE_BOLT("Fire Bolt", WidgetInfo.SPELL_FIRE_BOLT),
	FIRE_STRIKE("Fire Strike", WidgetInfo.SPELL_FIRE_STRIKE),
	FIRE_SURGE("Fire Surge", WidgetInfo.SPELL_FIRE_SURGE),
	FIRE_WAVE("Fire Wave", WidgetInfo.SPELL_FIRE_WAVE),
	FLAMES_OF_ZAMORAK("Flames of Zamorak", WidgetInfo.SPELL_FLAMES_OF_ZAMORAK),
	IBAN_BLAST("Iban Blast", WidgetInfo.SPELL_IBAN_BLAST),
	MAGIC_DART("Magic Dart", WidgetInfo.SPELL_MAGIC_DART),
	SARADOMIN_STRIKE("Saradomin Strike", WidgetInfo.SPELL_SARADOMIN_STRIKE),
	SNARE("Snare", WidgetInfo.SPELL_SNARE),
	TELE_BLOCK("Tele Block", WidgetInfo.SPELL_TELE_BLOCK),
	WATER_BLAST("Water Blast", WidgetInfo.SPELL_WATER_BLAST),
	WATER_BOLT("Water Bolt", WidgetInfo.SPELL_WATER_BOLT),
	WATER_STRIKE("Water Strike", WidgetInfo.SPELL_WATER_STRIKE),
	WATER_SURGE("Water Surge", WidgetInfo.SPELL_WATER_SURGE),
	WATER_WAVE("Water Wave", WidgetInfo.SPELL_WATER_WAVE),
	WIND_BLAST("Wind Blast", WidgetInfo.SPELL_WIND_BLAST),
	WIND_BOLT("Wind Bolt", WidgetInfo.SPELL_WIND_BOLT),
	WIND_STRIKE("Wind Strike", WidgetInfo.SPELL_WIND_STRIKE),
	WIND_SURGE("Wind Surge", WidgetInfo.SPELL_WIND_SURGE),
	WIND_WAVE("Wind Wave", WidgetInfo.SPELL_WIND_WAVE),

	//Curses
	CHARGE("Charge", WidgetInfo.SPELL_CHARGE),
	CONFUSE("Confuse", WidgetInfo.SPELL_CONFUSE),
	CURSE("Curse", WidgetInfo.SPELL_CURSE),
	ENFEEBLE("Enfeeble", WidgetInfo.SPELL_ENFEEBLE),
	STUN("Stun", WidgetInfo.SPELL_STUN),
	VULNERABILITY("Vulnerability", WidgetInfo.SPELL_VULNERABILITY),
	WEAKEN("Weaken", WidgetInfo.SPELL_WEAKEN),

	//Ancients
	BLOOD_BARRAGE("Blood Barrage", WidgetInfo.SPELL_BLOOD_BARRAGE),
	BLOOD_BLITZ("Blood Blitz", WidgetInfo.SPELL_BLOOD_BLITZ),
	BLOOD_BURST("Blood Burst", WidgetInfo.SPELL_BLOOD_BURST),
	BLOOD_RUSH("Blood Rush", WidgetInfo.SPELL_BLOOD_RUSH),
	ICE_BARRAGE("Ice Barrage", WidgetInfo.SPELL_ICE_BARRAGE),
	ICE_BLITZ("Ice Blitz", WidgetInfo.SPELL_ICE_BLITZ),
	ICE_BURST("Ice Burst", WidgetInfo.SPELL_ICE_BURST),
	ICE_RUSH("Ice Rush", WidgetInfo.SPELL_ICE_RUSH),
	SHADOW_BARRAGE("Shadow Barrage", WidgetInfo.SPELL_SHADOW_BARRAGE),
	SHADOW_BLITZ("Shadow Blitz", WidgetInfo.SPELL_SHADOW_BLITZ),
	SHADOW_BURST("Shadow Burst", WidgetInfo.SPELL_SHADOW_BURST),
	SHADOW_RUSH("Shadow Rush", WidgetInfo.SPELL_SHADOW_RUSH),
	SMOKE_BARRAGE("Smoke Barrage", WidgetInfo.SPELL_SMOKE_BARRAGE),
	SMOKE_BLITZ("Smoke Blitz", WidgetInfo.SPELL_SMOKE_BLITZ),
	SMOKE_BURST("Smoke Burst", WidgetInfo.SPELL_SMOKE_BURST),
	SMOKE_RUSH("Smoke Rush", WidgetInfo.SPELL_SMOKE_RUSH);

	private String name;
	private WidgetInfo spell;

	@Override
	public String toString()
	{
		return getName();
	}
}
