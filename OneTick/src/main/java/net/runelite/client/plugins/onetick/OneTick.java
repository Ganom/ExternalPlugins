/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.onetick;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.Point;
import net.runelite.api.VarClientStr;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.onetick.util.TabUtils;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.util.HotkeyListener;


@PluginDescriptor(
	name = "One Tick",
	description = "Flexo Sample Plugin",
	tags = {"flexo", "one", "tick", "bot"},
	type = PluginType.EXTERNAL
)
@Slf4j
public class OneTick extends Plugin
{
	private static final int CHAOS_ALTAR = 411;
	private static final int GILDED_ALTAR = 13197;
	@Inject
	private Client client;
	@Inject
	private TabUtils tabUtils;
	@Inject
	private OneTickConfig config;
	@Inject
	private KeyManager keyManager;
	@Inject
	private ConfigManager configManager;
	@Setter
	@Getter
	private GameObject altar;
	private Flexo flexo;
	private boolean oneTick;
	private BlockingQueue queue = new ArrayBlockingQueue(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 2, 10, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());

	private final HotkeyListener oneTickHotkey = new HotkeyListener(() -> config.oneTick())
	{
		@Override
		public void hotkeyPressed()
		{
			oneTick = !oneTick;
		}
	};

	@Provides
	OneTickConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OneTickConfig.class);
	}

	private List<WidgetItem> getItems(int... itemIds)
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

	@Override
	protected void startUp()
	{
		keyManager.registerKeyListener(oneTickHotkey);
		Flexo.client = client;
		executorService.submit(() -> {
			flexo = null;
			try
			{
				flexo = new Flexo();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		keyManager.unregisterKeyListener(oneTickHotkey);
		flexo = null;
		setAltar(null);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (!event.getGameState().equals(GameState.LOGGED_IN))
		{
			setAltar(null);
		}
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		final GameObject gameObject = event.getGameObject();

		switch (gameObject.getId())
		{
			case GILDED_ALTAR:
			case CHAOS_ALTAR:
				setAltar(gameObject);
				break;
		}
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		final GameObject gameObject = event.getGameObject();

		switch (gameObject.getId())
		{
			case GILDED_ALTAR:
			case CHAOS_ALTAR:
				setAltar(null);
				break;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (oneTick)
		{
			if (getItems(config.boneId()).size() <= 0)
			{
				oneTick = false;
			}

			executorService.submit(() -> {
				final String typedText = client.getVar(VarClientStr.CHATBOX_TYPED_TEXT);

				if (!Strings.isNullOrEmpty(typedText))
				{
					return;
				}

				if (getAltar() == null)
				{
					return;
				}

				WidgetItem next = getItems(config.boneId()).iterator().next();
				clickItem(next);
				clickGameObject(altar);
			});
		}
	}

	private void clickItem(WidgetItem item)
	{
		if (item != null)
		{
			Rectangle bounds = item.getCanvasBounds();

			if (item.getCanvasBounds() == null)
			{
				return;
			}

			handleSwitch(bounds);
		}
	}

	private void clickGameObject(GameObject gameObject)
	{
		if (gameObject != null)
		{
			Rectangle bounds = gameObject.getConvexHull().getBounds();

			if (gameObject.getConvexHull() == null)
			{
				return;
			}

			handleSwitch(bounds);
		}
	}

	private void handleSwitch(Rectangle rectangle)
	{
		Point cp = getClickPoint(rectangle);
		if (cp.getX() >= 1)
		{
			switch (config.actionType())
			{
				case FLEXO:
					flexo.mouseMove(cp.getX(), cp.getY());
					flexo.mousePressAndRelease(1);
					break;
				case MOUSEEVENTS:
					leftClick(cp.getX(), cp.getY());
					try
					{
						Thread.sleep(getMillis());
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					break;
			}
		}
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}

	private void moveMouse(int x, int y)
	{
		MouseEvent mouseEntered = new MouseEvent(this.client.getCanvas(), 504, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseEntered);
		MouseEvent mouseExited = new MouseEvent(this.client.getCanvas(), 505, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseExited);
		MouseEvent mouseMoved = new MouseEvent(this.client.getCanvas(), 503, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseMoved);
	}

	private void leftClick(int x, int y)
	{
		if (client.isStretchedEnabled())
		{
			double scalingfactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
			Point p = this.client.getMouseCanvasPosition();
			if (p.getX() != x || p.getY() != y)
			{
				this.moveMouse(x, y);
			}
			double scale = 1 + (scalingfactor / 100);

			MouseEvent mousePressed =
				new MouseEvent(this.client.getCanvas(), 501, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * scale), (int) (this.client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased =
				new MouseEvent(this.client.getCanvas(), 502, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * scale), (int) (this.client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked =
				new MouseEvent(this.client.getCanvas(), 500, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * scale), (int) (this.client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseClicked);
		}
		if (!client.isStretchedEnabled())
		{
			Point p = this.client.getMouseCanvasPosition();
			if (p.getX() != x || p.getY() != y)
			{
				this.moveMouse(x, y);
			}
			MouseEvent mousePressed = new MouseEvent(this.client.getCanvas(), 501, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased = new MouseEvent(this.client.getCanvas(), 502, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked = new MouseEvent(this.client.getCanvas(), 500, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseClicked);
		}
	}

	private Point getClickPoint(Rectangle rect)
	{
		double scalingfactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
		if (client.isStretchedEnabled())
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
			int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);
			double scale = 1 + (scalingfactor / 100);
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
