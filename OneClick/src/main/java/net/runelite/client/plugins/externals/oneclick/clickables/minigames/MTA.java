package net.runelite.client.plugins.externals.oneclick.clickables.minigames;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import static net.runelite.api.ItemID.CUBE;
import static net.runelite.api.ItemID.CYLINDER;
import static net.runelite.api.ItemID.DRAGONSTONE;
import static net.runelite.api.ItemID.DRAGONSTONE_6903;
import static net.runelite.api.ItemID.ICOSAHEDRON;
import static net.runelite.api.ItemID.PENTAMID;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;
import net.runelite.client.plugins.externals.oneclick.pojos.ItemData;
import net.runelite.client.plugins.mta.alchemy.AlchemyItem;

@Slf4j
public class MTA extends Clickable
{
	private AlchemyItem previousBest;
	private int consumeOverrideTick;

	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (insideTeleRoom())
		{
			return teleRoom(event);
		}
		else if (insideAlchemyRoom())
		{
			return alchemyRoom(event);
		}
		else if (insideEnchantRoom())
		{
			return enchantRoom(event);
		}
		return false;
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		if (!event.getMenuOption().equals("Search") || !event.getMenuTarget().contains("Cupboard") || !insideAlchemyRoom())
		{
			return false;
		}
		var best = getBest();
		if (best == null || getItemCount(best.getId()) < 3 || client.getTickCount() < consumeOverrideTick)
		{
			return false;
		}
		event.consume();
		return true;
	}

	public boolean insideEnchantRoom()
	{
		Player player = client.getLocalPlayer();
		return player != null && player.getWorldLocation().getRegionID() == 13462
			&& player.getWorldLocation().getPlane() == 0;
	}

	private boolean enchantRoom(MenuEntryAdded event)
	{
		if (!event.getOption().equals("Cast") ||
			!event.getTarget().contains("Enchant") ||
			event.isForceLeftClick() ||
			event.getType() != MenuAction.WIDGET_TARGET.getId())
		{
			return false;
		}
		ItemData item = findItemWithIds(Set.of(ICOSAHEDRON, CYLINDER, CUBE, PENTAMID, DRAGONSTONE, DRAGONSTONE_6903));
		if (item == null)
		{
			return false;
		}
		client.createMenuEntry(client.getMenuOptionCount())
			.setOption("Cast")
			.setTarget("<col=00ff00>Lvl-6 Enchant</col><col=ffffff> -> <col=ff9040>" +
				item.getName() +
				"(" +
				getItemCount(item.getId()) +
				") </col>"
			)
			.setType(MenuAction.WIDGET_TARGET_ON_WIDGET)
			.setIdentifier(0)
			.setParam0(item.getIndex())
			.setParam1(9764864)
			.setForceLeftClick(true)
			.onClick(e ->
			{
				refreshInventory();
				setSelectSpell(WidgetInfo.SPELL_LVL_6_ENCHANT);
			});
		return true;
	}

	private boolean insideTeleRoom()
	{
		return client.getWidget(WidgetID.MTA_TELEKINETIC_GROUP_ID, 0) != null;
	}

	private boolean teleRoom(MenuEntryAdded event)
	{
		if (!event.getTarget().contains("Maze Guardian") || !event.getOption().contains("Observe"))
		{
			return false;
		}
		client.createMenuEntry(client.getMenuOptionCount())
			.setOption("Cast")
			.setTarget("<col=00ff00>Telekinetic Grab</col><col=ffffff> -> <col=ffff00><col=ff9040>Maze Guardian</col>")
			.setType(MenuAction.WIDGET_TARGET_ON_NPC)
			.setIdentifier(event.getIdentifier())
			.setParam0(0)
			.setParam1(0)
			.setForceLeftClick(true)
			.onClick(e -> setSelectSpell(WidgetInfo.SPELL_TELEKINETIC_GRAB));
		return true;
	}

	private boolean insideAlchemyRoom()
	{
		Player player = client.getLocalPlayer();
		return player != null && player.getWorldLocation().getRegionID() == 13462
			&& player.getWorldLocation().getPlane() == 2;
	}

	private boolean alchemyRoom(MenuEntryAdded event)
	{
		if (!event.getOption().equals("Search") ||
			!event.getTarget().contains("Cupboard") ||
			event.isForceLeftClick() ||
			event.getType() != 3 ||
			plugin.getHighAlchTicks() > 0)
		{
			return false;
		}

		var best = getBest();

		if (best == null)
		{
			return false;
		}

		if (best != previousBest)
		{
			previousBest = best;
			consumeOverrideTick = client.getTickCount() + 10;
		}

		ItemData item = findItem(best.getId());

		if (item == null)
		{
			return false;
		}

		client.createMenuEntry(client.getMenuOptionCount())
			.setOption("Cast")
			.setTarget("<col=00ff00>" +
				"High Level Alchemy" +
				"</col><col=ffffff> -> <col=ff9040>" +
				item.getName() +
				"</col>"
			)
			.setType(MenuAction.WIDGET_TARGET_ON_WIDGET)
			.setIdentifier(0)
			.setParam0(item.getIndex())
			.setParam1(9764864)
			.setForceLeftClick(true)
			.onClick(e ->
			{
				refreshInventory();
				setSelectSpell(WidgetInfo.SPELL_HIGH_LEVEL_ALCHEMY);
			});
		return true;
	}

	private AlchemyItem getBest()
	{
		for (int i = 0; i < 5; i++)
		{
			Widget textWidget = client.getWidget(WidgetID.MTA_ALCHEMY_GROUP_ID, 7 + i);

			if (textWidget == null)
			{
				return null;
			}

			String item = textWidget.getText();
			Widget pointsWidget = client.getWidget(WidgetID.MTA_ALCHEMY_GROUP_ID, 12 + i);

			if (pointsWidget == null)
			{
				return null;
			}

			int points = Integer.parseInt(pointsWidget.getText());

			if (points == 30)
			{
				return AlchemyItem.find(item);
			}
		}

		return null;
	}
}
