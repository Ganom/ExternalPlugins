package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Runes extends ClickCompare
{
	private final String rune;
	private final int id;

	public Runes(String rune, int id)
	{
		this.rune = "<col=ff9040>" + rune + "<col=ffffff> -> <col=ffff>Altar";
		this.id = id;
	}

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuAction.GAME_OBJECT_FIRST_OPTION.getId() && !event.isForceLeftClick() &&
			event.getOption().equals("Craft-rune") &&
			event.getTarget().equals("<col=ffff>Altar");
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(id).getLeft() == -1 || plugin == null)
		{
			return;
		}

		if (!plugin.isImbue() && plugin.isEnableImbue())
		{
			MenuEntry e = event.clone();
			e.setOption("Use");
			e.setTarget("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself");
			e.setForceLeftClick(true);
			insert(e);
			return;
		}
		MenuEntry e = event.clone();
		e.setOption("Use");
		e.setTarget(rune);
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return (event.getMenuAction() == MenuAction.GAME_OBJECT_FIRST_OPTION && event.getMenuTarget().equals(rune)) ||
			(event.getMenuAction() == MenuAction.GAME_OBJECT_FIRST_OPTION && event.getMenuTarget().equals("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself"));
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		if (event.getMenuTarget().equals(rune))
		{
			if (updateSelectedItem(id))
			{
				event.setMenuAction(MenuAction.ITEM_USE_ON_GAME_OBJECT);
			}
		}
		else if (event.getMenuTarget().equals("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself"))
		{
			event.setId(1);
			event.setMenuAction(MenuAction.CC_OP);
			event.setActionParam(-1);
			event.setWidgetId(WidgetInfo.SPELL_MAGIC_IMBUE.getId());
		}
	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(id).getLeft() == -1 || plugin == null)
		{
			return;
		}

		if (!plugin.isImbue() && plugin.isEnableImbue())
		{
			e.setOption("Use");
			e.setTarget("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself");
			e.setForceLeftClick(true);
			return;
		}
		e.setOption("Use");
		e.setTarget(rune);
		e.setForceLeftClick(true);
	}
}
