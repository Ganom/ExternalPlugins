package net.runelite.client.plugins.externals.oneclick.Comparables;

import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

public class Runes implements ClickComparable
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
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.findItem(id).getLeft() == -1)
		{
			return;
		}

		if (!plugin.isImbue() && plugin.isEnableImbue())
		{
			event.setOption("Use");
			event.setTarget("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself");
			event.setForceLeftClick(true);
			return;
		}
		event.setOption("Use");
		event.setTarget(rune);
		event.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return (event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getTarget().equals(rune)) ||
			(event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getTarget().equals("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself"));
	}

	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event)
	{
		if (event.getTarget().equals(rune))
		{
			if (plugin.updateSelectedItem(id))
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
