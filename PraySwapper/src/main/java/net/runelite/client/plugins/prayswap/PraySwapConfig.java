/*
 * Copyright (c) 2019, gazivodag <https://github.com/gazivodag>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.runelite.client.plugins.prayswap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Prayer;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigTitleSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Title;

@ConfigGroup("PraySwap")
public interface PraySwapConfig extends Config
{
	@ConfigTitleSection(
		position = 0,
		keyName = "mainConfig",
		name = "Main Config",
		description = ""
	)
	default Title mainConfig()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "protectItem",
		name = "Always protect item",
		description = "Always be protecting item.",
		position = 1,
		titleSection = "mainConfig"
	)
	default boolean protectItem()
	{
		return false;
	}

	@ConfigItem(
		keyName = "lowLevel",
		name = "Enable Low Level Prayers",
		description = "",
		position = 2,
		titleSection = "mainConfig"
	)
	default boolean lowLevel()
	{
		return false;
	}

	@ConfigItem(
		keyName = "backToInventory",
		name = "Swap back to Inventory",
		description = "After finishing a sequence, it will swap back to inventory if enabled.",
		position = 3,
		titleSection = "mainConfig"
	)
	default boolean backToInventory()
	{
		return true;
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
		keyName = "hotkeyMage",
		name = "Protect Mage",
		description = "Mage Pray Swap",
		position = 5,
		titleSection = "hotkeys"
	)
	default Keybind hotkeyMage()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyRange",
		name = "Protect Missiles",
		description = "Range Pray Swap",
		position = 6,
		titleSection = "hotkeys"
	)
	default Keybind hotkeyRange()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyMelee",
		name = "Protect Melee",
		description = "Melee Pray Swap",
		position = 7,
		titleSection = "hotkeys"
	)
	default Keybind hotkeyMelee()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyAugury",
		name = "Augury",
		description = "Low level -> Mystic Might",
		position = 8,
		titleSection = "hotkeys"
	)
	default Keybind hotkeyAugury()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyRigour",
		name = "Rigour",
		description = "Low level -> Eagle Eye",
		position = 9,
		titleSection = "hotkeys"
	)
	default Keybind hotkeyRigour()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyPiety",
		name = "Piety",
		description = "Low level -> Steel Skin, Ultimate Strength, Incredible Reflexs",
		position = 10,
		titleSection = "hotkeys"
	)
	default Keybind hotkeyPiety()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeySmite",
		name = "Smite",
		description = "Smite Pray Swap",
		position = 11,
		titleSection = "hotkeys"
	)
	default Keybind hotkeySmite()
	{
		return Keybind.NOT_SET;
	}

	@ConfigTitleSection(
		position = 12,
		keyName = "combos",
		name = "Combos",
		description = ""
	)
	default Title combos()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "comboOne",
		name = "Combo One",
		description = "Press this key to active combo one.",
		position = 13,
		titleSection = "combos"
	)
	default Keybind comboOne()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "comboOnePrayerOne",
		name = "Prayer 1",
		description = "Prayer 1",
		position = 14,
		titleSection = "combos"
	)
	default Prayers comboOnePrayerOne()
	{
		return Prayers.PROTECT_MAGE;
	}

	@ConfigItem(
		keyName = "comboOnePrayerTwo",
		name = "Prayer 2",
		description = "Prayer 2",
		position = 15,
		titleSection = "combos"
	)
	default Prayers comboOnePrayerTwo()
	{
		return Prayers.RIGOUR;
	}

	@ConfigItem(
		keyName = "comboTwo",
		name = "Combo Two",
		description = "Press this key to active combo one.",
		position = 16,
		titleSection = "combos"
	)
	default Keybind comboTwo()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "comboOnePrayerOne",
		name = "Prayer 1",
		description = "Prayer 1",
		position = 17,
		titleSection = "combos"
	)
	default Prayers comboTwoPrayerOne()
	{
		return Prayers.PROTECT_RANGE;
	}

	@ConfigItem(
		keyName = "comboTwoPrayerTwo",
		name = "Prayer 2",
		description = "Prayer 2",
		position = 18,
		titleSection = "combos"
	)
	default Prayers comboTwoPrayerTwo()
	{
		return Prayers.PIETY;
	}

	@ConfigItem(
		keyName = "comboThree",
		name = "Combo Three",
		description = "Press this key to active combo one.",
		position = 19,
		titleSection = "combos"
	)
	default Keybind comboThree()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "comboThreePrayerOne",
		name = "Prayer 1",
		description = "Prayer 1",
		position = 20,
		titleSection = "combos"
	)
	default Prayers comboThreePrayerOne()
	{
		return Prayers.PROTECT_MELEE;
	}

	@ConfigItem(
		keyName = "comboThreePrayerTwo",
		name = "Prayer 2",
		description = "Prayer 2",
		position = 21,
		titleSection = "combos"
	)
	default Prayers comboThreePrayerTwo()
	{
		return Prayers.AUGURY;
	}

	@ConfigTitleSection(
		position = 22,
		keyName = "botConfig",
		name = "Bot Config",
		description = ""
	)
	default Title botConfig()
	{
		return new Title();
	}

	@ConfigItem(
		position = 38,
		keyName = "actionType",
		name = "Action Type",
		description = "Flexo is smooth mouse, MouseEvents is ghost mouse.",
		titleSection = "botConfig"
	)
	default ActionType actionType()
	{
		return ActionType.FLEXO;
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum Delay",
		description = "For MouseEvents",
		position = 39,
		titleSection = "botConfig"
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "randLower",
		name = "Maximum Delay",
		description = "For MouseEvents",
		position = 40,
		titleSection = "botConfig"
	)
	default int randHigh()
	{
		return 80;
	}

	@Getter
	@AllArgsConstructor
	public enum ActionType
	{
		FLEXO("Flexo"),
		MOUSEEVENTS("MouseEvents");

		private String name;

		@Override
		public String toString()
		{
			return getName();
		}
	}

	@Getter
	@AllArgsConstructor
	public enum Prayers
	{
		PROTECT_MELEE("Protect Melee", Prayer.PROTECT_FROM_MELEE),
		PROTECT_RANGE("Protect Range", Prayer.PROTECT_FROM_MISSILES),
		PROTECT_MAGE("Protect Mage", Prayer.PROTECT_FROM_MAGIC),
		AUGURY("Augury", Prayer.AUGURY),
		RIGOUR("Rigour", Prayer.RIGOUR),
		PIETY("Piety", Prayer.PIETY),
		MYSTIC_MIGHT("Mystic Might", Prayer.MYSTIC_MIGHT),
		EAGLE_EYE("Eagle Eye", Prayer.EAGLE_EYE),
		CHIVALRY("Chivalry", Prayer.CHIVALRY);

		private String name;
		private Prayer prayer;

		@Override
		public String toString()
		{
			return getName();
		}
	}
}
