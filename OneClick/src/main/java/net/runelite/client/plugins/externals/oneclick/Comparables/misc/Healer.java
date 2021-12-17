package net.runelite.client.plugins.externals.oneclick.comparables.misc;

import com.google.common.collect.ImmutableMap;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
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
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getOpcode() == 1003 && !event.isForceLeftClick() && event.getTarget().contains("Penance Healer");
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null ||
			roleText == null ||
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

		client.createMenuEntry(-1)
			.setOption("Use")
			.setTarget("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer")
			.setType(MenuAction.ITEM_USE_ON_NPC)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuTarget().equalsIgnoreCase("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer") &&
			updateSelectedItem(ITEMS.getOrDefault(roleText, -1));
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{

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
