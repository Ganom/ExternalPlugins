package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.util.Text;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class KarambwansFire extends ClickCompare
{
	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getMenuOpcode() == MenuOpcode.EXAMINE_OBJECT && Text.standardize(event.getTarget()).equals("fire");
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(ItemID.RAW_KARAMBWAN).getLeft() == -1)
		{
			return;
		}
		MenuEntry e = event.clone();
		e.setOption("Use");
		e.setOpcode(MenuOpcode.RUNELITE.getId());
		e.setTarget("<col=ff9040>Raw karambwan<col=ffffff> -> " + event.getTarget());
		e.setForceLeftClick(true);
		insert(e);

		// deprioritize walk here
		MenuEntry[] entries = client.getMenuEntries();
		for (int i = 0; i < entries.length; i++)
		{
			MenuEntry entry = entries[i];
			if (entry.getMenuOpcode() == MenuOpcode.WALK)
			{
				entry.setOpcode(entry.getOpcode() + MenuOpcode.MENU_ACTION_DEPRIORITIZE_OFFSET);
				break;
			}
		}

		client.setMenuEntries(entries);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.RUNELITE.getId() &&
			event.getTarget().contains("<col=ff9040>Raw karambwan<col=ffffff> -> ");
	}

	@Override
	public void modifyClick(MenuEntry event)
	{
		if (updateSelectedItem(ItemID.RAW_KARAMBWAN) && plugin != null)
		{
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			plugin.setTick(true);
		}
	}
}
