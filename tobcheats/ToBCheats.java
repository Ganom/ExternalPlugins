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
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Projectile;
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
import net.runelite.client.flexo.FlexoMouse;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "ToB Cheats",
	description = "ToB Cheats",
	tags = {"ToB", "theatre", "blood", "cheats", "flexo"},
	enabledByDefault = false,
	type = PluginType.EXTERNAL
)

public class ToBCheats extends Plugin
{
	@Getter(AccessLevel.PACKAGE)
	private NPC Maiden;
	@Getter(AccessLevel.PACKAGE)
	private NPC Nylo;
	private boolean lock1;
	private boolean lock2;
	private boolean lock3;
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
	private Flexo flexo;
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private double scalingfactor;
	private boolean magepray;
	private boolean rangepray;
	private boolean nylolockRa;
	private boolean nylolockMe;
	private boolean nylolockMa;
	private int ticks;

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
		scalingfactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
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
			executeMagePray();
			magepray = false;
		}
		if (rangepray)
		{
			executeRangedPray();
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
						executeAugury();
					}
					executeMage();
					executeBarrage();
					lock1 = true;
				}
				if (maidenswap50 && !lock2)
				{
					if (!client.isPrayerActive(Prayer.AUGURY))
					{
						executeAugury();
					}
					executeMage();
					executeBarrage();
					lock2 = true;
				}
				if (maidenswap30 && !lock3)
				{
					if (!client.isPrayerActive(Prayer.AUGURY))
					{
						executeAugury();
					}
					executeMage();
					executeBarrage();
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
						executeRangedPray();
					}
					if (!client.isPrayerActive(Prayer.RIGOUR))
					{
						executeRigour();
					}
					System.out.println("Attempting Range Swap");
					executeRange();
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
						executeMagePray();
					}
					if (!client.isPrayerActive(Prayer.AUGURY))
					{
						executeAugury();
					}
					System.out.println("Attempting Mage Swap");
					executeMage();
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
						executeMeleePray();
					}
					if (!client.isPrayerActive(Prayer.PIETY))
					{
						executePiety();
					}
					System.out.println("Attempting Melee Swap");
					executeMelee();
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

	private void executeBarrage()
	{
/*		Widget iceBarrage = client.getWidget(WidgetInfo.SPELL_ICE_BARRAGE);
		if (client.getWidget(WidgetInfo.SPELL_ICE_BARRAGE).isHidden())
		{
			executorService.submit(() ->
				flexo.keyPress(tabUtils.getTabHotkey(Tab.MAGIC)));
		}
		clickSpell(iceBarrage);
		executorService.submit(() ->
			flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY)));*/
	}

	private void executeAugury()
	{
		Widget prayerAugury = client.getWidget(WidgetInfo.PRAYER_AUGURY);
		clickPrayer(prayerAugury);
	}

	private void executeRigour()
	{
		Widget prayerRigour = client.getWidget(WidgetInfo.PRAYER_RIGOUR);
		clickPrayer(prayerRigour);
	}

	private void executePiety()
	{
		Widget prayerPiety = client.getWidget(WidgetInfo.PRAYER_PIETY);
		clickPrayer(prayerPiety);
	}

	private void executeMagePray()
	{
		Widget prayerMage = client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MAGIC);
		clickPrayer(prayerMage);
	}

	private void executeRangedPray()
	{
		Widget prayerRanged = client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MISSILES);
		clickPrayer(prayerRanged);
	}

	private void executeMeleePray()
	{
		Widget prayerMelee = client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE);
		clickPrayer(prayerMelee);
	}

	private void executeMelee()
	{
		executorService.submit(() -> {
			if (getMelee().isEmpty())
			{
				return;
			}
			for (WidgetItem Melee : getMelee())
			{
				clickItem(Melee);
			}
		});
	}

	private void executeRange()
	{
		executorService.submit(() -> {
			if (getRange().isEmpty())
			{
				return;
			}
			for (WidgetItem Range : getRange())
			{
				clickItem(Range);
			}
		});
	}

	private void executeMage()
	{
		executorService.submit(() -> {
			if (getMage().isEmpty())
			{
				return;
			}
			for (WidgetItem mage : getMage())
			{
				clickItem(mage);
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
			Rectangle bounds = item.getCanvasBounds();
			Point cp = getClickPoint(bounds);
			System.out.println("Click Point Generated");
			if (cp.getX() >= 1)
			{
				executorService.submit(() -> {
					if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
					{
						return;
					}
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
						case MENUACTIONS:
							client.invokeMenuAction(item.getIndex(), 9764864, 34, item.getId(), "", "", item.getCanvasLocation().getX(), item.getCanvasLocation().getY());
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
				});
			}
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
			Rectangle bounds = prayer.getBounds();
			Point cp = getClickPoint(bounds);
			if (cp.getX() >= 1)
			{
				executorService.submit(() -> {
					if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE).isHidden())
					{
						return;
					}
					switch (config.actionType())
					{
						case FLEXO:
							flexo.mouseMove(cp.getX(), cp.getY());
							flexo.mousePressAndRelease(1);
							flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
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
							flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
							break;
						case MENUACTIONS:
							client.invokeMenuAction(-1, prayer.getId(), 57, 1, "Activate", prayer.getName(),
								prayer.getCanvasLocation().getX(), prayer.getCanvasLocation().getY());
							try
							{
								Thread.sleep(getMillis());
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
							break;
					}
				});
			}
		}
	}

	private void clickSpell(Widget spell)
	{
		if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE).isHidden())
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.PRAYER));
		}
		if (spell != null)
		{
			Rectangle bounds = spell.getBounds();
			Point cp = getClickPoint(bounds);
			if (cp.getX() >= 1)
			{
				executorService.submit(() -> {
					if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE).isHidden())
					{
						return;
					}
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
						case MENUACTIONS:
							client.invokeMenuAction(-1, spell.getId(), 25, 0, "Cast", spell.getName(),
								spell.getCanvasLocation().getX(), spell.getCanvasLocation().getY());
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
				});
			}
		}
	}

	private void clickActor(Actor actor)
	{
		if (actor != null)
		{
			if (actor.getConvexHull() != null)
			{
				Rectangle bounds = actor.getConvexHull().getBounds();
				Point cp = getClickPoint(bounds);
				if (cp.getX() >= 1)
				{
					executorService.submit(() -> {
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
							case MENUACTIONS:
								if (actor instanceof Player)
								{
									Player target = (Player) actor;
									client.invokeMenuAction(0, 0, 45, target.getPlayerId(), "Attack", "<col=ffffff>" + target.getName() + "<col=ff00>  (level-" + target.getCombatLevel() + ")", 0, 0);
									try
									{
										Thread.sleep(getMillis());
									}
									catch (InterruptedException e)
									{
										e.printStackTrace();
									}
								}
								if (actor instanceof NPC)
								{
									NPC target = (NPC) actor;
									client.invokeMenuAction(0, 0, 10, target.getId(), "Attack", "<col=ffffff>" + target.getName() + "<col=ff00>  (level-" + target.getCombatLevel() + ")", 0, 0);
									try
									{
										Thread.sleep(getMillis());
									}
									catch (InterruptedException e)
									{
										e.printStackTrace();
									}
								}
								break;
						}
					});
				}
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
