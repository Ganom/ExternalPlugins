package net.runelite.client.plugins.externals.oneclick.clickables.misc;

import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;

public class Vorkath extends Clickable
{
	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (!event.getTarget().contains("Zombified Spawn") || !event.getOption().equals("Attack"))
		{
			return false;
		}
		client.createMenuEntry(client.getMenuOptionCount())
			.setOption("Cast")
			.setTarget("(N) <col=00ff00>Crumble Undead</col><col=ffffff> -> <col=ffff00>Zombified Spawn<col=ff00>")
			.setType(MenuAction.WIDGET_TARGET_ON_NPC)
			.setIdentifier(event.getIdentifier())
			.setParam0(0)
			.setParam1(0)
			.setForceLeftClick(true);
		return true;
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		if (!event.getMenuTarget().equals("(N) <col=00ff00>Crumble Undead</col><col=ffffff> -> <col=ffff00>Zombified Spawn<col=ff00>"))
		{
			return false;
		}
		setSelectSpell(WidgetInfo.SPELL_CRUMBLE_UNDEAD);
		return true;
	}
}
