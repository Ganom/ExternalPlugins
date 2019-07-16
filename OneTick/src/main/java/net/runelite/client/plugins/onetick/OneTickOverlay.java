package net.runelite.client.plugins.onetick;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.graphics.ModelOutlineRenderer;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

@Singleton
public class OneTickOverlay extends Overlay
{
	private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

	private final Client client;
	private final OneTick plugin;
	private final ModelOutlineRenderer modelOutliner;
	private final ItemManager itemManager;

	@Inject
	private OneTickOverlay(final Client client, final OneTick plugin, final ModelOutlineRenderer modelOutliner, final ItemManager itemManager)
	{
		this.client = client;
		this.plugin = plugin;
		this.modelOutliner = modelOutliner;
		this.itemManager = itemManager;
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.LOW);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		for (TileObject object : plugin.getObjects())
		{
			if (object.getPlane() != client.getPlane())
			{
				continue;
			}

			modelOutliner.drawOutline(object, 4, Color.CYAN, TRANSPARENT);
		}
		if (plugin.getKaram() != null)
		{
			final WidgetItem karam = plugin.getKaram();

			final BufferedImage outline = itemManager.getItemOutline(karam.getId(), karam.getQuantity(), Color.CYAN);
			graphics.drawImage(outline, (int) karam.getCanvasBounds().getX(), (int) karam.getCanvasBounds().getY(), null);
		}
		return null;
	}
}
