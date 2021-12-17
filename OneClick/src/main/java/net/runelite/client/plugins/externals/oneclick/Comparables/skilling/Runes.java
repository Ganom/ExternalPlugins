package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
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
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getOpcode() == MenuAction.GAME_OBJECT_FIRST_OPTION.getId() && !event.isForceLeftClick() &&
			event.getOption().equals("Craft-rune") &&
			event.getTarget().equals("<col=ffff>Altar");
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null || findItem(id).getLeft() == -1 || plugin == null)
		{
			return;
		}

		if (!plugin.isImbue() && plugin.isEnableImbue())
		{
			client.createMenuEntry(-1)
				.setOption("Use")
				.setTarget("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself")
				.setType(MenuAction.CC_OP)
				.setIdentifier(1)
				.setParam0(-1)
				.setParam1(WidgetInfo.SPELL_MAGIC_IMBUE.getId())
				.setForceLeftClick(true);
			return;
		}
		client.createMenuEntry(-1)
			.setOption("Use")
			.setTarget(rune)
			.setType(MenuAction.ITEM_USE_ON_GAME_OBJECT)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		if (event.getMenuAction() == MenuAction.GAME_OBJECT_FIRST_OPTION &&
			event.getMenuTarget().equals(rune) &&
			updateSelectedItem(id))
		{
			return true;
		}
		else return event.getMenuTarget().equals("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself");
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{

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
