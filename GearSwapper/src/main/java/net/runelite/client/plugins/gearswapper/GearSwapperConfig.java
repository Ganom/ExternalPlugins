package net.runelite.client.plugins.gearswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Stub;
import net.runelite.client.plugins.gearswapper.utils.ActionType;

@ConfigGroup("gearswapper")

public interface GearSwapperConfig extends Config
{
	@ConfigItem(
		keyName = "desc",
		name = "<html>If you would like to copy your currently" +
			"<br> equipped gear, type \"::copy\" in chat." +
			"<br>This will copy your gear to your" +
			"<br>clipboard for easy copy paste.</html>",
		description = "",
		position = 0
	)
	default Stub desc()
	{
		return new Stub();
	}

	@ConfigItem(
		keyName = "swaps",
		name = "Swaps",
		description = "",
		position = 0
	)
	default Stub swaps()
	{
		return new Stub();
	}

	@ConfigItem(
		keyName = "gearSwapMelee",
		name = "Gear Swap Melee",
		description = "Enables/Disables Set",
		parent = "swaps",
		position = 1
	)
	default boolean gearSwapMelee()
	{
		return true;
	}

	@ConfigItem(
		keyName = "gearSwapMage",
		name = "Gear Swap Mage",
		description = "Enables/Disables Set",
		parent = "swaps",
		position = 2
	)
	default boolean gearSwapMage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "gearSwapRange",
		name = "Gear Swap Range",
		description = "Enables/Disables Set",
		parent = "swaps",
		position = 3
	)
	default boolean gearSwapRange()
	{
		return true;
	}

	@ConfigItem(
		keyName = "hotkeys",
		name = "Hotkeys",
		description = "",
		position = 4
	)
	default Stub hotkeys()
	{
		return new Stub();
	}

	@ConfigItem(
		keyName = "hotkeyMelee",
		name = "Melee Swap hotkey",
		description = "When you press this key your Melee Set will be equipped",
		parent = "hotkeys",
		position = 5
	)
	default Keybind hotkeyMelee()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyMage",
		name = "Mage Swap hotkey",
		description = "When you press this key your Mage Set will be equipped",
		parent = "hotkeys",
		position = 6
	)
	default Keybind hotkeyMage()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyRange",
		name = "Range Swap hotkey",
		description = "When you press this key your Ranged Set will be equipped",
		parent = "hotkeys",
		position = 7
	)
	default Keybind hotkeyRange()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyUtil",
		name = "Utility hotkey",
		description = "Util",
		parent = "hotkeys",
		position = 8
	)
	default Keybind hotkeyUtil()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "config",
		name = "Config",
		description = "",
		position = 9
	)
	default Stub config()
	{
		return new Stub();
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum MS Delay",
		description = "Dont set this too high.",
		parent = "config",
		position = 10
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "randLower",
		name = "Maximum MS Delay",
		description = "Dont set this too high.",
		parent = "config",
		position = 11
	)
	default int randHigh()
	{
		return 80;
	}

	@ConfigItem(
		keyName = "actionType",
		name = "Action Type",
		parent = "config",
		description = "Flexo is smooth mouse<br> MouseEvents is ghost mouse",
		position = 12
	)
	default ActionType actionType()
	{
		return ActionType.FLEXO;
	}

	@ConfigItem(
		keyName = "mageSet",
		name = "Mage Set",
		description = "Item Ids, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 14
	)
	default String mageSet()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "removeMageSet",
		name = "Mage Set Un-Equip",
		description = "Item Ids, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 15
	)
	default String removeMageSet()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "rangeSet",
		name = "Range Set",
		description = "Item Ids, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 16
	)
	default String rangeSet()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "removeRangeSet",
		name = "Range Set Un-Equip",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 17
	)
	default String removeRangeSet()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "meleeSet",
		name = "Melee Set",
		description = "Item Ids, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 18
	)
	default String meleeSet()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "removeMeleeSet",
		name = "Melee Set Un-Equip",
		description = "Item Ids, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 19
	)
	default String removeMeleeSet()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "util",
		name = "Util",
		description = "util",
		group = "Util",
		position = 17
	)
	default String util()
	{
		return "0";
	}

}
