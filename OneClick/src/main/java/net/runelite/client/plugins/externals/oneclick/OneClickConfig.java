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
			description = "Uses saltpeter on compost for Hosidius reputation.",
			position = 1
	)
	default boolean swapCompost()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapDarts",
			name = "Swap Darts",
			description = "Uses feathers on dart tips.",
			position = 2
	)
	default boolean swapDarts()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapFiremaking",
			name = "Swap Firemaking",
			description = "Uses tinderbox on all types of logs.",
			position = 3
	)
	default boolean swapFiremaking()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapBirdhouses",
			name = "Swap Birdhouses",
			description = "Uses all types of hops seeds on built birdhouse.",
			position = 4
	)
	default boolean swapBirdhouses()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapTar",
			name = "Swap Tar",
			description = "Uses herb on swamp tar.",
			position = 5
	)
	default boolean swapTar()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapLavas",
			name = "Swap Lavas",
			description = "Left click to craft lava runes.",
			position = 6
	)
	default boolean swapLavas()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapSteams",
			name = "Swap Steams",
			description = "Left click to craft steam runes.",
			position = 7
	)
	default boolean swapSteams()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapSmokes",
			name = "Swap Smokes",
			description = "Left click to craft smoke runes.",
			position = 8
	)
	default boolean swapSmokes()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapBones",
			name = "Swap Bones",
			description = "Left click use bones on altar.",
			position = 9
	)
	default boolean swapBones()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapKarambwans",
			name = "Swap Karambwans",
			description = "Left click use karambwan on fire/range.",
			position = 10
	)
	default boolean swapKarambwans()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapSeaweed",
			name = "Swap Seaweed",
			description = "Left click use seaweed on fire.",
			position = 11
	)
	default boolean swapSeaweed()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapEssence",
			name = "Swap Dark Essence",
			description = "Left click chisel dark essence.",
			position = 12
	)
	default boolean swapEssence()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapTithe",
			name = "Swap Tithe Farm",
			description = "Left click use seeds on field.",
			position = 13
	)
	default boolean swapTithe()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapTiara",
			name = "Swap Tiara",
			description = "Left click use Tiara on Altar.",
			position = 14
	)
	default boolean swapTiara()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapSpell",
			name = "Swap Spell",
			description = "Set a spell to do something specific",
			position = 22
	)
	default boolean swapSpell()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapHealer",
			name = "Swap Healer",
			description = "Left click healer food in Barbarian Assault.",
			position = 16
	)
	default boolean swapHealer()
	{
		return false;
	}

	@ConfigItem(
			keyName = "swapCustom",
			name = "Swap Custom",
			description = "Custom Use item on item.",
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
