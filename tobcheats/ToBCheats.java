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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
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
	private final List<Projectile> Sotetseg_MageProjectiles = new ArrayList<>();
	@Getter(AccessLevel.PACKAGE)
	private final List<Projectile> Sotetseg_RangeProjectiles = new ArrayList<>();
	@Getter(AccessLevel.PACKAGE)
	private NPC Maiden;
	@Getter(AccessLevel.PACKAGE)
	private NPC Sote;
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
	private TabUtils tabutils;
	private Flexo flexer;
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

/*	private List<WidgetItem> getEat()
	{
		String str = config.food();
		int[] food = Arrays.stream(str.substring(1, str.length() - 1).split(","))
			.map(String::trim).mapToInt(Integer::parseInt).toArray();
		return getItems(food);
	}*/

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
		;
		return getItems(range);
	}

	private List<WidgetItem> getMelee()
	{
		String str = config.melee();
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
		/*		keyManager.registerKeyListener(hotkeyListener);*/
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
			flexer = null;
			try
			{
				flexer = new Flexo();
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
		/*		keyManager.unregisterKeyListener(hotkeyListener);*/
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
		switch (npc.getId())
		{
			case NpcID.THE_MAIDEN_OF_SUGADINTI:
				Maiden = null;
				break;
			case NpcID.SOTETSEG:
			case NpcID.SOTETSEG_8388:
				Active = false;
				break;
		}
	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved event)
	{
		Projectile projectile = event.getProjectile();

		if (projectile.getId() == 1606)
		{
			Sotetseg_MageProjectiles.add(projectile);
		}
		if (projectile.getId() == 1607)
		{
			Sotetseg_RangeProjectiles.add(projectile);
		}
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

/*	@Subscribe
	public void onClientTick(ClientTick event)
	{
		if (config.sote())
		{
			for (Projectile projectile : Sotetseg_RangeProjectiles)
			{
				if (projectile.getInteracting() != null)
				{
					if (projectile.getInteracting() == client.getLocalPlayer())
					{
						if (projectile.getRemainingCycles() == 1)
						{
							if (tickeat)
							{
								executeEat();
							}
						}
						if (projectile.getRemainingCycles() <= 20 && projectile.getRemainingCycles() >= 5)
						{
							if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES) && client.getBoostedSkillLevel(Skill.PRAYER) >= 1)
							{
								magepray = true;
							}
						}
					}
				}
			}

			for (Projectile projectile : Sotetseg_MageProjectiles)
			{
				if (projectile.getInteracting() != null)
				{
					if (projectile.getInteracting() == client.getLocalPlayer())
					{
						if (projectile.getRemainingCycles() == 1)
						{
							if (tickeat)
							{
								executeEat();
							}
						}
						if (projectile.getRemainingCycles() <= 20 && projectile.getRemainingCycles() >= 5)
						{
							if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC) && client.getBoostedSkillLevel(Skill.PRAYER) >= 1)
							{
								magepray = true;
							}
						}
					}
				}
			}

			if (!getSotetseg_MageProjectiles().isEmpty())
			{
				for (Iterator<Projectile> it = Sotetseg_MageProjectiles.iterator(); it.hasNext(); )
				{
					Projectile projectile = it.next();
					if (projectile.getRemainingCycles() < 1)
					{
						it.remove();
					}
				}
			}

			if (!getSotetseg_RangeProjectiles().isEmpty())
			{
				for (Iterator<Projectile> it = Sotetseg_RangeProjectiles.iterator(); it.hasNext(); )
				{
					Projectile projectile = it.next();
					if (projectile.getRemainingCycles() < 1)
					{
						it.remove();
					}
				}
			}
		}
	}*/

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
				int timer = 0;
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
					nylolockRa = false;
					nylolockMe = true;
					nylolockMa = false;
				}
			}
		}
	}

	private void executeBarrage()
	{
		//
	}

	private void executeAugury()
	{
		Widget prayerAugury = client.getWidget(WidgetInfo.PRAYER_AUGURY);
		if (prayerAugury == null)
		{
			return;
		}
		if (client.getWidget(WidgetInfo.PRAYER_AUGURY).isHidden())
		{
			executorService.submit(() -> {
				flexer.keyPress(tabutils.getTabHotkey(Tab.PRAYER));
			});
		}
		Rectangle bounds = FlexoMouse.getClickArea(prayerAugury.getBounds());
		Point cp = getClickPoint(bounds);
		executorService.submit(() -> {
			if (bounds.getX() >= 1)
			{
				flexer.mouseMove(cp.getX(), cp.getY());
				flexer.mousePressAndRelease(1);
				flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
			}
		});
	}

	private void executeRigour()
	{
		Widget prayerRigour = client.getWidget(WidgetInfo.PRAYER_RIGOUR);
		if (prayerRigour == null)
		{
			return;
		}
		if (client.getWidget(WidgetInfo.PRAYER_RIGOUR).isHidden())
		{
			executorService.submit(() -> {
				flexer.keyPress(tabutils.getTabHotkey(Tab.PRAYER));
			});
		}
		Rectangle bounds = FlexoMouse.getClickArea(prayerRigour.getBounds());
		Point cp = getClickPoint(bounds);
		executorService.submit(() -> {
			if (bounds.getX() >= 1)
			{
				flexer.mouseMove(cp.getX(), cp.getY());
				flexer.mousePressAndRelease(1);
				flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
			}
		});
	}

	private void executePiety()
	{
		Widget prayerPiety = client.getWidget(WidgetInfo.PRAYER_PIETY);
		if (prayerPiety == null)
		{
			return;
		}
		if (client.getWidget(WidgetInfo.PRAYER_PIETY).isHidden())
		{
			executorService.submit(() -> {
				flexer.keyPress(tabutils.getTabHotkey(Tab.PRAYER));
			});
		}
		Rectangle bounds = FlexoMouse.getClickArea(prayerPiety.getBounds());
		Point cp = getClickPoint(bounds);
		executorService.submit(() -> {
			if (bounds.getX() >= 1)
			{
				flexer.mouseMove(cp.getX(), cp.getY());
				flexer.mousePressAndRelease(1);
				flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
			}
		});
	}

	private void executeMagePray()
	{
		Widget prayerMage = client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MAGIC);
		if (prayerMage == null)
		{
			return;
		}
		if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MAGIC).isHidden())
		{
			executorService.submit(() -> {
				flexer.keyPress(tabutils.getTabHotkey(Tab.PRAYER));
			});
		}
		Rectangle bounds = FlexoMouse.getClickArea(prayerMage.getBounds());
		Point cp = getClickPoint(bounds);
		executorService.submit(() -> {
			if (bounds.getX() >= 1)
			{
				flexer.mouseMove(cp.getX(), cp.getY());
				flexer.mousePressAndRelease(1);
				flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
			}
		});
	}

	private void executeRangedPray()
	{
		Widget prayerRanged = client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MISSILES);
		if (prayerRanged == null)
		{
			return;
		}
		if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MISSILES).isHidden())
		{
			executorService.submit(() -> {
				flexer.keyPress(tabutils.getTabHotkey(Tab.PRAYER));
			});
		}
		Rectangle bounds = FlexoMouse.getClickArea(prayerRanged.getBounds());
		Point cp = getClickPoint(bounds);
		executorService.submit(() -> {
			if (bounds.getX() >= 1)
			{
				flexer.mouseMove(cp.getX(), cp.getY());
				flexer.mousePressAndRelease(1);
				flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
			}
		});
	}

	private void executeMeleePray()
	{
		Widget prayerMelee = client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE);
		if (prayerMelee == null)
		{
			return;
		}
		if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE).isHidden())
		{
			executorService.submit(() -> {
				flexer.keyPress(tabutils.getTabHotkey(Tab.PRAYER));
			});
		}
		Rectangle bounds = FlexoMouse.getClickArea(prayerMelee.getBounds());
		Point cp = getClickPoint(bounds);
		executorService.submit(() -> {
			if (bounds.getX() >= 1)
			{
				flexer.mouseMove(cp.getX(), cp.getY());
				flexer.mousePressAndRelease(1);
				flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
			}
		});
	}

/*	private void executeEat()
	{
		executorService.submit(() -> {
			Rectangle bounds = new Rectangle(0, 0, 0, 0);
			Point cp = new Point(0, 0);
			if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
			{
				Widget equipTab = client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_TAB);
				if (equipTab == null)
				{
					equipTab = client.getWidget(WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB);
					if (equipTab == null)
					{
						return;
					}
				}
				Rectangle boundsTab = equipTab.getBounds();
				Point cptab = getClickPoint(boundsTab);
				if (boundsTab.getX() >= 1)
				{
					flexer.mouseMove(cptab.getX(), cptab.getY());
					flexer.mousePressAndRelease(1);
				}
			}
			for (WidgetItem eat : getEat())
			{
				if (getEat().isEmpty())
				{
					break;
				}
				bounds = FlexoMouse.getClickArea(eat.getCanvasBounds());
				cp = getClickPoint(bounds);
			}
			if (bounds.getX() >= 1)
			{
				flexer.mouseMove(cp.getX(), cp.getY());
				flexer.mousePressAndRelease(1);
			}
		});
	}*/

	private void executeMelee()
	{
		executorService.submit(() -> {
			for (WidgetItem melee : getMelee())
			{
				if (getMelee().isEmpty())
				{
					break;
				}
				if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
				Rectangle bounds = FlexoMouse.getClickArea(melee.getCanvasBounds());
				Point cp = getClickPoint(bounds);
				if (bounds.getX() >= 1)
				{
					flexer.mouseMove(cp.getX(), cp.getY());
					flexer.mousePressAndRelease(1);
				}
			}
		});
	}

	private void executeRange()
	{
		executorService.submit(() -> {
			for (WidgetItem range : getRange())
			{
				if (getRange().isEmpty())
				{
					break;
				}
				if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
				Rectangle bounds = FlexoMouse.getClickArea(range.getCanvasBounds());
				Point cp = getClickPoint(bounds);
				if (bounds.getX() >= 1)
				{
					flexer.mouseMove(cp.getX(), cp.getY());
					flexer.mousePressAndRelease(1);
				}
			}
		});
	}

	private void executeMage()
	{
		executorService.submit(() -> {
			for (WidgetItem mage : getMage())
			{
				if (getMage().isEmpty())
				{
					break;
				}
				if (client.getWidget(WidgetInfo.INVENTORY).isHidden())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
				Rectangle bounds = FlexoMouse.getClickArea(mage.getCanvasBounds());
				Point cp = getClickPoint(bounds);
				if (bounds.getX() >= 1)
				{
					flexer.mouseMove(cp.getX(), cp.getY());
					flexer.mousePressAndRelease(1);
				}
			}
		});
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
