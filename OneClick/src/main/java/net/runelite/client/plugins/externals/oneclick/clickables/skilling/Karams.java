package net.runelite.client.plugins.externals.oneclick.clickables.skilling;

import net.runelite.api.ItemID;
import static net.runelite.api.ItemID.RAW_KARAMBWAN;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;

public class Karams extends Clickable
{
	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (event.getType() != MenuAction.GAME_OBJECT_FIRST_OPTION.getId() ||
			event.isForceLeftClick() ||
			!event.getOption().equals("Cook") ||
			findItem(RAW_KARAMBWAN) == null)
		{
			return false;
		}

		client.createMenuEntry(client.getMenuOptionCount())
			.setOption("Use")
			.setTarget("<col=ff9040>Raw karambwan<col=ffffff> -> " + event.getTarget())
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
		if (!event.getMenuTarget().contains("<col=ff9040>Raw karambwan<col=ffffff> -> ") || !updateSelectedItem(ItemID.RAW_KARAMBWAN))
		{
			return false;
		}
		plugin.setTick(true);
		return true;
	}
}
