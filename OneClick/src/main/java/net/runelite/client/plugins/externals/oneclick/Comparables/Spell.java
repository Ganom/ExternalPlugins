package net.runelite.client.plugins.externals.oneclick.Comparables;

import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.externals.oneclick.ClickItem;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
public class Spell implements ClickComparable
{
	private final Predicate<MenuEntry> cast;
	private final Predicate<MenuEntry> reset;
	private final Predicate<MenuEntry> set;
	private final String spell;

	public Spell(String spell)
	{
		this.spell = spell;
		this.cast = (event) -> event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
			event.getOption().equals("Cast") &&
			event.getTarget().contains("<col=00ff00>" + spell + "</col><col=ffffff> -> ");
		this.reset = (event) -> event.getOpcode() == MenuOpcode.RUNELITE.getId() &&
			event.getIdentifier() == -1;
		this.set = (event) -> event.getOpcode() == MenuOpcode.RUNELITE.getId();
	}

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
			event.getOption().equals("Cast") &&
			event.getTarget().equals("<col=00ff00>" + spell + "</col>");
	}

	@Override
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.getClickItem() == null)
		{
			return;
		}

		event.setOption("Cast");
		event.setTarget("<col=00ff00>" + spell + "</col>" + "<col=ffffff> -> " + plugin.getClickItem().getName());
		event.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return cast.test(event) || reset.test(event) || set.test(event);
	}

	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event)
	{
		if (cast.test(event))
		{
			if (plugin.getClickItem() == null)
			{
				return;
			}

			final Pair<Integer, Integer> pair = plugin.findItem(plugin.getClickItem().getId());

			if (pair.getLeft() != -1)
			{
				event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
				event.setIdentifier(pair.getLeft());
				event.setParam0(pair.getRight());
				event.setParam1(WidgetInfo.INVENTORY.getId());
				plugin.getClient().setSelectedSpellName("<col=00ff00>" + spell + "</col>" + "<col=ffffff>");
				plugin.getClient().setSelectedSpellWidget(plugin.getSpellSelection().getWidgetInfo().getId());
			}
		}
		else if (reset.test(event))
		{
			plugin.setClickItem(null);
		}
		else if (set.test(event))
		{
			final String itemName = event.getTarget().split("->")[1];
			plugin.setClickItem(new ClickItem(itemName, event.getIdentifier()));
		}
	}
}