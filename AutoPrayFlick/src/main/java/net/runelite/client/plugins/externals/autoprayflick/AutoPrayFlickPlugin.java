/*
 * Copyright (c) 2018, DennisDeV <https://github.com/DevDennis>
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
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
 *
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
package net.runelite.client.plugins.externals.autoprayflick;

import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.VarClientStr;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseListener;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.utils.ExtUtils;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "Auto Pray Flick",
	type = PluginType.UTILITY
)
@Slf4j
@SuppressWarnings("unused")
@PluginDependency(ExtUtils.class)
public class AutoPrayFlickPlugin extends Plugin implements KeyListener, MouseListener
{
	private static final int[] NMZ_MAP_REGION = {9033};

	@Inject
	private Client client;

	@Inject
	private KeyManager keyManager;

	@Inject
	private MouseManager mouseManager;

	@Inject
	private EventBus eventBus;

	@Inject
	private AutoPrayFlickConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AutoPrayFlickOverlay autoPrayFlickOverlay;

	@Inject
	private ExtUtils extUtils;

	private Rectangle bounds;
	private boolean held;
	private boolean firstFlick;
	@Getter(AccessLevel.PACKAGE)
	private boolean toggleFlick;

	@Provides
	AutoPrayFlickConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoPrayFlickConfig.class);
	}

	@Override
	protected void startUp()
	{
		keyManager.registerKeyListener(this);
		mouseManager.registerMouseListener(this);
		overlayManager.add(autoPrayFlickOverlay);
	}

	@Override
	protected void shutDown()
	{
		keyManager.unregisterKeyListener(this);
		overlayManager.remove(autoPrayFlickOverlay);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (config.onlyInNmz() && !isInNightmareZone())
		{
			return;
		}

		Widget widget = client.getWidget(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB);

		if (widget != null)
		{
			bounds = widget.getBounds();
		}

		if (toggleFlick && config.clicks())
		{
			int p = 0;
			for (Prayer prayer : Prayer.values())
			{
				if (!client.isPrayerActive(prayer))
				{
					p++;
				}
			}
			if (p == 29 && !firstFlick)
			{
				singleClick();
				return;
			}
			doubleClick();
			if (firstFlick)
			{
				firstFlick = false;
			}
		}
		if (toggleFlick && !config.clicks())
		{
			singleClick();
		}
	}

	@Subscribe
	public void onFocusChanged(FocusChanged event)
	{
		if (!event.isFocused() && !config.mouseEvents())
		{
			toggleFlick = false;
			firstFlick = false;
		}
	}

	private void doubleClick()
	{
		delayFirstClick();
		delaySecondClick();
	}

	private void singleClick()
	{
		delayFirstClick();
	}

	private void delayFirstClick()
	{
		final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.schedule(this::simLeftClick, randomDelay(1, 9), TimeUnit.MILLISECONDS);
		service.shutdown();
	}

	private void delaySecondClick()
	{
		final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.schedule(this::simLeftClick, randomDelay(90, 100), TimeUnit.MILLISECONDS);
		service.shutdown();
	}

	private void simLeftClick()
	{
		if (config.mouseEvents())
		{
			extUtils.click(bounds);
			return;
		}

		try
		{
			Robot leftClk = new Robot();
			leftClk.mousePress(BUTTON1_DOWN_MASK);
			leftClk.mouseRelease(BUTTON1_DOWN_MASK);
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (config.useMouse())
		{
			return;
		}

		final int keycode = config.hotkey2().getKeyCode();

		if (e.getKeyCode() == keycode && toggleFlick && !held)
		{
			toggleFlick = false;
			firstFlick = true;
		}
		else if (e.getKeyCode() == keycode && !toggleFlick && !held)
		{
			toggleFlick = true;
			firstFlick = true;
		}
		if (config.holdMode())
		{
			held = true;
			firstFlick = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (config.useMouse())
		{
			return;
		}
		if (config.holdMode())
		{
			toggleFlick = false;
			held = false;
			firstFlick = false;
		}
		if (config.clearChat() && config.hotkey2().matches(e))
		{
			String chat = client.getVar(VarClientStr.CHATBOX_TYPED_TEXT);
			if (chat.endsWith(String.valueOf(e.getKeyChar())))
			{
				chat = chat.substring(0, chat.length() - 1);
				client.setVar(VarClientStr.CHATBOX_TYPED_TEXT, chat);
			}
		}
	}

	@Override
	public MouseEvent mouseClicked(MouseEvent e)
	{
		return e;
	}

	@Override
	public MouseEvent mousePressed(MouseEvent e)
	{
		if (!config.useMouse())
		{
			return e;
		}
		if (e.getButton() == config.mouseButton() && toggleFlick && !held)
		{
			toggleFlick = false;
			firstFlick = false;
		}
		else if (e.getButton() == config.mouseButton() && !toggleFlick && !held)
		{
			toggleFlick = true;
			firstFlick = true;
		}
		if (config.holdMode() && e.getButton() == config.mouseButton())
		{
			held = true;
			firstFlick = true;
		}
		return e;
	}

	@Override
	public MouseEvent mouseReleased(MouseEvent e)
	{
		if (config.holdMode() && e.getButton() == config.mouseButton())
		{
			toggleFlick = false;
			firstFlick = false;
			held = false;
		}
		return e;
	}

	@Override
	public MouseEvent mouseEntered(MouseEvent e)
	{
		return e;
	}

	@Override
	public MouseEvent mouseExited(MouseEvent e)
	{
		return e;
	}

	@Override
	public MouseEvent mouseDragged(MouseEvent e)
	{
		return e;
	}

	@Override
	public MouseEvent mouseMoved(MouseEvent e)
	{
		return e;
	}

	private boolean isInNightmareZone()
	{
		return Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION);
	}

	private static int randomDelay(int min, int max)
	{
		Random rand = new Random();
		int n = rand.nextInt(max) + 1;
		if (n < min)
		{
			n += min;
		}
		return n;
	}
}
