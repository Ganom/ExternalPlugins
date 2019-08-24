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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.spellcaster.utils.ExtUtils;
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
		if (client.getVar(Varbits.SPELLBOOK) != 0)
		{
			return;
		}
		handleWidgets(spell.getSpell());
	}

	private void clickSpell(SpellCasterConfig.Ancients spell)
	{
		if (client.getVar(Varbits.SPELLBOOK) != 1)
		{
			return;
		}
		handleWidgets(spell.getSpell());
	}

	private void clickSpell(SpellCasterConfig.Lunars spell)
	{
		if (client.getVar(Varbits.SPELLBOOK) != 2)
		{
			return;
		}
		handleWidgets(spell.getSpell());
	}

	private void handleWidgets(WidgetInfo spell)
	{
		Widget widget = client.getWidget(spell);

		if (widget != null)
		{
			if (widget.isHidden())
			{
				flexo.keyPress(TabUtils.getTabHotkey(Tab.MAGIC, client));
			}

			handleSwitch(widget.getBounds());

			if (config.backToInventory())
			{
				flexo.keyPress(TabUtils.getTabHotkey(Tab.INVENTORY, client));
			}
		}
	}

	private void handleSwitch(Rectangle rectangle)
	{
		ExtUtils.handleSwitch(rectangle, config.actionType(), flexo, client, configManager.getConfig(StretchedModeConfig.class).scalingFactor(), (int) getMillis());
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}
}
