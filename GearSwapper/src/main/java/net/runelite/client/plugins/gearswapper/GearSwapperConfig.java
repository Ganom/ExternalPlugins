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
		name = "Gear Swap for Mage",
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
		keyName = "hotkeyMage",
		name = "Mage Swap hotkey",
		description = "When you press this key your Mage Set will be equipped",
		parent = "hotkeys",
		position = 5
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
		position = 6
	)
	default Keybind hotkeyRange()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyMelee",
		name = "Melee Swap hotkey",
		description = "When you press this key your Melee Set will be equipped",
		parent = "hotkeys",
		position = 7
	)
	default Keybind hotkeyMelee()
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
		keyName = "mainhandMage",
		name = "Mainhand for Mage",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 14
	)
	default String mainhandMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "offhandMage",
		name = "Offhand for Mage",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 15
	)
	default String offhandMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "helmetMage",
		name = "Helmet for Mage",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 16
	)
	default String helmetMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "capeMage",
		name = "Cape for Mage",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 17
	)
	default String capeMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "neckMage",
		name = "Neck for Mage",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 18
	)
	default String neckMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bodyMage",
		name = "Body for Mage",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 19
	)
	default String bodyMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "legsMage",
		name = "Leg for Mage",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 20
	)
	default String legsMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "ringMage",
		name = "Ring for Mage",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 21
	)
	default String ringMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bootMage",
		name = "Boots for Mage",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 22
	)
	default String bootsMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "glovesMage",
		name = "Gloves for Mage",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Mage",
		hidden = true,
		unhide = "gearSwapMage",
		position = 23
	)
	default String glovesMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "mainhandRange",
		name = "Mainhand for Range",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 24
	)
	default String mainhandRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "offhandRange",
		name = "Offhand for Range",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 25
	)
	default String offhandRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "helmetRange",
		name = "Helmet for Range",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 26
	)
	default String helmetRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "capeRange",
		name = "Cape for Range",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 27
	)
	default String capeRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "neckRange",
		name = "Neck for Range",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 28
	)
	default String neckRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bodyRange",
		name = "Body for Range",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 29
	)
	default String bodyRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "legsRange",
		name = "Leg for Range",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 30
	)
	default String legsRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "ringRange",
		name = "Ring for Range",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 31
	)
	default String ringRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bootRange",
		name = "Boots for Range",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 32
	)
	default String bootsRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "glovesRange",
		name = "Gloves for Range",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Range",
		hidden = true,
		unhide = "gearSwapRange",
		position = 33
	)
	default String glovesRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "mainhandMelee",
		name = "Mainhand for Melee",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 34
	)
	default String mainhandMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "offhandMelee",
		name = "Offhand for Melee",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 35
	)
	default String offhandMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "helmetMelee",
		name = "Helmet for Melee",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 36
	)
	default String helmetMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "capeMelee",
		name = "Cape for Melee",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 37
	)
	default String capeMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "neckMelee",
		name = "Neck for Melee",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 38
	)
	default String neckMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bodyMelee",
		name = "Body for Melee",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 39
	)
	default String bodyMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "legsMelee",
		name = "Leg for Melee",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 40
	)
	default String legsMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "ringMelee",
		name = "Ring for Melee",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 41
	)
	default String ringMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bootMelee",
		name = "Boots for Melee",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 42
	)
	default String bootsMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "glovesMelee",
		name = "Gloves for Melee",
		description = "Item Id, separate with commas.<br> DO NOT LEAVE EMPTY",
		group = "Melee",
		hidden = true,
		unhide = "gearSwapMelee",
		position = 43
	)
	default String glovesMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "util",
		name = "Util",
		description = "util",
		group = "Util",
		position = 44
	)
	default String util()
	{
		return "0";
	}

}
