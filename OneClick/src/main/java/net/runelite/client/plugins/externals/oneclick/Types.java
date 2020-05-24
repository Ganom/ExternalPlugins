/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, TomC <https://github.com/tomcylke>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.oneclick;

import lombok.AllArgsConstructor;
import lombok.Getter;
import static net.runelite.api.ItemID.AIR_RUNE;
import static net.runelite.api.ItemID.EARTH_RUNE;
import static net.runelite.api.ItemID.WATER_RUNE;
import net.runelite.client.plugins.externals.oneclick.Comparables.Birdhouses;
import net.runelite.client.plugins.externals.oneclick.Comparables.Blank;
import net.runelite.client.plugins.externals.oneclick.Comparables.Bones;
import net.runelite.client.plugins.externals.oneclick.Comparables.ClickComparable;
import net.runelite.client.plugins.externals.oneclick.Comparables.Compost;
import net.runelite.client.plugins.externals.oneclick.Comparables.DarkEssence;
import net.runelite.client.plugins.externals.oneclick.Comparables.Darts;
import net.runelite.client.plugins.externals.oneclick.Comparables.Firemaking;
import net.runelite.client.plugins.externals.oneclick.Comparables.Healer;
import net.runelite.client.plugins.externals.oneclick.Comparables.Herbtar;
import net.runelite.client.plugins.externals.oneclick.Comparables.Karambwans;
import net.runelite.client.plugins.externals.oneclick.Comparables.Runes;
import net.runelite.client.plugins.externals.oneclick.Comparables.Seeds;
import net.runelite.client.plugins.externals.oneclick.Comparables.Tiara;

@AllArgsConstructor
@Getter
public enum Types
{
	BA_HEALER("BA Healer", new Healer()),
	BIRDHOUSES("Birdhouses", new Birdhouses()),
	BONES("Bones", new Bones()),
	COMPOST("Compost", new Compost()),
	DARK_ESSENCE("Dark Essence", new DarkEssence()),
	DARTS("Darts", new Darts()),
	FIREMAKING("Firemaking", new Firemaking()),
	HERB_TAR("Herb Tar", new Herbtar()),
	KARAMBWANS("Karambwans", new Karambwans()),
	LAVA_RUNES("Lava Runes", new Runes("Earth rune", EARTH_RUNE)),
	SEED_SET("Tithe Farm", new Seeds()),
	SMOKE_RUNES("Smoke Runes", new Runes("Air rune", AIR_RUNE)),
	SPELL("Spell Casting", new Blank()),
	STEAM_RUNES("Steam Runes", new Runes("Water rune", WATER_RUNE)),
	TIARA("Tiara", new Tiara()),
	NONE("None", new Blank());

	private String name;
	private ClickComparable comparable;

	@Override
	public String toString()
	{
		return getName();
	}
}
