package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
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
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
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
	public boolean isClickValid(MenuEntry event)
	{
		return (event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getTarget().equals(rune)) ||
			(event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getTarget().equals("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself"));
	}

	@Override
	public void modifyClick(MenuEntry event)
	{
		if (event.getTarget().equals(rune))
		{
			if (updateSelectedItem(id))
			{
				event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			}
		}
		else if (event.getTarget().equals("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself"))
		{
			event.setIdentifier(1);
			event.setOpcode(MenuOpcode.CC_OP.getId());
			event.setParam0(-1);
			event.setParam1(WidgetInfo.SPELL_MAGIC_IMBUE.getId());
		}
	}
}
