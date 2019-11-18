/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.leftclickcast;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigTitleSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Title;

@ConfigGroup(value = "leftClickPK")
public interface LeftClickConfig extends Config
{
	@ConfigTitleSection(
		position = 0,
		keyName = "preset",
		name = "Preset",
		description = ""
	)
	default Title preset()
	{
		return new Title();
	}

	@ConfigItem(
		position = 2,
		keyName = "disableFriendlyRegionChecks",
		name = "Disable Friend/Clan/Lvl Checks",
		description = "This will make it so you can cast on friends/clan members.",
		titleSection = "preset"
	)
	default boolean disableFriendlyRegionChecks()
	{
		return false;
	}

	@ConfigTitleSection(
		position = 4,
		keyName = "hotkeys",
		name = "Hotkeys",
		description = ""
	)
	default Title hotkeys()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "spellOneSwap",
		name = "Swap to Spell #1",
		description = "",
		position = 5,
		titleSection = "hotkeys"
	)
	default Keybind spellOneSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "spellTwoSwap",
		name = "Swap to Spell #2",
		description = "",
		position = 6,
		titleSection = "hotkeys"
	)
	default Keybind spellTwoSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "spellThreeSwap",
		name = "Swap to Spell #3",
		description = "",
		position = 7,
		titleSection = "hotkeys"
	)
	default Keybind spellThreeSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "spellFourSwap",
		name = "Swap to Spell #4",
		description = "",
		position = 5,
		titleSection = "hotkeys"
	)
	default Keybind spellFourSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "spell5Swap",
		name = "Swap to Spell #5",
		description = "",
		position = 6,
		titleSection = "hotkeys"
	)
	default Keybind spellFiveSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "spellSixSwap",
		name = "Swap to Spell #6",
		description = "",
		position = 7,
		titleSection = "hotkeys"
	)
	default Keybind spellSixSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigTitleSection(
		position = 8,
		keyName = "spells",
		name = "Spells",
		description = ""
	)
	default Title spells()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "spellOne",
		name = "Spell #1",
		description = "",
		position = 9,
		titleSection = "spells"
	)
	default Spells spellOne()
	{
		return Spells.ICE_BARRAGE;
	}

	@ConfigItem(
		keyName = "spellTwo",
		name = "Spell #2",
		description = "",
		position = 10,
		titleSection = "spells"
	)
	default Spells spellTwo()
	{
		return Spells.ICE_BARRAGE;
	}

	@ConfigItem(
		keyName = "spellThree",
		name = "Spell #3",
		description = "",
		position = 11,
		titleSection = "spells"
	)
	default Spells spellThree()
	{
		return Spells.ICE_BARRAGE;
	}

	@ConfigItem(
		keyName = "spellFour",
		name = "Spell #4",
		description = "",
		position = 9,
		titleSection = "spells"
	)
	default Spells spellFour()
	{
		return Spells.ICE_BARRAGE;
	}

	@ConfigItem(
		keyName = "spellFive",
		name = "Spell #5",
		description = "",
		position = 10,
		titleSection = "spells"
	)
	default Spells spellFive()
	{
		return Spells.ICE_BARRAGE;
	}

	@ConfigItem(
		keyName = "spellSix",
		name = "Spell #6",
		description = "",
		position = 11,
		titleSection = "spells"
	)
	default Spells spellSix()
	{
		return Spells.ICE_BARRAGE;
	}


}

