/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, TomC <https://github.com/tomcylke>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.oneclick;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oneclick")
public interface OneClickConfig extends Config
{
	@ConfigItem(
			keyName = "swapCompost",
			name = "Swap Compost",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 1
	)
	default boolean swapCompost()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapDarts",
			name = "Swap Darts",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 2
	)
	default boolean swapDarts()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapFiremaking",
			name = "Swap Firemaking",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 3
	)
	default boolean swapFiremaking()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapBirdhouses",
			name = "Swap Birdhouses",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 4
	)
	default boolean swapBirdhouses()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapTar",
			name = "Swap Tar",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 5
	)
	default boolean swapTar()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapLavas",
			name = "Swap Lavas",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 6
	)
	default boolean swapLavas()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapSteams",
			name = "Swap Steams",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 7
	)
	default boolean swapSteams()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapSmokes",
			name = "Swap Smokes",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 8
	)
	default boolean swapSmokes()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapBones",
			name = "Swap Bones",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 9
	)
	default boolean swapBones()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapKarambwans",
			name = "Swap Karambwans",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 10
	)
	default boolean swapKarambwans()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapSeaweed",
			name = "Swap Seaweed",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 11
	)
	default boolean swapSeaweed()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapEssence",
			name = "Swap Dark Essence",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 12
	)
	default boolean swapEssence()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapTithe",
			name = "Swap Tithe Farm",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 13
	)
	default boolean swapTithe()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapTiara",
			name = "Swap Tiara",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 14
	)
	default boolean swapTiara()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapSpell",
			name = "Swap Spell",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 22
	)
	default boolean swapSpell()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapHealer",
			name = "Swap Healer",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 16
	)
	default boolean swapHealer()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapCustom",
			name = "Swap Custom",
			description = "If you are using lava runes, and want to use magic imbue, enable this.",
			position = 17
	)
	default boolean swapCustom()
	{
		return false;
	}

	@ConfigItem(
		keyName = "isUsingImbue",
		name = "Use Magic Imbue",
		description = "If you are using lava runes, and want to use magic imbue, enable this.",
		position = 18
	)
	default boolean isUsingImbue()
	{
		return false;
	}

	@ConfigItem(
		keyName = "deprioritizeWalk",
		name = "Deprioritize Walk",
		description = "Deprioritizes walking on BA healer & seeds.",
		position = 999
	)
	default boolean deprioritizeWalk()
	{
		return false;
	}

	@ConfigItem(
			keyName = "spell",
			name = "Spell Select",
			description = "Choose a spell to One click",
			position = 23
	)
	default Spells getSpells()
	{
		return Spells.NONE;
	}

	@ConfigItem(
		keyName = "swaps",
		name = "Custom Inventory Swaps",
		description = "Format is as follows: OneClickThis:WithThis" +
			"<br>For example, 6032:13421. This will use saltpetre on compost.",
		position = 24
	)
	default String swaps()
	{
		return "0:0";
	}
}
