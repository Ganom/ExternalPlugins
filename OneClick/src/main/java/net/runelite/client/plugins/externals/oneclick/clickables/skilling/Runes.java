package net.runelite.client.plugins.externals.oneclick.clickables.skilling;

import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;

public class Runes extends Clickable
{
	private static final Set<Integer> VALID_COMBO_RUNES = Set.of(ItemID.AIR_RUNE, ItemID.EARTH_RUNE, ItemID.WATER_RUNE, ItemID.FIRE_RUNE);
	private static final String MAGIC_IMBUE = "<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself";
	private static final String COMBO_RUNE = "<col=ff9040>Elemental Rune<col=ffffff> -> <col=ffff>Altar";

	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (event.getType() != MenuAction.GAME_OBJECT_FIRST_OPTION.getId() ||
			event.isForceLeftClick() ||
			!event.getOption().equals("Craft-rune") ||
			!event.getTarget().equals("<col=ffff>Altar"))
		{
			return false;
		}

		var item = findItemWithIds(VALID_COMBO_RUNES);

		if (item == null)
		{
			return false;
		}

		if (config.isMagicImbueEnabled() && !plugin.isImbued() && VALID_COMBO_RUNES.contains(item.getId()))
		{
			client.createMenuEntry(client.getMenuOptionCount())
				.setOption("Use")
				.setTarget(MAGIC_IMBUE)
				.setType(MenuAction.CC_OP)
				.setIdentifier(1)
				.setParam0(-1)
				.setParam1(WidgetInfo.SPELL_MAGIC_IMBUE.getId())
				.setForceLeftClick(true);
			return true;
		}
		client.createMenuEntry(client.getMenuOptionCount())
			.setOption("Use")
			.setTarget(COMBO_RUNE)
			.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
		return true;
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		switch (event.getMenuTarget())
		{
			case MAGIC_IMBUE:
				return true;
			case COMBO_RUNE:
				return updateSelectedItem(VALID_COMBO_RUNES);
			default:
				return false;
		}
	}
}
