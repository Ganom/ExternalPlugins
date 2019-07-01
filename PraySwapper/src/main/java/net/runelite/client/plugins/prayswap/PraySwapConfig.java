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
import net.runelite.client.config.Keybind;
import net.runelite.client.plugins.prayswap.utils.ActionType;

@ConfigGroup("PraySwap")
public interface PraySwapConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "actionType",
		name = "Action Type",
		description = "Flexo is smooth mouse, MouseEvents is ghost mouse."
	)
	default ActionType actionType()
	{
		return ActionType.FLEXO;
	}

	@ConfigItem(
		keyName = "protectItem",
		name = "Always protect item",
		description = "Always be protecting item.",
		position = 0
	)
	default boolean protectItem()
	{
		return false;
	}

	@ConfigItem(
		keyName = "lowLevel",
		name = "Enable Low Level Prayers",
		description = "",
		position = 0
	)
	default boolean lowLevel()
	{
		return false;
	}

	@ConfigItem(
		keyName = "backToInventory",
		name = "Swap back to Inventory",
		description = "After finishing a sequence, it will swap back to inventory if enabled.",
		position = 5
	)
	default boolean backToInventory()
	{
		return true;
	}

	@ConfigItem(
		keyName = "hotkeyMage",
		name = "Mage Swap hotkey",
		description = "Mage Pray Swap",
		position = 3
	)
	default Keybind hotkeyMage()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyRange",
		name = "Range Swap hotkey",
		description = "Range Pray Swap",
		position = 4
	)
	default Keybind hotkeyRange()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyMelee",
		name = "Melee Swap hotkey",
		description = "Melee Pray Swap",
		position = 5
	)
	default Keybind hotkeyMelee()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyAugury",
		name = "Augury Swap hotkey",
		description = "Augury Pray Swap",
		position = 6
	)
	default Keybind hotkeyAugury()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyRigour",
		name = "Rigour Swap hotkey",
		description = "Rigour Pray Swap",
		position = 7
	)
	default Keybind hotkeyRigour()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyPiety",
		name = "Piety Swap hotkey",
		description = "Piety Pray Swap",
		position = 8
	)
	default Keybind hotkeyPiety()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeySmite",
		name = "Smite Swap hotkey",
		description = "Smite Pray Swap",
		position = 9
	)
	default Keybind hotkeySmite()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "comboOne",
		name = "Combo One",
		description = "Press this key to active combo one.",
		position = 10
	)
	default Keybind comboOne()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "comboOnePrayerOne",
		name = "Prayer 1",
		description = "Prayer 1",
		position = 11
	)
	default Prayers comboOnePrayerOne()
	{
		return Prayers.PROTECT_MAGE;
	}

	@ConfigItem(
		keyName = "comboOnePrayerTwo",
		name = "Prayer 2",
		description = "Prayer 2",
		position = 12
	)
	default Prayers comboOnePrayerTwo()
	{
		return Prayers.RIGOUR;
	}

	@ConfigItem(
		keyName = "comboTwo",
		name = "Combo Two",
		description = "Press this key to active combo one.",
		position = 13
	)
	default Keybind comboTwo()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "comboOnePrayerOne",
		name = "Prayer 1",
		description = "Prayer 1",
		position = 14
	)
	default Prayers comboTwoPrayerOne()
	{
		return Prayers.PROTECT_RANGE;
	}

	@ConfigItem(
		keyName = "comboTwoPrayerTwo",
		name = "Prayer 2",
		description = "Prayer 2",
		position = 15
	)
	default Prayers comboTwoPrayerTwo()
	{
		return Prayers.PIETY;
	}

	@ConfigItem(
		keyName = "comboThree",
		name = "Combo Three",
		description = "Press this key to active combo one.",
		position = 16
	)
	default Keybind comboThree()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "comboThreePrayerOne",
		name = "Prayer 1",
		description = "Prayer 1",
		position = 17
	)
	default Prayers comboThreePrayerOne()
	{
		return Prayers.PROTECT_MELEE;
	}

	@ConfigItem(
		keyName = "comboThreePrayerTwo",
		name = "Prayer 2",
		description = "Prayer 2",
		position = 18
	)
	default Prayers comboThreePrayerTwo()
	{
		return Prayers.AUGURY;
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum Delay",
		description = "For MouseEvents",
		position = 39
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "randLower",
		name = "Maximum Delay",
		description = "For MouseEvents",
		position = 40
	)
	default int randHigh()
	{
		return 80;
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
		PIETY("Piety", Prayer.PIETY);

		private String name;
		private Prayer prayer;

		@Override
		public String toString()
		{
			return getName();
		}
	}
}
