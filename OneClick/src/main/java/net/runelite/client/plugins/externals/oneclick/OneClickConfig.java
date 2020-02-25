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
		keyName = "Type",
		name = "Type",
		description = "Select which one click method you would like to use.",
		position = 1
	)
	default Types getType()
	{
		return Types.NONE;
	}

	@ConfigItem(
		keyName = "spell",
		name = "Spell Select",
		description = "Choose a spell to One click",
		position = 2
	)
	default Spells getSpells()
	{
		return Spells.NONE;
	}

	@ConfigItem(
		keyName = "isUsingImbue",
		name = "Use Magic Imbue",
		description = "If you are using lava runes, and want to use magic imbue, enable this.",
		position = 3
	)
	default boolean isUsingImbue()
	{
		return false;
	}

	@ConfigItem(
		keyName = "customInvSwap",
		name = "Enable Custom Swaps",
		description = "Dont enable this if you don't know what you're doing.",
		position = 4
	)
	default boolean customInvSwap()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swaps",
		name = "Custom Inventory Swaps",
		description = "Format is as follows: OneClickThis:WithThis" +
			"<br>For example, 6032:13421. This will use saltpetre on compost.",
		position = 5,
		hidden = true,
		unhide = "customInvSwap"
	)
	default String swaps()
	{
		return "0:0";
	}
}
