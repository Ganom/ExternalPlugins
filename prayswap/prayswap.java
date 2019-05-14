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

package net.runelite.client.plugins.prayswap;

import com.google.inject.Provides;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.flexo.FlexoMouse;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
	name = "PraySwap",
	description = "Use plugin in PvP situations for best results!!",
	tags = {"highlight", "pvp", "overlay", "players"},
	type = PluginType.EXTERNAL
)

public class prayswap extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ItemManager itemManager;
	@Inject
	private ChatMessageManager chatMessageManager;
	@Inject
	private KeyManager keyManager;
	@Inject
	private prayswapconfig config;
	@Inject
	private ClientThread clientThread;
	@Getter
	private Widget widget;
	@Inject
	private ConfigManager externalConfig;
	@Inject
	private TabUtils tabutils;
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private boolean runProtPray;
	private int scalingfactor;
	private Flexo flexer;


	private final HotkeyListener protmage = new HotkeyListener(() -> config.hotkeyMage())
	{
		@Override
		public void hotkeyPressed()
		{
			executeMagePray();
		}
	};
	private final HotkeyListener protrange = new HotkeyListener(() -> config.hotkeyRange())
	{
		@Override
		public void hotkeyPressed()
		{
			executeRangedPray();
		}
	};
	private final HotkeyListener protmelee = new HotkeyListener(() -> config.hotkeyMelee())
	{
		@Override
		public void hotkeyPressed()
		{
			executeMeleePray();
		}
	};

	private final HotkeyListener augury = new HotkeyListener(() -> config.hotkeyAugury())
	{
		@Override
		public void hotkeyPressed()
		{
			executeAugury();
		}
	};
	private final HotkeyListener rigour = new HotkeyListener(() -> config.hotkeyRigour())
	{
		@Override
		public void hotkeyPressed()
		{
			executeRigour();
		}
	};
	private final HotkeyListener piety = new HotkeyListener(() -> config.hotkeyPiety())
	{
		@Override
		public void hotkeyPressed()
		{
			executePiety();
		}
	};
	private final HotkeyListener smite = new HotkeyListener(() -> config.hotkeySmite())
	{
		@Override
		public void hotkeyPressed()
		{
			executeSmite();
		}
	};

	@Provides
	prayswapconfig getConfig(ConfigManager manager)
	{
		return (prayswapconfig) manager.getConfig(prayswapconfig.class);
	}

	protected void startUp()
	{
		keyManager.registerKeyListener(protmage);
		keyManager.registerKeyListener(protrange);
		keyManager.registerKeyListener(protmelee);
		keyManager.registerKeyListener(augury);
		keyManager.registerKeyListener(rigour);
		keyManager.registerKeyListener(piety);
		keyManager.registerKeyListener(smite);
		scalingfactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
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

	protected void shutDown()
	{
		keyManager.unregisterKeyListener(protmage);
		keyManager.unregisterKeyListener(protrange);
		keyManager.unregisterKeyListener(protmelee);
		keyManager.unregisterKeyListener(augury);
		keyManager.unregisterKeyListener(rigour);
		keyManager.unregisterKeyListener(piety);
		keyManager.unregisterKeyListener(smite);
	}

	private Point getClickPoint(Rectangle2D rect)
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

	@Subscribe
	public void onGameTick(GameTick event)
	{
		scalingfactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
		if (!client.isPrayerActive(Prayer.PROTECT_ITEM) && config.protectItem())
		{
			if (client.getBoostedSkillLevel(Skill.PRAYER) >= 1)
			{
				runProtPray = true;
			}
		}
		if (runProtPray)
		{
			executeProtect();
			runProtPray = false;
		}
	}

	private void executeProtect()
	{
		Widget prayerProtect = client.getWidget(WidgetInfo.PRAYER_PROTECT_ITEM);
		if (prayerProtect == null)
		{
			return;
		}
		if (client.getWidget(WidgetInfo.PRAYER_PROTECT_ITEM).isHidden())
		{
			executorService.submit(() -> {
				flexer.keyPress(tabutils.getTabHotkey(Tab.PRAYER));
			});
		}
		Rectangle bounds = FlexoMouse.getClickArea(prayerProtect.getBounds());
		Point cp = getClickPoint(bounds);
		executorService.submit(() -> {
			if (bounds.getX() >= 1 && bounds.getY() >= 1)
			{
				flexer.mouseMove(cp.getX(), cp.getY());
				flexer.mousePressAndRelease(1);
				if (config.backToInv())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
			}
		});
	}

	private void executeSmite()
	{
		Widget prayerSmite = client.getWidget(WidgetInfo.PRAYER_SMITE);
		if (prayerSmite == null)
		{
			return;
		}
		if (client.getWidget(WidgetInfo.PRAYER_SMITE).isHidden())
		{
			executorService.submit(() -> {
				flexer.keyPress(tabutils.getTabHotkey(Tab.PRAYER));
			});
		}
		Rectangle bounds = FlexoMouse.getClickArea(prayerSmite.getBounds());
		Point cp = getClickPoint(bounds);
		executorService.submit(() -> {
			if (bounds.getX() >= 1)
			{
				flexer.mouseMove(cp.getX(), cp.getY());
				flexer.mousePressAndRelease(1);
				if (config.backToInv())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
			}
		});
	}

	private void executeAugury()
	{
		Widget prayerAugury = client.getWidget(WidgetInfo.PRAYER_AUGURY);
		if (prayerAugury == null)
		{
			return;
		}
		if (config.lowLevel())
		{
			prayerAugury = client.getWidget(WidgetInfo.PRAYER_MYSTIC_MIGHT);
			if (prayerAugury == null)
			{
				return;
			}
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
				if (config.backToInv())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
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
		if (config.lowLevel())
		{
			prayerRigour = client.getWidget(WidgetInfo.PRAYER_EAGLE_EYE);
			if (prayerRigour == null)
			{
				return;
			}
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
				if (config.backToInv())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
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
		if (config.lowLevel())
		{
			prayerPiety = client.getWidget(WidgetInfo.PRAYER_ULTIMATE_STRENGTH);
			if (prayerPiety == null)
			{
				return;
			}
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
				if (config.backToInv())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
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
				if (config.backToInv())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
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
				if (config.backToInv())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
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
				if (config.backToInv())
				{
					flexer.keyPress(tabutils.getTabHotkey(Tab.INVENTORY));
				}
			}
		});
	}
}
