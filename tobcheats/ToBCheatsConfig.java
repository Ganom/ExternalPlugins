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

@ConfigGroup("tobcheats")

public interface ToBCheatsConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "maidenSwapper",
		name = "Maiden Swapper",
		description = "Swaps on nylo spawns"
	)
	default boolean maidenSwapper()
	{
		return true;
	}

	@ConfigItem(
		position = 0,
		keyName = "nyloSwapper",
		name = "Nylo Swapper",
		description = "Swaps Pray/Items for you"
	)
	default boolean nyloSwapper()
	{
		return true;
	}

/*	@ConfigItem(
		position = 1,
		keyName = "sote",
		name = "Sote Helper",
		description = "Swaps Prays/Allows Tick Eat"
	)
	default boolean sote()
	{
		return true;
	}*/

/*	@ConfigItem(
		position = 2,
		keyName = "soteTickEat",
		name = "Sote Tick Eat",
		description = "Toggle Tick Eat"
	)
	default Keybind hotkeyUtil()
	{
		return Keybind.NOT_SET;
	}*/

	@ConfigItem(
		position = 3,
		keyName = "Verzik",
		name = "Verzik Helper",
		description = "Swaps Prays on Verzik"
	)
	default boolean Verzik()
	{
		return true;
	}

/*	@ConfigItem(
		position = 4,
		keyName = "food",
		name = "Sote Tick Eat Food",
		description = "Tick Eats for you"
	)
	default String food()
	{
		return "[6685,6687,6689,6691]";
	}*/

	@ConfigItem(
		position = 41,
		keyName = "mage",
		name = "Mage Gearswap",
		description = "Mage Gearswap Item Id's"
	)
	default String mage()
	{
		return "[11663,22323,21795,12002]";
	}

	@ConfigItem(
		position = 42,
		keyName = "range",
		name = "Range Gearswap",
		description = "Range Gearswap Item Id's"
	)
	default String range()
	{
		return "[11664,12926,22109,19547]";
	}

	@ConfigItem(
		position = 43,
		keyName = "melee",
		name = "Melee Gearswap",
		description = "Melee Gearswap Item Id's"
	)
	default String melee()
	{
		return "[11665,12006,6570,19553,12954]";
	}
}
