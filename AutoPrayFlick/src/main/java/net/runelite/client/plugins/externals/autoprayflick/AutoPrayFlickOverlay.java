/*
 * Copyright (c) 2018, Seth <Sethtroll3@gmail.com>
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class AutoPrayFlickOverlay extends Overlay
{

	private final Client client;
	private final AutoPrayFlickPlugin plugin;
	private final AutoPrayFlickConfig config;

	@Inject
	AutoPrayFlickOverlay(Client client, AutoPrayFlickPlugin plugin, AutoPrayFlickConfig config)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		this.client = client;
		this.plugin = plugin;
		this.config = config;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.display())
		{
			return null;
		}
		Widget xpOrb = client.getWidget(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB);
		if (xpOrb == null)
		{
			return null;
		}

		Rectangle2D bounds = xpOrb.getBounds().getBounds2D();
		if (bounds.getX() <= 0)
		{
			return null;
		}

		int orbInnerX = (int) (bounds.getX() + 24);//x pos of the inside of the prayer orb
		int orbInnerY = (int) (bounds.getY() - 1);//y pos of the inside of the prayer orb

		orbInnerX -= 3;
		orbInnerY -= 3;

		if (!plugin.isToggleFlick())
		{
			graphics.setColor(Color.red);
			//graphics.drawOval(66, 59, 30, 30);
		}
		else
		{
			graphics.setColor(Color.cyan);
			//graphics.drawOval(66, 59, 30, 30);
		}
		graphics.setStroke(new BasicStroke(2));
		graphics.drawOval(orbInnerX, orbInnerY, 30, 30);
		return null;
	}

}
