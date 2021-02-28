package net.runelite.client.plugins.externals.oneclick.comparables.misc;

import com.google.common.collect.ImmutableMap;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

@Slf4j
public class Healer extends ClickCompare
{
	private static final ImmutableMap<String, Integer> ITEMS = ImmutableMap.<String, Integer>builder()
		.put("Pois. Worms", ItemID.POISONED_WORMS)
		.put("Pois. Tofu", ItemID.POISONED_TOFU)
		.put("Pois. Meat", ItemID.POISONED_MEAT)
		.build();

	@Setter
	private String roleText = "";

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == 1003 && !event.isForceLeftClick() && event.getTarget().contains("Penance Healer");
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (roleText == null ||
			roleText.isBlank() ||
			roleText.isEmpty() ||
			roleText.equals("- - -"))
		{
			return;
		}

		int id = ITEMS.getOrDefault(roleText, -1);

		if (id == -1)
		{
			log.error("This shouldn't be possible, bad string: {}", roleText);
			return;
		}

		MenuEntry e = event.clone();
		e.setOption("Use");
		e.setTarget("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer");
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuTarget().equalsIgnoreCase("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer");
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		if (event.getMenuTarget().equalsIgnoreCase("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer"))
		{
			if (updateSelectedItem(ITEMS.getOrDefault(roleText, -1)))
			{
				event.setMenuAction(MenuAction.ITEM_USE_ON_NPC);
			}
		}
	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (roleText == null ||
			roleText.isBlank() ||
			roleText.isEmpty() ||
			roleText.equals("- - -"))
		{
			return;
		}

		int id = ITEMS.getOrDefault(roleText, -1);

		if (id == -1)
		{
			log.error("This shouldn't be possible, bad string: {}", roleText);
			return;
		}

		e.setOption("Use");
		e.setTarget("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer");
		e.setForceLeftClick(true);
	}
}
