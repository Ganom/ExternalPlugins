/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
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

package net.runelite.client.plugins.tobcheats;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Stub;
import net.runelite.client.plugins.tobcheats.utils.ActionType;

@ConfigGroup("tobcheats")

public interface ToBCheatsConfig extends Config
{
	@ConfigItem(
		position = 1,
		keyName = "swappers",
		name = "Swappers",
		description = ""
	)
	default Stub swappers()
	{
		return new Stub();
	}

	@ConfigItem(
		position = 2,
		keyName = "maidenSwapper",
		name = "Maiden Swapper",
		description = "Swaps on nylo spawns",
		parent = "swappers"
	)
	default boolean maidenSwapper()
	{
		return true;
	}

	@ConfigItem(
		position = 3,
		keyName = "nyloSwapper",
		name = "Nylo Swapper",
		description = "Swaps Pray/Items for you",
		parent = "swappers"
	)
	default boolean nyloSwapper()
	{
		return true;
	}

	@ConfigItem(
		position = 4,
		keyName = "Verzik",
		name = "Verzik Helper",
		description = "Swaps Prays on Verzik. EXPERIMENTAL",
		parent = "swappers"
	)
	default boolean Verzik()
	{
		return false;
	}

	@ConfigItem(
		position = 5,
		keyName = "configs",
		name = "Configs",
		description = ""
	)
	default Stub configs()
	{
		return new Stub();
	}

	@ConfigItem(
		position = 6,
		keyName = "autoAttack",
		name = "Auto Attack",
		description = "Auto Attack after swaps. EXPERIMENTAL",
		parent = "configs"
	)
	default boolean autoAttack()
	{
		return true;
	}

	@ConfigItem(
		keyName = "backToInventory",
		name = "Swap back to Inventory",
		description = "After finishing a sequence, it will swap back to inventory if enabled.",
		position = 7,
		parent = "configs"
	)
	default boolean backToInventory()
	{
		return true;
	}

	@ConfigItem(
		keyName = "handleSpec",
		name = "Wait for Spec to Swap",
		description = "If enabled the bot will wait for you to spec before swapping on nylo.",
		position = 8,
		parent = "configs",
		hidden = true,
		unhide = "nyloSwapper"
	)
	default boolean handleSpec()
	{
		return false;
	}

	@ConfigItem(
		position = 9,
		keyName = "specThreshold",
		name = "Spec Threshold",
		description = "If you are below this spec percent, you will swap.",
		parent = "configs",
		hidden = true,
		unhide = "handleSpec"
	)
	default int specThreshold()
	{
		return 29;
	}

	@ConfigItem(
		keyName = "testing",
		name = "Test the features",
		description = "Type in chat 1 for mage swap, 2 for range, 3 for melee, 4 for ice barrage.",
		position = 10,
		parent = "configs"
	)
	default boolean testing()
	{
		return true;
	}

	@ConfigItem(
		position = 11,
		keyName = "mage",
		name = "Mage Gearswap",
		description = "Mage Gearswap Item Id's",
		parent = "configs",
		hidden = true,
		unhide = "nyloSwapper"
	)
	default String mage()
	{
		return "11663,22323,21795,12002";
	}

	@ConfigItem(
		position = 12,
		keyName = "range",
		name = "Range Gearswap",
		description = "Range Gearswap Item Id's",
		parent = "configs",
		hidden = true,
		unhide = "nyloSwapper"
	)
	default String range()
	{
		return "11664,12926,22109,19547";
	}

	@ConfigItem(
		position = 13,
		keyName = "melee",
		name = "Melee Gearswap",
		description = "Melee Gearswap Item Id's",
		parent = "configs",
		hidden = true,
		unhide = "nyloSwapper"
	)
	default String melee()
	{
		return "11665,12006,6570,19553,12954";
	}

	@ConfigItem(
		position = 14,
		keyName = "actionType",
		name = "Action Type",
		description = "Flexo is smooth mouse, MouseEvents is ghost mouse",
		parent = "configs"
	)
	default ActionType actionType()
	{
		return ActionType.FLEXO;
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum Delay",
		description = "For MouseEvents only.",
		position = 15,
		parent = "configs"
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "randLower",
		name = "Maximum Delay",
		description = "For MouseEvents only.",
		position = 16,
		parent = "configs"
	)
	default int randHigh()
	{
		return 80;
	}
}
