/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.leftclickcast;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

@ConfigGroup(value = "leftClickPK")
public interface LeftClickConfig extends Config
{
	@ConfigSection(
			position = 0,
			keyName = "preset",
			name = "Preset",
			description = ""
	)
	String preset = "Preset";

	@ConfigItem(
			position = 1,
			keyName = "disableFriendlyRegionChecks",
			name = "Disable Friend/Clan/Lvl Checks",
			description = "This will make it so you can cast on friends/clan members.",
			section = preset
	)
	default boolean disableFriendlyRegionChecks()
	{
		return false;
	}

	@ConfigItem(
			position = 2,
			keyName = "disableStaffChecks",
			name = "Disable Staff/Wand Check",
			description = "This will make it so your attack will always change to cast.",
			section = preset
	)
	default boolean disableStaffChecks()
	{
		return false;
	}

	@ConfigItem(
			position = 3,
			keyName = "whitelist",
			name = "Whitelisted NPCs",
			description = "Seperate with comma.",
			section = preset,
			hidden = true,
			unhide = "disableStaffChecks"
	)
	default String whitelist()
	{
		return "";
	}

	@ConfigSection(
			position = 4,
			keyName = "hotkeys",
			name = "Hotkeys",
			description = ""
	)
	String hotkeys = "Hotkeys";

	@ConfigItem(
			keyName = "spellOneSwap",
			name = "Swap to Spell #1",
			description = "",
			position = 1,
			section = hotkeys
	)
	default Keybind spellOneSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "spellTwoSwap",
			name = "Swap to Spell #2",
			description = "",
			position = 2,
			section = hotkeys
	)
	default Keybind spellTwoSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "spellThreeSwap",
			name = "Swap to Spell #3",
			description = "",
			position = 3,
			section = hotkeys
	)
	default Keybind spellThreeSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "spellFourSwap",
			name = "Swap to Spell #4",
			description = "",
			position = 4,
			section = hotkeys
	)
	default Keybind spellFourSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "spell5Swap",
			name = "Swap to Spell #5",
			description = "",
			position = 5,
			section = hotkeys
	)
	default Keybind spellFiveSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "spellSixSwap",
			name = "Swap to Spell #6",
			description = "",
			position = 6,
			section = hotkeys
	)
	default Keybind spellSixSwap()
	{
		return Keybind.NOT_SET;
	}

	@ConfigSection(
			position = 8,
			keyName = "spells",
			name = "Spells",
			description = ""
	)
	String spells = "Spells";

	@ConfigItem(
			keyName = "spellOne",
			name = "Spell #1",
			description = "",
			position = 1,
			section = spells
	)
	default Spells spellOne()
	{
		return Spells.ICE_BARRAGE;
	}

	@ConfigItem(
			keyName = "spellTwo",
			name = "Spell #2",
			description = "",
			position = 2,
			section = spells
	)
	default Spells spellTwo()
	{
		return Spells.ICE_BARRAGE;
	}

	@ConfigItem(
			keyName = "spellThree",
			name = "Spell #3",
			description = "",
			position = 3,
			section = spells
	)
	default Spells spellThree()
	{
		return Spells.ICE_BARRAGE;
	}

	@ConfigItem(
			keyName = "spellFour",
			name = "Spell #4",
			description = "",
			position = 4,
			section = spells
	)
	default Spells spellFour()
	{
		return Spells.ICE_BARRAGE;
	}

	@ConfigItem(
			keyName = "spellFive",
			name = "Spell #5",
			description = "",
			position = 5,
			section = spells
	)
	default Spells spellFive()
	{
		return Spells.ICE_BARRAGE;
	}

	@ConfigItem(
			keyName = "spellSix",
			name = "Spell #6",
			description = "",
			position = 6,
			section = spells
	)
	default Spells spellSix()
	{
		return Spells.ICE_BARRAGE;
	}
}

