package net.runelite.client.plugins.gearswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("gearswapper")

public interface GearSwapperConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "actionType",
		name = "Action Type",
		description = "Flexo is smooth mouse, MouseEvents is ghost mouse, MenuAction is no mouse, just invokes. BANNABLE"
	)
	default ActionType actionType()
	{
		return ActionType.FLEXO;
	}

	@ConfigItem(
		keyName = "hotkeyMage",
		name = "Mage Swap hotkey",
		description = "When you press this key your Mage Set will be equipped"
	)
	default Keybind hotkeyMage()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyRange",
		name = "Range Swap hotkey",
		description = "When you press this key your Ranged Set will be equipped"
	)
	default Keybind hotkeyRange()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyMelee",
		name = "Melee Swap hotkey",
		description = "When you press this key your Melee Set will be equipped"
	)
	default Keybind hotkeyMelee()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "hotkeyUtil",
		name = "Utility hotkey",
		description = "Util"
	)
	default Keybind hotkeyUtil()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum MS Delay",
		description = "ID",
		position = 1
	)
	default int randLow()
	{
		return 100;
	}

	@ConfigItem(
		keyName = "randLower",
		name = "Maximum MS Delay",
		description = "ID",
		position = 2
	)
	default int randHigh()
	{
		return 140;
	}


	@ConfigItem(
		keyName = "gearSwapMage",
		name = "Gear Swap for Mage",
		description = "Enables/Disables Set",
		position = 3
	)

	default boolean gearSwapMage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "mainhandMage",
		name = "Mainhand for Mage",
		description = "ID",
		position = 4
	)
	default String mainhandMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "offhandMage",
		name = "Offhand for Mage",
		description = "ID",
		position = 5
	)
	default String offhandMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "helmetMage",
		name = "Helmet for Mage",
		description = "ID",
		position = 6
	)
	default String helmetMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "capeMage",
		name = "Cape for Mage",
		description = "ID",
		position = 7
	)
	default String capeMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "neckMage",
		name = "Neck for Mage",
		description = "ID",
		position = 8
	)
	default String neckMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bodyMage",
		name = "Body for Mage",
		description = "ID",
		position = 9
	)
	default String bodyMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "legsMage",
		name = "Leg for Mage",
		description = "ID",
		position = 10
	)
	default String legsMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "ringMage",
		name = "Ring for Mage",
		description = "ID",
		position = 11
	)
	default String ringMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bootMage",
		name = "Boots for Mage",
		description = "ID",
		position = 12
	)
	default String bootsMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "glovesMage",
		name = "Gloves for Mage",
		description = "ID",
		position = 12
	)
	default String glovesMage()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "gearSwapRange",
		name = "Gear Swap Range",
		description = "Enables/Disables Set",
		position = 13
	)
	default boolean gearSwapRange()
	{
		return true;
	}

	@ConfigItem(
		keyName = "mainhandRange",
		name = "Mainhand for Range",
		description = "ID",
		position = 14
	)
	default String mainhandRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "offhandRange",
		name = "Offhand for Range",
		description = "ID",
		position = 15
	)
	default String offhandRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "helmetRange",
		name = "Helmet for Range",
		description = "ID",
		position = 16
	)
	default String helmetRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "capeRange",
		name = "Cape for Range",
		description = "ID",
		position = 17
	)
	default String capeRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "neckRange",
		name = "Neck for Range",
		description = "ID",
		position = 18
	)
	default String neckRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bodyRange",
		name = "Body for Range",
		description = "ID",
		position = 19
	)
	default String bodyRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "legsRange",
		name = "Leg for Range",
		description = "ID",
		position = 20
	)
	default String legsRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "ringRange",
		name = "Ring for Range",
		description = "ID",
		position = 21
	)
	default String ringRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bootRange",
		name = "Boots for Range",
		description = "ID",
		position = 22
	)
	default String bootsRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "glovesRange",
		name = "Gloves for Range",
		description = "ID",
		position = 22
	)
	default String glovesRange()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "gearSwapMelee",
		name = "Gear Swap Melee",
		description = "Enables/Disables Set",
		position = 23
	)
	default boolean gearSwapMelee()
	{
		return true;
	}

	@ConfigItem(
		keyName = "mainhandMelee",
		name = "Mainhand for Melee",
		description = "ID",
		position = 24
	)
	default String mainhandMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "offhandMelee",
		name = "Offhand for Melee",
		description = "ID",
		position = 25
	)
	default String offhandMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "helmetMelee",
		name = "Helmet for Melee",
		description = "ID",
		position = 26
	)
	default String helmetMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "capeMelee",
		name = "Cape for Melee",
		description = "ID",
		position = 27
	)
	default String capeMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "neckMelee",
		name = "Neck for Melee",
		description = "ID",
		position = 28
	)
	default String neckMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bodyMelee",
		name = "Body for Melee",
		description = "ID",
		position = 29
	)
	default String bodyMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "legsMelee",
		name = "Leg for Melee",
		description = "ID",
		position = 30
	)
	default String legsMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "ringMelee",
		name = "Ring for Melee",
		description = "ID",
		position = 31
	)
	default String ringMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "bootMelee",
		name = "Boots for Melee",
		description = "ID",
		position = 32
	)
	default String bootsMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "glovesMelee",
		name = "Gloves for Melee",
		description = "ID",
		position = 32
	)
	default String glovesMelee()
	{
		return "0";
	}

	@ConfigItem(
		keyName = "util",
		name = "Util",
		description = "util",
		position = 33
	)
	default String util()
	{
		return "0";
	}

}
