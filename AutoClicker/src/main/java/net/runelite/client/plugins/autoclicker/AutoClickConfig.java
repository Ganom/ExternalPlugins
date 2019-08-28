/*
 * Copyright (c) 2019, Ganom <https://github.com/Ganom>
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
 *
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
package net.runelite.client.plugins.autoclicker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Range;

@ConfigGroup("autoClickConfig")
public interface AutoClickConfig extends Config
{
	@ConfigItem(
		keyName = "hotkey",
		name = "Click hotkey",
		description = "",
		position = 0
	)
	default Keybind hotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "delayMin",
		name = "Min Delay",
		description = "",
		position = 1
	)
	default int delayMin()
	{
		return 200;
	}

	@ConfigItem(
		keyName = "delayMax",
		name = "Max Delay",
		description = "",
		position = 2
	)
	default int delayMax()
	{
		return 200;
	}

	@ConfigItem(
		keyName = "autoDisable",
		name = "Auto Disable at low HP",
		description = "Automatically disables the clicker when you get to low hp.",
		position = 3
	)
	default boolean autoDisableHp()
	{
		return false;
	}

	@ConfigItem(
		keyName = "autoDisableInv",
		name = "Disable when Inv is full",
		description = "Automatically disables the clicker when your inventory is full..",
		position = 3
	)
	default boolean autoDisableInv()
	{
		return false;
	}

	@Range(
		min = 5,
		max = 98
	)
	@ConfigItem(
		keyName = "hpThreshold",
		name = "Hp Threshold",
		description = "The hp in which the plugin will auto disable.",
		position = 4,
		hidden = true,
		unhide = "autoDisable"
	)
	default int hpThreshold()
	{
		return 200;
	}

	@ConfigItem(
		keyName = "flash",
		name = "Flash on Low HP",
		description = "Your Screen flashes when you get to low hp.",
		position = 5,
		hidden = true,
		unhide = "autoDisable"
	)
	default boolean flash()
	{
		return false;
	}
}
