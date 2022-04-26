package net.runelite.client.plugins.externals.oneclick.clickables.misc;

import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.pojos.CustomItem;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;
import net.runelite.client.util.Text;

public class Custom extends Clickable
{
	private CustomItem currentItemTarget = null;

	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (event.getType() != MenuAction.WIDGET_TARGET.getId() || !isValid(event.getTarget()))
		{
			return false;
		}
		client.createMenuEntry(client.getMenuOptionCount())
			.setOption(event.getOption())
			.setTarget(currentItemTarget.getTargetString())
			.setIdentifier(0)
			.setType(MenuAction.WIDGET_TARGET_ON_WIDGET)
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
		return true;
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		if (currentItemTarget == null || !event.getMenuTarget().equals(currentItemTarget.getTargetString()))
		{
			return false;
		}
		return updateSelectedItem(currentItemTarget.getOnThisId());
	}

	private boolean isValid(String target)
	{
		currentItemTarget = plugin.getItems()
			.stream()
			.filter(item -> item.getUseThisName().equalsIgnoreCase(Text.removeTags(target)))
			.filter(item -> findItem(item.getUseThisId()) != null && findItem(item.getOnThisId()) != null)
			.findFirst()
			.orElse(null);
		return currentItemTarget != null;
	}
}