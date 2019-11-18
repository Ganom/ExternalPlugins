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

package net.runelite.client.plugins.customswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigTitleSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Title;

@ConfigGroup("customSwapper")
public interface CustomSwapperConfig extends Config
{
	@ConfigTitleSection(
		position = 0,
		keyName = "mainConfig",
		name = "Main Config",
		description = ""
	)
	default Title mainConfig()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "customSwapOne",
		name = "Swap Set: One",
		description = "",
		position = 1,
		titleSection = "mainConfig"
	)
	default String customSwapOne()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapTwo",
		name = "Swap Set: Two",
		description = "",
		position = 2,
		titleSection = "mainConfig"
	)
	default String customSwapTwo()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapThree",
		name = "Swap Set: Three",
		description = "",
		position = 3,
		titleSection = "mainConfig"
	)
	default String customSwapThree()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapFour",
		name = "Swap Set: Four",
		description = "",
		position = 4,
		titleSection = "mainConfig"
	)
	default String customSwapFour()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapFive",
		name = "Swap Set: Five",
		description = "",
		position = 4,
		titleSection = "mainConfig"
	)
	default String customSwapFive()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapSix",
		name = "Swap Set: Six",
		description = "",
		position = 4,
		titleSection = "mainConfig"
	)
	default String customSwapSix()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapSeven",
		name = "Swap Set: Seven",
		description = "",
		position = 4,
		titleSection = "mainConfig"
	)
	default String customSwapSeven()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapEight",
		name = "Swap Set: Eight",
		description = "",
		position = 4,
		titleSection = "mainConfig"
	)
	default String customSwapEight()
	{
		return "";
	}


	@ConfigTitleSection(
		position = 4,
		keyName = "hotkeys",
		name = "Hotkeys",
		description = ""
	)
	default Title hotkeys()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "customOne",
		name = "Execute One",
		description = "",
		position = 5,
		titleSection = "hotkeys"
	)
	default Keybind customOne()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customTwo",
		name = "Execute Two",
		description = "",
		position = 6,
		titleSection = "hotkeys"
	)
	default Keybind customTwo()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customThree",
		name = "Execute Three",
		description = "",
		position = 7,
		titleSection = "hotkeys"
	)
	default Keybind customThree()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customFour",
		name = "Execute Four",
		description = "",
		position = 8,
		titleSection = "hotkeys"
	)
	default Keybind customFour()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customFive",
		name = "Execute Five",
		description = "",
		position = 8,
		titleSection = "hotkeys"
	)
	default Keybind customFive()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customSix",
		name = "Execute Six",
		description = "",
		position = 8,
		titleSection = "hotkeys"
	)
	default Keybind customSix()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customSeven",
		name = "Execute Seven",
		description = "",
		position = 8,
		titleSection = "hotkeys"
	)
	default Keybind customSeven()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customEight",
		name = "Execute Eight",
		description = "",
		position = 8,
		titleSection = "hotkeys"
	)
	default Keybind customEight()
	{
		return Keybind.NOT_SET;
	}

	@ConfigTitleSection(
		keyName = "config",
		name = "Config",
		description = "",
		position = 10
	)
	default Title config()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum MS Delay",
		description = "Dont set this too high.",
		titleSection = "config",
		position = 11
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "randLower",
		name = "Maximum MS Delay",
		description = "Dont set this too high.",
		titleSection = "config",
		position = 12
	)
	default int randHigh()
	{
		return 80;
	}
}
