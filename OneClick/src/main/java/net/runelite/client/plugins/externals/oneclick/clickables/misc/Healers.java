package net.runelite.client.plugins.externals.oneclick.clickables.misc;

import com.google.common.collect.ImmutableMap;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;

public class Healers extends Clickable
{
	private static final ImmutableMap<String, Integer> ITEMS = ImmutableMap.<String, Integer>builder()
		.put("Pois. Worms", ItemID.POISONED_WORMS)
		.put("Pois. Tofu", ItemID.POISONED_TOFU)
		.put("Pois. Meat", ItemID.POISONED_MEAT)
		.build();

	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (event.getType() != MenuAction.EXAMINE_NPC.getId() || event.isForceLeftClick() || !event.getTarget().contains("Penance Healer"))
		{
			return false;
		}

		int id = ITEMS.getOrDefault(plugin.getRoleText(), -1);

		if (id == -1)
		{
			return false;
		}

		client.createMenuEntry(client.getMenuOptionCount())
			.setOption("Use")
			.setTarget("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer")
			.setType(MenuAction.WIDGET_TARGET_ON_NPC)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
		return true;
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		return event.getMenuTarget().equals("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer") &&
			updateSelectedItem(ITEMS.getOrDefault(plugin.getRoleText(), -1));
	}
}
