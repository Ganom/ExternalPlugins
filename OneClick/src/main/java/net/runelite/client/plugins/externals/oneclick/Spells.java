package net.runelite.client.plugins.externals.oneclick;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.externals.oneclick.Comparables.Blank;
import net.runelite.client.plugins.externals.oneclick.Comparables.ClickComparable;
import net.runelite.client.plugins.externals.oneclick.Comparables.Spell;

@AllArgsConstructor
@Getter
public enum Spells
{
	HIGH_ALCH("High Alch", WidgetInfo.SPELL_HIGH_LEVEL_ALCHEMY, new Spell("High Level Alchemy")),
	SUPERHEAT("Superheat", WidgetInfo.SPELL_SUPERHEAT_ITEM, new Spell("Superheat Item")),
	ENCHANT_SAPPHIRE("Sapphire", WidgetInfo.SPELL_LVL_1_ENCHANT, new Spell("Lvl-1 Enchant")),
	ENCHANT_EMERALD("Emerald", WidgetInfo.SPELL_LVL_2_ENCHANT, new Spell("Lvl-2 Enchant")),
	ENCHANT_RUBY("Ruby", WidgetInfo.SPELL_LVL_3_ENCHANT, new Spell("Lvl-3 Enchant")),
	ENCHANT_DIAMOND("Diamond", WidgetInfo.SPELL_LVL_4_ENCHANT, new Spell("Lvl-4 Enchant")),
	ENCHANT_DRAGONSTONE("Dragonstone", WidgetInfo.SPELL_LVL_5_ENCHANT, new Spell("Lvl-5 Enchant")),
	ENCHANT_ONYX("Onyx", WidgetInfo.SPELL_LVL_6_ENCHANT, new Spell("Lvl-6 Enchant")),
	ENCHANT_ZENYTE("Zenyte", WidgetInfo.SPELL_LVL_7_ENCHANT, new Spell("Lvl-7 Enchant")),
	NONE("None", null, new Blank());

	private String spells;
	private WidgetInfo widgetInfo;
	private ClickComparable comparable;

	@Override
	public String toString()
	{
		return getSpells();
	}
}
