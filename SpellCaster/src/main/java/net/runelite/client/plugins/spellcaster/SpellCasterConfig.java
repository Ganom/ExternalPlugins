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

package net.runelite.client.plugins.spellcaster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigTitleSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Title;
import net.runelite.client.plugins.spellcaster.utils.ActionType;

@ConfigGroup("SpellCaster")
public interface SpellCasterConfig extends Config
{
	@ConfigItem(
		keyName = "backToInventory",
		name = "Swap back to Inventory",
		description = "After finishing a sequence, it will swap back to inventory if enabled.",
		position = -1
	)
	default boolean backToInventory()
	{
		return true;
	}

	@ConfigTitleSection(
		position = 0,
		keyName = "ancientsStub",
		name = "Ancient Spells",
		description = ""
	)
	default Title ancientsStub()
	{
		return new Title();
	}

	@ConfigItem(
		position = 1,
		keyName = "ancientOne",
		name = "1",
		description = "",
		titleSection = "ancientsStub"
	)
	default Ancients ancientOne()
	{
		return Ancients.SMOKE_RUSH;
	}

	@ConfigItem(
		position = 2,
		keyName = "hotkeyAncientOne",
		name = "Spell 1 Hotkey",
		description = "",
		titleSection = "ancientsStub"
	)
	default Keybind hotkeyAncientOne()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 3,
		keyName = "ancientTwo",
		name = "2",
		description = "",
		titleSection = "ancientsStub"
	)
	default Ancients ancientTwo()
	{
		return Ancients.SMOKE_RUSH;
	}

	@ConfigItem(
		position = 4,
		keyName = "hotkeyAncientTwo",
		name = "Spell 2 Hotkey",
		description = "",
		titleSection = "ancientsStub"
	)
	default Keybind hotkeyAncientTwo()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 5,
		keyName = "ancientThree",
		name = "3",
		description = "",
		titleSection = "ancientsStub"
	)
	default Ancients ancientThree()
	{
		return Ancients.SMOKE_RUSH;
	}

	@ConfigItem(
		position = 6,
		keyName = "hotkeyAncientThree",
		name = "Spell 3 Hotkey",
		description = "",
		titleSection = "ancientsStub"
	)
	default Keybind hotkeyAncientThree()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 7,
		keyName = "ancientFour",
		name = "4",
		description = "",
		titleSection = "ancientsStub"
	)
	default Ancients ancientFour()
	{
		return Ancients.SMOKE_RUSH;
	}

	@ConfigItem(
		position = 8,
		keyName = "hotkeyAncientFour",
		name = "Spell 4 Hotkey",
		description = "",
		titleSection = "ancientsStub"
	)
	default Keybind hotkeyAncientFour()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 9,
		keyName = "ancientFive",
		name = "5",
		description = "",
		titleSection = "ancientsStub"
	)
	default Ancients ancientFive()
	{
		return Ancients.SMOKE_RUSH;
	}

	@ConfigItem(
		position = 10,
		keyName = "hotkeyAncientFive",
		name = "Spell 5 Hotkey",
		description = "",
		titleSection = "ancientsStub"
	)
	default Keybind hotkeyAncientFive()
	{
		return Keybind.NOT_SET;
	}

	@ConfigTitleSection(
		position = 0,
		keyName = "standardsStub",
		name = "Standard Spells",
		description = ""
	)
	default Title standardsStub()
	{
		return new Title();
	}

	@ConfigItem(
		position = 1,
		keyName = "standardOne",
		name = "1",
		description = "",
		titleSection = "standardsStub"
	)
	default Standards standardOne()
	{
		return Standards.WIND_STRIKE;
	}

	@ConfigItem(
		position = 2,
		keyName = "hotkeyStandardOne",
		name = "Spell 1 Hotkey",
		description = "",
		titleSection = "standardsStub"
	)
	default Keybind hotkeyStandardOne()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 3,
		keyName = "standardTwo",
		name = "2",
		description = "",
		titleSection = "standardsStub"
	)
	default Standards standardTwo()
	{
		return Standards.WIND_STRIKE;
	}

	@ConfigItem(
		position = 4,
		keyName = "hotkeyStandardTwo",
		name = "Spell 2 Hotkey",
		description = "",
		titleSection = "standardsStub"
	)
	default Keybind hotkeyStandardTwo()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 5,
		keyName = "standardThree",
		name = "3",
		description = "",
		titleSection = "standardsStub"
	)
	default Standards standardThree()
	{
		return Standards.WIND_STRIKE;
	}

	@ConfigItem(
		position = 6,
		keyName = "hotkeyStandardThree",
		name = "Spell 3 Hotkey",
		description = "",
		titleSection = "standardsStub"
	)
	default Keybind hotkeyStandardThree()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 7,
		keyName = "standardFour",
		name = "4",
		description = "",
		titleSection = "standardsStub"
	)
	default Standards standardFour()
	{
		return Standards.WIND_STRIKE;
	}

	@ConfigItem(
		position = 8,
		keyName = "hotkeyStandardFour",
		name = "Spell 4 Hotkey",
		description = "",
		titleSection = "standardsStub"
	)
	default Keybind hotkeyStandardFour()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 9,
		keyName = "standardFive",
		name = "5",
		description = "",
		titleSection = "standardsStub"
	)
	default Standards standardFive()
	{
		return Standards.WIND_STRIKE;
	}

	@ConfigItem(
		position = 10,
		keyName = "hotkeyStandardFive",
		name = "Spell 5 Hotkey",
		description = "",
		titleSection = "standardsStub"
	)
	default Keybind hotkeyStandardFive()
	{
		return Keybind.NOT_SET;
	}

	@ConfigTitleSection(
		position = 0,
		keyName = "lunarsStub",
		name = "Lunar Spells",
		description = ""
	)
	default Title lunarsStub()
	{
		return new Title();
	}

	//lunars
	@ConfigItem(
		position = 1,
		keyName = "lunarOne",
		name = "1",
		description = "",
		titleSection = "lunarsStub"
	)
	default Lunars lunarOne()
	{
		return Lunars.BAKE_PIE;
	}

	@ConfigItem(
		position = 2,
		keyName = "hotkeyLunarOne",
		name = "Spell 1 Hotkey",
		description = "",
		titleSection = "lunarsStub"
	)
	default Keybind hotkeyLunarOne()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 3,
		keyName = "lunarTwo",
		name = "2",
		description = "",
		titleSection = "lunarsStub"
	)
	default Lunars lunarTwo()
	{
		return Lunars.BAKE_PIE;
	}

	@ConfigItem(
		position = 4,
		keyName = "hotkeyLunarTwo",
		name = "Spell 2 Hotkey",
		description = "",
		titleSection = "lunarsStub"
	)
	default Keybind hotkeyLunarTwo()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 5,
		keyName = "lunarThree",
		name = "3",
		description = "",
		titleSection = "lunarsStub"
	)
	default Lunars lunarThree()
	{
		return Lunars.BAKE_PIE;
	}

	@ConfigItem(
		position = 6,
		keyName = "hotkeyLunarThree",
		name = "Spell 3 Hotkey",
		description = "",
		titleSection = "lunarsStub"
	)
	default Keybind hotkeyLunarThree()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 7,
		keyName = "lunarFour",
		name = "4",
		description = "",
		titleSection = "lunarsStub"
	)
	default Lunars lunarFour()
	{
		return Lunars.BAKE_PIE;
	}

	@ConfigItem(
		position = 8,
		keyName = "hotkeyLunarFour",
		name = "Spell 4 Hotkey",
		description = "",
		titleSection = "lunarsStub"
	)
	default Keybind hotkeyLunarFour()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 9,
		keyName = "lunarFive",
		name = "5",
		description = "",
		titleSection = "lunarsStub"
	)
	default Lunars lunarFive()
	{
		return Lunars.BAKE_PIE;
	}

	@ConfigItem(
		position = 10,
		keyName = "hotkeyLunarFive",
		name = "Spell 5 Hotkey",
		description = "",
		titleSection = "lunarsStub"
	)
	default Keybind hotkeyLunarFive()
	{
		return Keybind.NOT_SET;
	}

	//end lunars
	@ConfigTitleSection(
		position = 22,
		keyName = "botConfig",
		name = "Bot Config",
		description = ""
	)
	default Title botConfig()
	{
		return new Title();
	}

	@ConfigItem(
		position = 38,
		keyName = "actionType",
		name = "Action Type",
		description = "Flexo is smooth mouse, MouseEvents is ghost mouse.",
		titleSection = "botConfig"
	)
	default ActionType actionType()
	{
		return ActionType.FLEXO;
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum Delay",
		description = "For MouseEvents",
		position = 39,
		titleSection = "botConfig"
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "randLower",
		name = "Maximum Delay",
		description = "For MouseEvents",
		position = 40,
		titleSection = "botConfig"
	)
	default int randHigh()
	{
		return 80;
	}

	@Getter
	@AllArgsConstructor
	public enum Standards
	{
		LUMBRIDGE_HOME_TELEPORT("Lumbridge Home Teleport", WidgetInfo.SPELL_LUMBRIDGE_HOME_TELEPORT),
		WIND_STRIKE("Wind Strike", WidgetInfo.SPELL_WIND_STRIKE),
		CONFUSE("Confuse", WidgetInfo.SPELL_CONFUSE),
		ENCHANT_CROSSBOW_BOLT("Enchant Crossbow Bolt", WidgetInfo.SPELL_ENCHANT_CROSSBOW_BOLT),
		WATER_STRIKE("Water Strike", WidgetInfo.SPELL_WATER_STRIKE),
		LVL_1_ENCHANT("Lvl-1 Enchant", WidgetInfo.SPELL_LVL_1_ENCHANT),
		EARTH_STRIKE("Earth Strike", WidgetInfo.SPELL_EARTH_STRIKE),
		WEAKEN("Weaken", WidgetInfo.SPELL_WEAKEN),
		FIRE_STRIKE("Fire Strike", WidgetInfo.SPELL_FIRE_STRIKE),
		BONES_TO_BANANAS("Bones to Bananas", WidgetInfo.SPELL_BONES_TO_BANANAS),
		WIND_BOLT("Wind Bolt", WidgetInfo.SPELL_WIND_BOLT),
		CURSE("Curse", WidgetInfo.SPELL_CURSE),
		BIND("Bind", WidgetInfo.SPELL_BIND),
		LOW_LEVEL_ALCHEMY("Low Level Alchemy", WidgetInfo.SPELL_LOW_LEVEL_ALCHEMY),
		WATER_BOLT("Water Bolt", WidgetInfo.SPELL_WATER_BOLT),
		VARROCK_TELEPORT("Varrock Teleport", WidgetInfo.SPELL_VARROCK_TELEPORT),
		LVL_2_ENCHANT("Lvl-2 Enchant", WidgetInfo.SPELL_LVL_2_ENCHANT),
		EARTH_BOLT("Earth Bolt", WidgetInfo.SPELL_EARTH_BOLT),
		LUMBRIDGE_TELEPORT("Lumbridge Teleport", WidgetInfo.SPELL_LUMBRIDGE_TELEPORT),
		TELEKINETIC_GRAB("Telekinetic Grab", WidgetInfo.SPELL_TELEKINETIC_GRAB),
		FIRE_BOLT("Fire Bolt", WidgetInfo.SPELL_FIRE_BOLT),
		FALADOR_TELEPORT("Falador Teleport", WidgetInfo.SPELL_FALADOR_TELEPORT),
		CRUMBLE_UNDEAD("Crumble Undead", WidgetInfo.SPELL_CRUMBLE_UNDEAD),
		TELEPORT_TO_HOUSE("Teleport to House", WidgetInfo.SPELL_TELEPORT_TO_HOUSE),
		WIND_BLAST("Wind Blast", WidgetInfo.SPELL_WIND_BLAST),
		SUPERHEAT_ITEM("Superheat Item", WidgetInfo.SPELL_SUPERHEAT_ITEM),
		CAMELOT_TELEPORT("Camelot Teleport", WidgetInfo.SPELL_CAMELOT_TELEPORT),
		WATER_BLAST("Water Blast", WidgetInfo.SPELL_WATER_BLAST),
		LVL_3_ENCHANT("Lvl-3 Enchant", WidgetInfo.SPELL_LVL_3_ENCHANT),
		IBAN_BLAST("Iban Blast", WidgetInfo.SPELL_IBAN_BLAST),
		SNARE("Snare", WidgetInfo.SPELL_SNARE),
		MAGIC_DART("Magic Dart", WidgetInfo.SPELL_MAGIC_DART),
		ARDOUGNE_TELEPORT("Ardougne Teleport", WidgetInfo.SPELL_ARDOUGNE_TELEPORT),
		EARTH_BLAST("Earth Blast", WidgetInfo.SPELL_EARTH_BLAST),
		HIGH_LEVEL_ALCHEMY("High Level Alchemy", WidgetInfo.SPELL_HIGH_LEVEL_ALCHEMY),
		CHARGE_WATER_ORB("Charge Water Orb", WidgetInfo.SPELL_CHARGE_WATER_ORB),
		LVL_4_ENCHANT("Lvl-4 Enchant", WidgetInfo.SPELL_LVL_4_ENCHANT),
		WATCHTOWER_TELEPORT("Watchtower Teleport", WidgetInfo.SPELL_WATCHTOWER_TELEPORT),
		FIRE_BLAST("Fire Blast", WidgetInfo.SPELL_FIRE_BLAST),
		CHARGE_EARTH_ORB("Charge Earth Orb", WidgetInfo.SPELL_CHARGE_EARTH_ORB),
		BONES_TO_PEACHES("Bones to Peaches", WidgetInfo.SPELL_BONES_TO_PEACHES),
		SARADOMIN_STRIKE("Saradomin Strike", WidgetInfo.SPELL_SARADOMIN_STRIKE),
		CLAWS_OF_GUTHIX("Claws of Guthix", WidgetInfo.SPELL_CLAWS_OF_GUTHIX),
		FLAMES_OF_ZAMORAK("Flames of Zamorak", WidgetInfo.SPELL_FLAMES_OF_ZAMORAK),
		TROLLHEIM_TELEPORT("Trollheim Teleport", WidgetInfo.SPELL_TROLLHEIM_TELEPORT),
		WIND_WAVE("Wind Wave", WidgetInfo.SPELL_WIND_WAVE),
		CHARGE_FIRE_ORB("Charge Fire Orb", WidgetInfo.SPELL_CHARGE_FIRE_ORB),
		TELEPORT_TO_APE_ATOLL("Teleport to Ape Atoll", WidgetInfo.SPELL_TELEPORT_TO_APE_ATOLL),
		WATER_WAVE("Water Wave", WidgetInfo.SPELL_WATER_WAVE),
		CHARGE_AIR_ORB("Charge Air Orb", WidgetInfo.SPELL_CHARGE_AIR_ORB),
		VULNERABILITY("Vulnerability", WidgetInfo.SPELL_VULNERABILITY),
		LVL_5_ENCHANT("Lvl-5 Enchant", WidgetInfo.SPELL_LVL_5_ENCHANT),
		TELEPORT_TO_KOUREND("Teleport to Kourend", WidgetInfo.SPELL_TELEPORT_TO_KOUREND),
		EARTH_WAVE("Earth Wave", WidgetInfo.SPELL_EARTH_WAVE),
		ENFEEBLE("Enfeeble", WidgetInfo.SPELL_ENFEEBLE),
		TELEOTHER_LUMBRIDGE("Teleother Lumbridge", WidgetInfo.SPELL_TELEOTHER_LUMBRIDGE),
		FIRE_WAVE("Fire Wave", WidgetInfo.SPELL_FIRE_WAVE),
		ENTANGLE("Entangle", WidgetInfo.SPELL_ENTANGLE),
		STUN("Stun", WidgetInfo.SPELL_STUN),
		CHARGE("Charge", WidgetInfo.SPELL_CHARGE),
		WIND_SURGE("Wind Surge", WidgetInfo.SPELL_WIND_SURGE),
		TELEOTHER_FALADOR("Teleother Falador", WidgetInfo.SPELL_TELEOTHER_FALADOR),
		WATER_SURGE("Water Surge", WidgetInfo.SPELL_WATER_SURGE),
		TELE_BLOCK("Tele Block", WidgetInfo.SPELL_TELE_BLOCK),
		BOUNTY_TARGET_TELEPORT("Bounty Target Teleport", WidgetInfo.SPELL_BOUNTY_TARGET_TELEPORT),
		LVL_6_ENCHANT("Lvl-6 Enchant", WidgetInfo.SPELL_LVL_6_ENCHANT),
		TELEOTHER_CAMELOT("Teleother Camelot", WidgetInfo.SPELL_TELEOTHER_CAMELOT),
		EARTH_SURGE("Earth Surge", WidgetInfo.SPELL_EARTH_SURGE),
		LVL_7_ENCHANT("Lvl-7 Enchant", WidgetInfo.SPELL_LVL_7_ENCHANT),
		FIRE_SURGE("Fire Surge", WidgetInfo.SPELL_FIRE_SURGE);

		private String name;
		private WidgetInfo spell;

		@Override
		public String toString()
		{
			return getName();
		}
	}

	@Getter
	@AllArgsConstructor
	public enum Ancients
	{
		SMOKE_RUSH("Smoke Rush", WidgetInfo.SPELL_SMOKE_RUSH),
		SHADOW_RUSH("Shadow Rush", WidgetInfo.SPELL_SHADOW_RUSH),
		PADDEWWA_TELEPORT("Paddewwa Teleport", WidgetInfo.SPELL_PADDEWWA_TELEPORT),
		BLOOD_RUSH("Blood Rush", WidgetInfo.SPELL_BLOOD_RUSH),
		ICE_RUSH("Ice Rush", WidgetInfo.SPELL_ICE_RUSH),
		SENNTISTEN_TELEPORT("Senntisten Teleport", WidgetInfo.SPELL_SENNTISTEN_TELEPORT),
		SMOKE_BURST("Smoke Burst", WidgetInfo.SPELL_SMOKE_BURST),
		SHADOW_BURST("Shadow Burst", WidgetInfo.SPELL_SHADOW_BURST),
		KHARYRLL_TELEPORT("Kharyrll Teleport", WidgetInfo.SPELL_KHARYRLL_TELEPORT),
		BLOOD_BURST("Blood Burst", WidgetInfo.SPELL_BLOOD_BURST),
		ICE_BURST("Ice Burst", WidgetInfo.SPELL_ICE_BURST),
		LASSAR_TELEPORT("Lassar Teleport", WidgetInfo.SPELL_LASSAR_TELEPORT),
		SMOKE_BLITZ("Smoke Blitz", WidgetInfo.SPELL_SMOKE_BLITZ),
		SHADOW_BLITZ("Shadow Blitz", WidgetInfo.SPELL_SHADOW_BLITZ),
		DAREEYAK_TELEPORT("Dareeyak Teleport", WidgetInfo.SPELL_DAREEYAK_TELEPORT),
		BLOOD_BLITZ("Blood Blitz", WidgetInfo.SPELL_BLOOD_BLITZ),
		ICE_BLITZ("Ice Blitz", WidgetInfo.SPELL_ICE_BLITZ),
		CARRALLANGER_TELEPORT("Carrallanger Teleport", WidgetInfo.SPELL_CARRALLANGER_TELEPORT),
		SMOKE_BARRAGE("Smoke Barrage", WidgetInfo.SPELL_SMOKE_BARRAGE),
		SHADOW_BARRAGE("Shadow Barrage", WidgetInfo.SPELL_SHADOW_BARRAGE),
		ANNAKARL_TELEPORT("Annakarl Teleport", WidgetInfo.SPELL_ANNAKARL_TELEPORT),
		BLOOD_BARRAGE("Blood Barrage", WidgetInfo.SPELL_BLOOD_BARRAGE),
		ICE_BARRAGE("Ice Barrage", WidgetInfo.SPELL_ICE_BARRAGE),
		GHORROCK_TELEPORT("Ghorrock Teleport", WidgetInfo.SPELL_GHORROCK_TELEPORT),
		TELEPORT_TO_BOUNTY_TARGET("Teleport to Bounty Target", WidgetInfo.SPELL_BOUNTY_TARGET_TELEPORT2);

		private String name;
		private WidgetInfo spell;

		@Override
		public String toString()
		{
			return getName();
		}
	}

	@Getter
	@AllArgsConstructor
	public enum Lunars
	{
		BAKE_PIE("Bake Pie", WidgetInfo.SPELL_BAKE_PIE),
		GEOMANCY("Geomancy", WidgetInfo.SPELL_GEOMANCY),
		CURE_PLANT("Cure Plant", WidgetInfo.SPELL_CURE_PLANT),
		MONSTER_EXAMINE("Monster Examine", WidgetInfo.SPELL_MONSTER_EXAMINE),
		NPC_CONTACT("NPC Contact", WidgetInfo.SPELL_NPC_CONTACT),
		CURE_OTHER("Cure Other", WidgetInfo.SPELL_CURE_OTHER),
		HUMIDIFY("Humidify", WidgetInfo.SPELL_HUMIDIFY),
		MOONCLAN_TELEPORT("Moonclan Teleport", WidgetInfo.SPELL_MOONCLAN_TELEPORT),
		TELE_GROUP_MOONCLAN("Tele Group Moonclan", WidgetInfo.SPELL_TELE_GROUP_MOONCLAN),
		CURE_ME("Cure Me", WidgetInfo.SPELL_CURE_ME),
		HUNTER_KIT("Hunter Kit", WidgetInfo.SPELL_HUNTER_KIT),
		OURANIA_TELEPORT("Ourania Teleport", WidgetInfo.SPELL_OURANIA_TELEPORT),
		WATERBIRTH_TELEPORT("Waterbirth Teleport", WidgetInfo.SPELL_WATERBIRTH_TELEPORT),
		TELE_GROUP_WATERBIRTH("Tele Group Waterbirth", WidgetInfo.SPELL_TELE_GROUP_WATERBIRTH),
		CURE_GROUP("Cure Group", WidgetInfo.SPELL_CURE_GROUP),
		BARBARIAN_TELEPORT("Barbarian Teleport", WidgetInfo.SPELL_BARBARIAN_TELEPORT),
		STAT_SPY("Stat Spy", WidgetInfo.SPELL_STAT_SPY),
		SPIN_FLAX("Spin Flax", WidgetInfo.SPELL_SPIN_FLAX),
		TELE_GROUP_BARBARIAN("Tele Group Barbarian", WidgetInfo.SPELL_TELE_GROUP_BARBARIAN),
		SUPERGLASS_MAKE("Superglass Make", WidgetInfo.SPELL_SUPERGLASS_MAKE),
		KHAZARD_TELEPORT("Khazard Teleport", WidgetInfo.SPELL_KHAZARD_TELEPORT),
		TAN_LEATHER("Tan Leather", WidgetInfo.SPELL_TAN_LEATHER),
		TELE_GROUP_KHAZARD("Tele Group Khazard", WidgetInfo.SPELL_TELE_GROUP_KHAZARD),
		DREAM("Dream", WidgetInfo.SPELL_DREAM),
		STRING_JEWELLERY("String Jewellery", WidgetInfo.SPELL_STRING_JEWELLERY),
		STAT_RESTORE_POT_SHARE("Stat Restore Pot Share", WidgetInfo.SPELL_STAT_RESTORE_POT_SHARE),
		MAGIC_IMBUE("Magic Imbue", WidgetInfo.SPELL_MAGIC_IMBUE),
		FERTILE_SOIL("Fertile Soil", WidgetInfo.SPELL_FERTILE_SOIL),
		BOOST_POTION_SHARE("Boost Potion Share", WidgetInfo.SPELL_BOOST_POTION_SHARE),
		FISHING_GUILD_TELEPORT("Fishing Guild Teleport", WidgetInfo.SPELL_FISHING_GUILD_TELEPORT),
		TELE_GROUP_FISHING_GUILD("Tele Group Fishing Guild", WidgetInfo.SPELL_TELE_GROUP_FISHING_GUILD),
		PLANK_MAKE("Plank Make", WidgetInfo.SPELL_PLANK_MAKE),
		CATHERBY_TELEPORT("Catherby Teleport", WidgetInfo.SPELL_CATHERBY_TELEPORT),
		TELE_GROUP_CATHERBY("Tele Group Catherby", WidgetInfo.SPELL_TELE_GROUP_CATHERBY),
		ICE_PLATEAU_TELEPORT("Ice Plateau Teleport", WidgetInfo.SPELL_ICE_PLATEAU_TELEPORT),
		RECHARGE_DRAGONSTONE("Recharge Dragonstone", WidgetInfo.SPELL_RECHARGE_DRAGONSTONE),
		TELE_GROUP_ICE_PLATEAU("Tele Group Ice Plateau", WidgetInfo.SPELL_TELE_GROUP_ICE_PLATEAU),
		ENERGY_TRANSFER("Energy Transfer", WidgetInfo.SPELL_ENERGY_TRANSFER),
		HEAL_OTHER("Heal Other", WidgetInfo.SPELL_HEAL_OTHER),
		VENGEANCE_OTHER("Vengeance Other", WidgetInfo.SPELL_VENGEANCE_OTHER),
		VENGEANCE("Vengeance", WidgetInfo.SPELL_VENGEANCE),
		HEAL_GROUP("Heal Group", WidgetInfo.SPELL_HEAL_GROUP),
		SPELLBOOK_SWAP("Spellbook Swap", WidgetInfo.SPELL_SPELLBOOK_SWAP);

		private String name;
		private WidgetInfo spell;

		@Override
		public String toString()
		{
			return getName();
		}
	}
}
