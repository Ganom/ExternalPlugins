package net.runelite.client.plugins.externals.oneclick.comparables.skilling.MTA;

import lombok.Setter;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.externals.oneclick.Spells;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Predicate;

public class MTA_Alchemy extends ClickCompare
{
	private static final int MTA_ALCH_REGION = 13462;
	private static final int INFO_ITEM_START = 8;
	private static final int INFO_POINT_START = 13;
	private static final int INFO_LENGTH = 5;
	private static final int BEST_POINTS = 30;
	private final Predicate<MenuOptionClicked> cast;
	private final Predicate<MenuOptionClicked> reset;
	private final Predicate<MenuOptionClicked> set;
	private final String spell;
	@Setter
	private Spells spellSelection = Spells.HIGH_ALCH;

	public MTA_Alchemy()
	{
		this.spell = "High Level Alchemy";
		this.cast = (event) -> event.getMenuAction() == MenuAction.WIDGET_TYPE_2 &&
				event.getMenuOption().equals("Cast") &&
				event.getMenuTarget().contains(spell);
		this.reset = (event) -> event.getMenuAction() == MenuAction.RUNELITE &&
				event.getId() == -1;
		this.set = (event) -> event.getMenuAction() == MenuAction.RUNELITE;
	}

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuAction.WIDGET_TYPE_2.getId() &&
				event.getOption().equals("Cast") &&
				event.getTarget().equals("<col=00ff00>" + spell + "</col>");
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		AlchemyItem bestItem = getBest();
		if (plugin == null || bestItem == null)
		{
			return;
		}

		MenuEntry e = event.clone();
		e.setOption("Cast");

		//Get number of bestItem in inventory
		int numBestItems = getNumItems(bestItem.getId());
		e.setTarget(spellString(numBestItems, bestItem));
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return cast.test(event) || reset.test(event) || set.test(event);
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		if (plugin == null || client == null)
		{
			return;
		}

		if (cast.test(event))
		{
			AlchemyItem bestItem = getBest();
			if (bestItem == null)
			{
				return;
			}

			final Pair<Integer, Integer> pair = findItem(bestItem.getId());

			if (pair.getLeft() != -1)
			{
				event.setMenuAction(MenuAction.ITEM_USE_ON_WIDGET);
				event.setId(pair.getLeft());
				event.setActionParam(pair.getRight());
				event.setWidgetId(WidgetInfo.INVENTORY.getId());
				client.setSelectedSpellName("<col=00ff00>" + spell + "</col>" + "<col=ffffff>");
				client.setSelectedSpellWidget(spellSelection.getWidgetInfo().getId());
			}
		}
	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		AlchemyItem bestItem = getBest();
		if (plugin == null || bestItem == null)
		{
			return;
		}

		e.setOption("Cast");

		int numBestItems = getNumItems(bestItem.getId());
		e.setTarget(spellString(numBestItems, bestItem));
		e.setForceLeftClick(true);
	}

	private AlchemyItem getBest()
	{
		Player player = client.getLocalPlayer();
		if (player == null || player.getWorldLocation().getRegionID() != MTA_ALCH_REGION || player.getWorldLocation().getPlane() != 2)
		{
			return null;
		}
		for (int i = 0; i < INFO_LENGTH; i++)
		{
			Widget textWidget = client.getWidget(WidgetID.MTA_ALCHEMY_GROUP_ID, INFO_ITEM_START + i);
			if (textWidget == null)
			{
				return AlchemyItem.LEATHER_BOOTS;
			}

			String item = textWidget.getText();
			Widget pointsWidget = client.getWidget(WidgetID.MTA_ALCHEMY_GROUP_ID, INFO_POINT_START + i);
			int points;

			try
			{
				points = Integer.parseInt(pointsWidget.getText());
			}
			catch (NumberFormatException e)
			{
				return AlchemyItem.LEATHER_BOOTS;
			}

			if (points == BEST_POINTS)
			{
				return AlchemyItem.find(item);
			}
		}
		return AlchemyItem.LEATHER_BOOTS;
	}

	public int getNumItems(int bestItemID)
	{
		final Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		final List<WidgetItem> itemList = (List<WidgetItem>) inventoryWidget.getWidgetItems();

		int numItems = 0;

		for (int i = itemList.size() - 1; i >= 0; i--)
		{
			final WidgetItem item = itemList.get(i);
			if (item.getId() == bestItemID)
			{
				numItems++;
			}
		}
		return numItems;
	}

	public String spellString(int numBestItems, AlchemyItem bestItem)
	{
		String e = "Inv";
		if (numBestItems > 1)
		{
			e = ("<col=09ff00>" + spell + " -> " + bestItem.getName() + "(" + numBestItems + ")");
		}
		else if (numBestItems == 1)
		{
			e = ("<col=f2ff00>" + spell + " -> " + bestItem.getName() + "(" + numBestItems + ")");
		}
		else
		{
			e = ("<col=ff0000>" + spell + " -> " + bestItem.getName() + "(" + numBestItems + ")");
		}
		return e;
	}

}
