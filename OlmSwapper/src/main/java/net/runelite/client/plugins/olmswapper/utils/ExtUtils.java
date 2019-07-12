package net.runelite.client.plugins.olmswapper.utils;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.flexo.Flexo;

@Slf4j
public class ExtUtils
{
	public static int[] stringToIntArray(String string)
	{
		return Arrays.stream(string.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
	}

	public static List<WidgetItem> getItems(int[] itemIds, Client client)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

		ArrayList<Integer> itemIDs = new ArrayList<>();

		for (int i : itemIds)
		{
			itemIDs.add(i);
		}

		List<WidgetItem> listToReturn = new ArrayList<>();

		for (WidgetItem item : inventoryWidget.getWidgetItems())
		{
			if (itemIDs.contains(item.getId()))
			{
				listToReturn.add(item);
			}
		}

		return listToReturn;
	}

	public static List<Widget> getEquippedItems(int[] itemIds, Client client)
	{
		Widget equipmentWidget = client.getWidget(WidgetInfo.EQUIPMENT);

		ArrayList<Integer> equippedIds = new ArrayList<>();

		for (int i : itemIds)
		{
			equippedIds.add(i);
		}

		List<Widget> equipped = new ArrayList<>();

		if (equipmentWidget.getStaticChildren() != null)
		{
			for (Widget widgets : equipmentWidget.getStaticChildren())
			{
				for (Widget items : widgets.getDynamicChildren())
				{
					if (equippedIds.contains(items.getItemId()))
					{
						equipped.add(items);
					}
				}
			}
		}
		else
		{
			log.error("Static Children is Null!");
		}

		return equipped;
	}

	public static void handleSwitch(Rectangle rectangle, ActionType actionType, Flexo flexo, Client client, double scalingfactor, int delay)
	{
		Point cp = ExtUtils.getClickPoint(rectangle, scalingfactor, client.isStretchedEnabled());

		if (cp.getX() >= 1)
		{
			switch (actionType)
			{
				case FLEXO:
					flexo.mouseMove(cp.getX(), cp.getY());
					flexo.mousePressAndRelease(1);
					break;
				case MOUSEEVENTS:
					leftClick(cp.getX(), cp.getY(), client, scalingfactor);
					flexo.delay(delay);
					break;
			}
		}
	}

	public static void leftClick(int x, int y, Client client, double scalingFactor)
	{
		if (client.isStretchedEnabled())
		{
			Point p = client.getMouseCanvasPosition();
			if (p.getX() != x || p.getY() != y)
			{
				moveMouse(x, y, client);
			}
			double scale = 1 + (scalingFactor / 100);

			MouseEvent mousePressed =
				new MouseEvent(client.getCanvas(), 501, System.currentTimeMillis(), 0, (int) (client.getMouseCanvasPosition().getX() * scale), (int) (client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased =
				new MouseEvent(client.getCanvas(), 502, System.currentTimeMillis(), 0, (int) (client.getMouseCanvasPosition().getX() * scale), (int) (client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked =
				new MouseEvent(client.getCanvas(), 500, System.currentTimeMillis(), 0, (int) (client.getMouseCanvasPosition().getX() * scale), (int) (client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			client.getCanvas().dispatchEvent(mouseClicked);
		}
		if (!client.isStretchedEnabled())
		{
			Point p = client.getMouseCanvasPosition();
			if (p.getX() != x || p.getY() != y)
			{
				moveMouse(x, y, client);
			}
			MouseEvent mousePressed = new MouseEvent(client.getCanvas(), 501, System.currentTimeMillis(), 0, client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), 1, false, 1);
			client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased = new MouseEvent(client.getCanvas(), 502, System.currentTimeMillis(), 0, client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), 1, false, 1);
			client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked = new MouseEvent(client.getCanvas(), 500, System.currentTimeMillis(), 0, client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY(), 1, false, 1);
			client.getCanvas().dispatchEvent(mouseClicked);
		}
	}

	private static void moveMouse(int x, int y, Client client)
	{
		MouseEvent mouseEntered = new MouseEvent(client.getCanvas(), 504, System.currentTimeMillis(), 0, x, y, 0, false);
		client.getCanvas().dispatchEvent(mouseEntered);
		MouseEvent mouseExited = new MouseEvent(client.getCanvas(), 505, System.currentTimeMillis(), 0, x, y, 0, false);
		client.getCanvas().dispatchEvent(mouseExited);
		MouseEvent mouseMoved = new MouseEvent(client.getCanvas(), 503, System.currentTimeMillis(), 0, x, y, 0, false);
		client.getCanvas().dispatchEvent(mouseMoved);
	}

	private static Point getClickPoint(Rectangle rect, double scalingFactor, boolean stretchedMode)
	{
		if (stretchedMode)
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
			int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);
			double scale = 1 + (scalingFactor / 100);
			return new Point((int) (x * scale), (int) (y * scale));
		}
		else
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
			int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);
			return new Point(x, y);
		}
	}
}
