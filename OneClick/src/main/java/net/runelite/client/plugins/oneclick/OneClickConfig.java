package net.runelite.client.plugins.oneclick;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oneclick")
public interface OneClickConfig extends Config
{
	@ConfigItem(
		keyName = "darts",
		name = "Darts",
		description = "One click fletch dart tips into darts"
	)
	default boolean getDarts()
	{
		return false;
	}

	@ConfigItem(
		keyName = "logs",
		name = "Light Logs",
		description = "One click light logs"
	)
	default boolean getLightLogs()
	{
		return false;
	}

	@ConfigItem(
		keyName = "birdhouses",
		name = "Bird Houses",
		description = "One click birdhouses"
	)
	default boolean getBirdHouses()
	{
		return false;
	}

	@ConfigItem(
		keyName = "herbtar",
		name = "Herb Tar",
		description = "One click to make herbs into herb tar"
	)
	default boolean getHerbTar()
	{
		return false;
	}

	@ConfigItem(
		keyName = "earthrune",
		name = "Earth Rune on Altar",
		description = "One click to use earth rune on altar"
	)
	default boolean getEarthRuneAltar()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highalch",
		name = "High Alchemy",
		description = "One click to cast high alchemy"
	)
	default boolean getHighAlch()
	{
		return false;
	}

	@ConfigItem(
		keyName = "dwarfmulticannon",
		name = "Dwarf Multicannon",
		description = "One click to fill the dwarf multicannon (Uses cannonballs on it instead of clicking fire to save a tick)"
	)
	default boolean getDwarfMultiCannon()
	{
		return false;
	}

	@ConfigItem(
		keyName = "bones",
		name = "Bones",
		description = "One click to use bones on altar"
	)
	default boolean getBones()
	{
		return false;
	}

	@ConfigItem(
		keyName = "karambwan",
		name = "Karambwan",
		description = "One click to use karambwan on range"
	)
	default boolean getKarambwan()
	{
		return false;
	}
	/*     Hasn't been implemeted yet but will be in the future
	@ConfigItem(
		keyName = "antidoteplusplus",
		name = "Antidote++",
		description = "One click to use antidote++ on zulrah's scales"
	)
	default boolean getAntidotePlusPlus()
	{
		return false;
	}*/

	@ConfigItem(
		keyName = "darkessence",
		name = "Dark Essence",
		description = "One click on chisel to automatically chisel dark essence"
	)
	default boolean getDarkEssence()
	{
		return false;
	}

	@ConfigItem(
			keyName = "cutfish",
			name = "Cut Fish",
			description = "One click to cut Sacred Eels and Barbarian Fishes"
	)
	default boolean getCutFish()
	{
		return false;
	}

	@ConfigItem(
			keyName = "cutfish",
			name = "Cut Fish",
			description = "One click to cut Sacred Eels and Barbarian Fishes"
	)
	default boolean getCutFish()
	{
		return false;
	}

}
