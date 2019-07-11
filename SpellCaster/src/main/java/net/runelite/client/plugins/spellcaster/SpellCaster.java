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

package net.runelite.client.plugins.spellcaster;

import com.google.inject.Provides;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Varbits;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.spellcaster.utils.Tab;
import net.runelite.client.plugins.spellcaster.utils.TabUtils;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;

@PluginDescriptor(
	name = "Spell Caster",
	description = "Cast spells with hotkeys",
	tags = {"spell", "pvp", "cast", "hotkey"},
	type = PluginType.EXTERNAL
)

public class SpellCaster extends Plugin implements KeyListener
{
	@Inject
	private Client client;
	@Inject
	private KeyManager keyManager;
	@Inject
	private SpellCasterConfig config;
	@Inject
	private ConfigManager configManager;
	@Inject
	private TabUtils tabUtils;
	private BlockingQueue queue = new ArrayBlockingQueue(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());
	private Flexo flexo;

	@Provides
	SpellCasterConfig getConfig(ConfigManager manager)
	{
		return (SpellCasterConfig) manager.getConfig(SpellCasterConfig.class);
	}

	protected void startUp()
	{
		keyManager.registerKeyListener(this);
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

	protected void shutDown()
	{
		keyManager.unregisterKeyListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == config.hotkeyAncientOne().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.ancientOne()));
		}
		if (e.getKeyCode() == config.hotkeyAncientTwo().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.ancientTwo()));
		}
		if (e.getKeyCode() == config.hotkeyAncientThree().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.ancientThree()));
		}
		if (e.getKeyCode() == config.hotkeyAncientFour().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.ancientFour()));
		}
		if (e.getKeyCode() == config.hotkeyAncientFive().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.ancientFive()));
		}
		if (e.getKeyCode() == config.hotkeyStandardOne().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.standardOne()));
		}
		if (e.getKeyCode() == config.hotkeyStandardTwo().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.standardTwo()));
		}
		if (e.getKeyCode() == config.hotkeyStandardThree().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.standardThree()));
		}
		if (e.getKeyCode() == config.hotkeyStandardFour().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.standardFour()));
		}
		if (e.getKeyCode() == config.hotkeyStandardFive().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.standardFive()));
		}
		if (e.getKeyCode() == config.hotkeyLunarOne().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.lunarOne()));
		}
		if (e.getKeyCode() == config.hotkeyLunarTwo().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.lunarTwo()));
		}
		if (e.getKeyCode() == config.hotkeyLunarThree().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.lunarThree()));
		}
		if (e.getKeyCode() == config.hotkeyLunarFour().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.lunarFour()));
		}
		if (e.getKeyCode() == config.hotkeyLunarFive().getKeyCode())
		{
			executorService.submit(() -> clickSpell(config.lunarFive()));
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}


	private void clickSpell(SpellCasterConfig.Standards spell)
	{
		if (client.getVar(Varbits.SPELLBOOK_ID) != 0)
		{
			return;
		}
		Widget widget = client.getWidget(spell.getSpell());
		if (widget != null)
		{
				if (widget.isHidden())
				{
					flexo.keyPress(tabUtils.getTabHotkey(Tab.MAGIC));
				}
				handleSwitch(widget.getBounds());
				if (config.backToInventory())
				{
					flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
				}
		}
	}

	private void clickSpell(SpellCasterConfig.Ancients spell)
	{
		if (client.getVar(Varbits.SPELLBOOK_ID) != 1)
		{
			return;
		}
		Widget widget = client.getWidget(spell.getSpell());
		if (widget != null)
		{
			if (widget.isHidden())
			{
				flexo.keyPress(tabUtils.getTabHotkey(Tab.MAGIC));
			}
			handleSwitch(widget.getBounds());
			if (config.backToInventory())
			{
				flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
			}
		}
	}

	private void clickSpell(SpellCasterConfig.Lunars spell)
	{
		if (client.getVar(Varbits.SPELLBOOK_ID) != 2)
		{
			return;
		}
		Widget widget = client.getWidget(spell.getSpell());
		if (widget != null)
		{
			if (widget.isHidden())
			{
				flexo.keyPress(tabUtils.getTabHotkey(Tab.MAGIC));
			}
			handleSwitch(widget.getBounds());
			if (config.backToInventory())
			{
				flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
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
		else
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

		int rand = (Math.random() <= 0.5) ? 1 : 2;
		int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
		int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);

		double scale = 1 + (scalingfactor / 100);

		if (client.isStretchedEnabled())
		{
			return new Point((int) (x * scale), (int) (y * scale));
		}

		return new Point(x, y);
	}
}
