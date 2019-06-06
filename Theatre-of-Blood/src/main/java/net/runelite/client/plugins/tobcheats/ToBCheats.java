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

package net.runelite.client.plugins.tobcheats;

import com.google.inject.Provides;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Projectile;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.plugins.tobcheats.utils.Tab;
import net.runelite.client.plugins.tobcheats.utils.TabUtils;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "ToB Cheats",
	description = "ToB Cheats",
	tags = {"ToB", "theatre", "blood", "cheats", "flexo"},
	enabledByDefault = false,
	type = PluginType.EXTERNAL
)

@Slf4j
public class ToBCheats extends Plugin
{
	@Getter(AccessLevel.PACKAGE)
	private NPC Maiden;

	@Getter(AccessLevel.PACKAGE)
	private NPC Nylo;

	@Getter(AccessLevel.PACKAGE)
	private boolean tickeat;

	@Getter(AccessLevel.PACKAGE)
	private boolean Active;

	@Getter
	private Widget widget;

	@Inject
	private Client client;

	@Inject
	private ToBCheatsConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ToBCheatsOverlay overlay;

	@Inject
	private KeyManager keyManager;

	@Inject
	private ConfigManager externalConfig;

	@Inject
	private TabUtils tabUtils;

	@Inject
	private ItemManager itemManager;

	private Flexo flexo;

	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	private double scalingfactor;

	private boolean magepray;
	private boolean rangepray;
	private boolean nylolockRa;
	private boolean nylolockMe;
	private boolean nylolockMa;
	private boolean lock1;
	private boolean lock2;
	private boolean lock3;

	@Provides
	ToBCheatsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ToBCheatsConfig.class);
	}

	private List<WidgetItem> getMage()
	{
		int[] mage = Arrays.stream(config.mage().split(","))
			.map(String::trim).mapToInt(Integer::parseInt).toArray();
		return getItems(mage);
	}

	private List<WidgetItem> getRange()
	{
		int[] range = Arrays.stream(config.range().split(","))
			.map(String::trim).mapToInt(Integer::parseInt).toArray();
		return getItems(range);
	}

	private List<WidgetItem> getMelee()
	{
		int[] melee = Arrays.stream(config.melee().split(","))
			.map(String::trim).mapToInt(Integer::parseInt).toArray();
		return getItems(melee);
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
		overlayManager.add(overlay);
		scalingfactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
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
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		Nylo = null;
		Maiden = null;
		tickeat = false;
		Active = false;
		lock1 = false;
		lock2 = false;
		lock3 = false;
		nylolockMa = false;
		nylolockMe = false;
		nylolockRa = false;
		magepray = false;
		rangepray = false;
		flexo = null;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		switch (event.getGameState())
		{
			case HOPPING:
			case LOGIN_SCREEN:
			case LOADING:
			case LOGGED_IN:
				scalingfactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
				Nylo = null;
				Maiden = null;
				Active = false;
				lock1 = false;
				lock2 = false;
				lock3 = false;
				nylolockMa = false;
				nylolockMe = false;
				nylolockRa = false;
				Flexo.client = client;
				break;
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (config.testing())
		{
			if (event.getType() == ChatMessageType.PUBLICCHAT)
			{
				if (event.getMessage().toLowerCase().contains("1"))
				{
					executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MAGIC);
					executePrayer(WidgetInfo.PRAYER_AUGURY);
					executeItem(getMage());
				}
				if (event.getMessage().toLowerCase().contains("2"))
				{
					executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MISSILES);
					executePrayer(WidgetInfo.PRAYER_RIGOUR);
					executeItem(getRange());
				}
				if (event.getMessage().toLowerCase().contains("3"))
				{
					executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MELEE);
					executePrayer(WidgetInfo.PRAYER_PIETY);
					executeItem(getMelee());
				}
				if (event.getMessage().toLowerCase().contains("4"))
				{
					executeItem(getMage());
					executeSpell(WidgetInfo.SPELL_ICE_BARRAGE);
				}
			}
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();
		if (config.nyloSwapper())
		{
			switch (npc.getId())
			{
				case 8355:
					Nylo = npc;
				case 8356:
					Nylo = npc;
				case 8357:
					Nylo = npc;
					break;
			}
		}
		if (config.maidenSwapper())
		{
			if (npc.getId() == NpcID.THE_MAIDEN_OF_SUGADINTI)
			{
				Maiden = npc;
			}
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		NPC npc = npcDespawned.getNpc();
		switch (npc.getId()) //Keeping switch in case i need npc checks later.
		{
			case NpcID.THE_MAIDEN_OF_SUGADINTI:
				Maiden = null;
				break;
		}
	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved event)
	{
		Projectile projectile = event.getProjectile();
		if (config.Verzik())
		{
			if (projectile.getId() == 1594)
			{
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
				{
					magepray = true;
				}
			}
			if (projectile.getId() == 1593)
			{
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES))
				{
					rangepray = true;
				}
			}
			if (projectile.getId() == 1591)
			{
				if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
				{
					magepray = true;
				}
			}
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		scalingfactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
		if (magepray)
		{
			executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MAGIC);
			magepray = false;
		}
		if (rangepray)
		{
			executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MISSILES);
			rangepray = false;
		}
		if (config.maidenSwapper())
		{
			if (Maiden != null)
			{
				boolean maidenswap70 = false;
				boolean maidenswap50 = false;
				boolean maidenswap30 = false;
				int healthpercent = Maiden.getHealthRatio();
				if (healthpercent > -1)
				{
					double truehealth = (healthpercent * 0.625);
					if (truehealth <= 71 && truehealth >= 70)
					{
						maidenswap70 = true;
					}
					if (truehealth <= 51 && truehealth >= 50)
					{
						maidenswap50 = true;
					}
					if (truehealth <= 31 && truehealth >= 30)
					{
						maidenswap30 = true;
					}
				}
				if (maidenswap70 && !lock1)
				{
					if (!client.isPrayerActive(Prayer.AUGURY))
					{
						executePrayer(WidgetInfo.PRAYER_AUGURY);
					}
					executeItem(getMage());
					executeSpell(WidgetInfo.SPELL_ICE_BARRAGE);
					lock1 = true;
				}
				if (maidenswap50 && !lock2)
				{
					if (!client.isPrayerActive(Prayer.AUGURY))
					{
						executePrayer(WidgetInfo.PRAYER_AUGURY);
					}
					executeItem(getMage());
					executeSpell(WidgetInfo.SPELL_ICE_BARRAGE);
					lock2 = true;
				}
				if (maidenswap30 && !lock3)
				{
					if (!client.isPrayerActive(Prayer.AUGURY))
					{
						executePrayer(WidgetInfo.PRAYER_AUGURY);
					}
					executeItem(getMage());
					executeSpell(WidgetInfo.SPELL_ICE_BARRAGE);
					lock3 = true;
				}
			}
		}
		if (config.nyloSwapper())
		{
			if (Nylo != null)
			{
				int spec = Integer.parseInt(client.getWidget(160, 31).getText());
				if (Nylo.getId() == 8357 && !nylolockRa)
				{
					if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES))
					{
						executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MISSILES);
					}
					if (!client.isPrayerActive(Prayer.RIGOUR))
					{
						executePrayer(WidgetInfo.PRAYER_RIGOUR);
					}
					log.info("Attempting Range Swap");
					executeItem(getRange());
					if (config.autoAttack())
					{
						clickActor(getNylo());
					}
					nylolockRa = true;
					nylolockMe = false;
					nylolockMa = false;
				}
				if (Nylo.getId() == 8356 && !nylolockMa)
				{
					if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
					{
						executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MAGIC);
					}
					if (!client.isPrayerActive(Prayer.AUGURY))
					{
						executePrayer(WidgetInfo.PRAYER_AUGURY);
					}
					log.info("Attempting Mage Swap");
					executeItem(getMage());
					if (config.autoAttack())
					{
						clickActor(getNylo());
					}
					nylolockRa = false;
					nylolockMe = false;
					nylolockMa = true;
				}
				if (Nylo.getId() == 8355 && !nylolockMe && spec <= 29)
				{
					if (!client.isPrayerActive(Prayer.PROTECT_FROM_MELEE))
					{
						executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MELEE);
					}
					if (!client.isPrayerActive(Prayer.PIETY))
					{
						executePrayer(WidgetInfo.PRAYER_PIETY);
					}
					log.info("Attempting Melee Swap");
					executeItem(getMelee());
					if (config.autoAttack())
					{
						clickActor(getNylo());
					}
					nylolockRa = false;
					nylolockMe = true;
					nylolockMa = false;
				}
			}
		}
	}

	private void executePrayer(WidgetInfo prayer)
	{
		Widget pray = client.getWidget(prayer);
		executorService.submit(() -> clickPrayer(pray));
	}

	private void executeSpell(WidgetInfo spell)
	{
		Widget cast = client.getWidget(spell);
		executorService.submit(() -> clickSpell(cast));
	}

	private void executeItem(List<WidgetItem> list)
	{
		executorService.submit(() -> {
			if (list.isEmpty())
			{
				return;
			}
			for (WidgetItem items : list)
			{
				clickItem(items);
			}
		});
	}

	private void clickItem(WidgetItem item)
	{
		if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
		}
		if (item != null)
		{
			log.info("Grabbing Bounds and CP of: " + itemManager.getItemComposition(item.getId()).getName());
			handleSwitch(item.getCanvasBounds());
		}
	}

	private void clickPrayer(Widget prayer)
	{
		if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE).isHidden())
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.PRAYER));
		}
		if (prayer != null)
		{
			handleSwitch(prayer.getBounds());
			if (config.backToInventory())
			{
				flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
			}
		}
	}

	private void clickSpell(Widget spell)
	{
		if (client.getWidget(WidgetInfo.SPELL_ICE_BARRAGE).isHidden())
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.MAGIC));
		}
		if (spell != null)
		{
			handleSwitch(spell.getBounds());
			if (config.backToInventory())
			{
				flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
			}
		}
	}

	private void clickActor(Actor actor)
	{
		if (actor != null)
		{
			if (actor.getConvexHull() != null)
			{
				handleSwitch(actor.getConvexHull().getBounds());
			}
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

	private void leftClick(int x, int y)
	{
		if (client.isStretchedEnabled())
		{
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

	private void moveMouse(int x, int y)
	{
		MouseEvent mouseEntered = new MouseEvent(this.client.getCanvas(), 504, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseEntered);
		MouseEvent mouseExited = new MouseEvent(this.client.getCanvas(), 505, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseExited);
		MouseEvent mouseMoved = new MouseEvent(this.client.getCanvas(), 503, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseMoved);
	}

	private Point getClickPoint(Rectangle rect)
	{
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
