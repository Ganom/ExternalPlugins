package net.runelite.client.plugins.externals.oneclick;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.api.Varbits;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.externals.oneclick.Comparables.Blank;
import net.runelite.client.plugins.externals.oneclick.Comparables.ClickComparable;
import net.runelite.client.plugins.externals.oneclick.Comparables.Spell;

@AllArgsConstructor
@Getter
public enum Spells
{
	HIGH_ALCH("High Alch", WidgetInfo.SPELL_HIGH_LEVEL_ALCHEMY, new Spell("High Level Alchemy"), SpriteID.SPELL_HIGH_LEVEL_ALCHEMY, SpriteID.SPELL_HIGH_LEVEL_ALCHEMY_DISABLED, 55, 0),
	SUPERHEAT("Superheat", WidgetInfo.SPELL_SUPERHEAT_ITEM, new Spell("Superheat Item"), SpriteID.SPELL_SUPERHEAT_ITEM, SpriteID.SPELL_SUPERHEAT_ITEM_DISABLED, 43, 0),

	ENCHANT_SAPPHIRE("Sapphire", WidgetInfo.SPELL_LVL_1_ENCHANT, new Spell("Lvl-1 Enchant"), SpriteID.SPELL_LVL_1_ENCHANT, SpriteID.SPELL_LVL_1_ENCHANT_DISABLED, 7, 0),
	ENCHANT_EMERALD("Emerald", WidgetInfo.SPELL_LVL_2_ENCHANT, new Spell("Lvl-2 Enchant"), SpriteID.SPELL_LVL_2_ENCHANT, SpriteID.SPELL_LVL_2_ENCHANT_DISABLED, 27, 0),
	ENCHANT_RUBY("Ruby", WidgetInfo.SPELL_LVL_3_ENCHANT, new Spell("Lvl-3 Enchant"), SpriteID.SPELL_LVL_3_ENCHANT, SpriteID.SPELL_LVL_3_ENCHANT_DISABLED, 49, 0),
	ENCHANT_DIAMOND("Diamond", WidgetInfo.SPELL_LVL_4_ENCHANT, new Spell("Lvl-4 Enchant"), SpriteID.SPELL_LVL_4_ENCHANT, SpriteID.SPELL_LVL_4_ENCHANT_DISABLED, 57, 0),
	ENCHANT_DRAGONSTONE("Dragonstone", WidgetInfo.SPELL_LVL_5_ENCHANT, new Spell("Lvl-5 Enchant"), SpriteID.SPELL_LVL_5_ENCHANT, SpriteID.SPELL_LVL_5_ENCHANT_DISABLED, 68, 0),
	ENCHANT_ONYX("Onyx", WidgetInfo.SPELL_LVL_6_ENCHANT, new Spell("Lvl-6 Enchant"), SpriteID.SPELL_LVL_6_ENCHANT, SpriteID.SPELL_LVL_6_ENCHANT_DISABLED, 87, 0),
	ENCHANT_ZENYTE("Zenyte", WidgetInfo.SPELL_LVL_7_ENCHANT, new Spell("Lvl-7 Enchant"), SpriteID.SPELL_LVL_7_ENCHANT, SpriteID.SPELL_LVL_7_ENCHANT_DISABLED, 93, 0),

	REANIMATE_GOBLIN("Reanimate Goblin", WidgetInfo.SPELL_REANIMATE_GOBLIN, new Spell("Reanimate Goblin"), SpriteID.SPELL_REANIMATE_GOBLIN, SpriteID.SPELL_REANIMATE_GOBLIN_DISABLED, 3, 3),
	REANIMATE_MONKEY("Reanimate Monkey", WidgetInfo.SPELL_REANIMATE_MONKEY, new Spell("Reanimate Monkey"), SpriteID.SPELL_REANIMATE_MONKEY, SpriteID.SPELL_REANIMATE_MONKEY_DISABLED, 7, 3),
	REANIMATE_IMP("Reanimate Imp", WidgetInfo.SPELL_REANIMATE_IMP, new Spell("Reanimate Imp"), SpriteID.SPELL_REANIMATE_IMP, SpriteID.SPELL_REANIMATE_IMP_DISABLED, 12, 3),
	REANIMATE_MINOTAUR("Reanimate Minotaur", WidgetInfo.SPELL_REANIMATE_MINOTAUR, new Spell("Reanimate Minotaur"), SpriteID.SPELL_REANIMATE_MINOTAUR, SpriteID.SPELL_REANIMATE_MINOTAUR_DISABLED, 16, 3),
	REANIMATE_SCORPION("Reanimate Scorpion", WidgetInfo.SPELL_REANIMATE_SCORPION, new Spell("Reanimate Scorpion"), SpriteID.SPELL_REANIMATE_SCORPION, SpriteID.SPELL_REANIMATE_SCORPION_DISABLED, 19, 3),
	REANIMATE_BEAR("Reanimate Bear", WidgetInfo.SPELL_REANIMATE_BEAR, new Spell("Reanimate Bear"), SpriteID.SPELL_REANIMATE_BEAR, SpriteID.SPELL_REANIMATE_BEAR_DISABLED, 21, 3),
	REANIMATE_UNICORN("Reanimate Unicorn", WidgetInfo.SPELL_REANIMATE_UNICORN, new Spell("Reanimate Unicorn"), SpriteID.SPELL_REANIMATE_UNICORN, SpriteID.SPELL_REANIMATE_UNICORN_DISABLED, 22, 3),
	REANIMATE_DOG("Reanimate Dog", WidgetInfo.SPELL_REANIMATE_DOG, new Spell("Reanimate Dog"), SpriteID.SPELL_REANIMATE_DOG, SpriteID.SPELL_REANIMATE_DOG_DISABLED, 26, 3),
	REANIMATE_CHAOS_DRUID("Reanimate Chaos Druid", WidgetInfo.SPELL_REANIMATE_CHAOS_DRUID, new Spell("Reanimate Chaos Druid"), SpriteID.SPELL_REANIMATE_CHAOS_DRUID, SpriteID.SPELL_REANIMATE_CHAOS_DRUID_DISABLED, 30, 3),
	REANIMATE_GIANT("Reanimate Giant", WidgetInfo.SPELL_REANIMATE_GIANT, new Spell("Reanimate Giant"), SpriteID.SPELL_REANIMATE_GIANT, SpriteID.SPELL_REANIMATE_GIANT_DISABLED, 37, 3),
	REANIMATE_OGRE("Reanimate Ogre", WidgetInfo.SPELL_REANIMATE_OGRE, new Spell("Reanimate Ogre"), SpriteID.SPELL_REANIMATE_OGRE, SpriteID.SPELL_REANIMATE_OGRE_DISABLED, 40, 3),
	REANIMATE_ELF("Reanimate Elf", WidgetInfo.SPELL_REANIMATE_ELF, new Spell("Reanimate Elf"), SpriteID.SPELL_REANIMATE_ELF, SpriteID.SPELL_REANIMATE_ELF_DISABLED, 43, 3),
	REANIMATE_TROLL("Reanimate Troll", WidgetInfo.SPELL_REANIMATE_TROLL, new Spell("Reanimate Troll"), SpriteID.SPELL_REANIMATE_TROLL, SpriteID.SPELL_REANIMATE_TROLL_DISABLED, 46, 3),
	REANIMATE_HORROR("Reanimate Horror", WidgetInfo.SPELL_REANIMATE_HORROR, new Spell("Reanimate Horror"), SpriteID.SPELL_REANIMATE_HORROR, SpriteID.SPELL_REANIMATE_HORROR_DISABLED, 52, 3),
	REANIMATE_KALPHITE("Reanimate Kalphite", WidgetInfo.SPELL_REANIMATE_KALPHITE, new Spell("Reanimate Kalphite"), SpriteID.SPELL_REANIMATE_KALPHITE, SpriteID.SPELL_REANIMATE_KALPHITE_DISABLED, 57, 3),
	REANIMATE_DAGANNOTH("Reanimate Dagannoth", WidgetInfo.SPELL_REANIMATE_DAGANNOTH, new Spell("Reanimate Dagannoth"), SpriteID.SPELL_REANIMATE_DAGANNOTH, SpriteID.SPELL_REANIMATE_DAGANNOTH_DISABLED, 62, 3),
	REANIMATE_BLOODVELD("Reanimate Bloodveld", WidgetInfo.SPELL_REANIMATE_BLOODVELD, new Spell("Reanimate Bloodveld"), SpriteID.SPELL_REANIMATE_BLOODVELD, SpriteID.SPELL_REANIMATE_BLOODVELD_DISABLED, 65, 3),
	REANIMATE_TZHAAR("Reanimate Tzhaar", WidgetInfo.SPELL_REANIMATE_TZHAAR, new Spell("Reanimate Tzhaar"), SpriteID.SPELL_REANIMATE_TZHAAR, SpriteID.SPELL_REANIMATE_TZHAAR_DISABLED, 69, 3),
	REANIMATE_DEMON("Reanimate Demon", WidgetInfo.SPELL_REANIMATE_DEMON, new Spell("Reanimate Demon"), SpriteID.SPELL_REANIMATE_DEMON, SpriteID.SPELL_REANIMATE_DEMON_DISABLED, 72, 3),
	REANIMATE_AVIANSIE("Reanimate Aviansie", WidgetInfo.SPELL_REANIMATE_AVIANSIE, new Spell("Reanimate Aviansie"), SpriteID.SPELL_REANIMATE_AVIANSIE, SpriteID.SPELL_REANIMATE_AVIANSIE_DISABLED, 78, 3),
	REANIMATE_ABYSSAL("Reanimate Abyssal", WidgetInfo.SPELL_REANIMATE_ABYSSAL, new Spell("Reanimate Abyssal"), SpriteID.SPELL_REANIMATE_ABYSSAL_CREATURE, SpriteID.SPELL_REANIMATE_ABYSSAL_CREATURE_DISABLED, 85, 3),
	REANIMATE_DRAGON("Reanimate Dragon", WidgetInfo.SPELL_REANIMATE_DRAGON, new Spell("Reanimate Dragon"), SpriteID.SPELL_REANIMATE_DRAGON, SpriteID.SPELL_REANIMATE_DRAGON_DISABLED, 93, 3),

	NONE("None", null, new Blank(), -1, -1, -1, -1);

	private String spells;
	private WidgetInfo widgetInfo;
	private ClickComparable comparable;
	private int enabledSpriteID;
	private int disabledSpriteID;
	private int requiredMagicLevel;
	private int spellbook;

	/**
	 * Returns whether the given spell Widget is a valid selection for the existing client, e.g.
	 *  if the client has the magic level, is on the proper spellbook, and has the runes to cast the spell.
	 * @param given
	 * @param client
	 * @return a boolean
	 */
	public boolean isValid(Widget given, Client client)
	{
		return given.getSpriteId() == enabledSpriteID &&
				given.getSpriteId() != disabledSpriteID &&
				client.getBoostedSkillLevel(Skill.MAGIC) >= requiredMagicLevel &&
				client.getVar(Varbits.SPELLBOOK) == spellbook;
	}

	@Override
	public String toString()
	{
		return getSpells();
	}
}
