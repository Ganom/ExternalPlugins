package net.runelite.client.plugins.externals.oneclick.clickables.skilling;

import javax.inject.Singleton;
import static net.runelite.api.ItemID.TINDERBOX;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;

@Singleton
public class Firemaking extends Clickable
{
	private String modifiedTarget = "";

	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (event.isForceLeftClick() ||
			!event.getOption().contains("Use") ||
			!event.getTarget().toLowerCase().contains("log") ||
			event.getType() != MenuAction.WIDGET_TARGET.getId())
		{
			return false;
		}

		modifiedTarget = "<col=ff9040>Tinderbox<col=ffffff> -> " + event.getTarget();

		client.createMenuEntry(client.getMenuOptionCount())
			.setOption(event.getOption())
			.setTarget(modifiedTarget)
			.setType(MenuAction.WIDGET_TARGET_ON_WIDGET)
			.setIdentifier(0)
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
		return true;
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		if (!event.getMenuTarget().equals(modifiedTarget))
		{
			return false;
		}
		return updateSelectedItem(TINDERBOX);
	}
}