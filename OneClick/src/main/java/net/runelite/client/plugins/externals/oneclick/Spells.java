package net.runelite.client.plugins.externals.oneclick;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.widgets.WidgetInfo;

@AllArgsConstructor
@Getter
public enum Spells
{
	HIGH_ALCH("High Alch", WidgetInfo.SPELL_HIGH_LEVEL_ALCHEMY),
	SUPERHEAT("Superheat", WidgetInfo.SPELL_SUPERHEAT_ITEM),
	ENCHANT_SAPPHIRE("Sapphire", WidgetInfo.SPELL_LVL_1_ENCHANT),
	ENCHANT_EMERALD("Emerald", WidgetInfo.SPELL_LVL_2_ENCHANT),
	ENCHANT_RUBY("Ruby", WidgetInfo.SPELL_LVL_3_ENCHANT),
	ENCHANT_DIAMOND("Diamond", WidgetInfo.SPELL_LVL_4_ENCHANT),
	ENCHANT_DRAGONSTONE("Dragonstone", WidgetInfo.SPELL_LVL_5_ENCHANT),
	ENCHANT_ONYX("Onyx", WidgetInfo.SPELL_LVL_6_ENCHANT),
	ENCHANT_ZENYTE("Zenyte", WidgetInfo.SPELL_LVL_7_ENCHANT),
	NONE("None", null);

	private String spells;
	private WidgetInfo widgetInfo;

	@Override
	public String toString()
	{
		return getSpells();
	}
}
