package net.runelite.client.plugins.externals.leaguetp;

import com.google.common.base.Strings;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ImageUtil;

@Slf4j
public class SelectorOverlay extends OverlayPanel
{
	private static final int BORDER_SIZE = 2;
	private static final int GAP = 2;
	private static final Rectangle BORDER = new Rectangle(2, 1, 4, 0);

	private final PanelComponent imagePanel = new PanelComponent();
	private final Client client;
	private final LeagueTeleportConfig config;
	private final SpriteManager spriteManager;
	private final TooltipManager tooltipManager;
	private final Map<Teleport, BufferedImage> imageMap;

	@Getter
	private TeleportComponent hoveredComponent = null;
	private boolean refreshed;

	public SelectorOverlay(Plugin plugin, Client client, LeagueTeleportConfig config, SpriteManager spriteManager, TooltipManager tooltipManager)
	{
		super(plugin);
		this.client = client;
		this.config = config;
		this.spriteManager = spriteManager;
		this.tooltipManager = tooltipManager;
		this.imageMap = new LinkedHashMap<>();
		panelComponent.setBorder(new Rectangle(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
		panelComponent.setGap(new Point(0, GAP));
		panelComponent.setBackgroundColor(null);
		imagePanel.setBorder(BORDER);
		imagePanel.setBackgroundColor(null);
	}

	public void refresh(List<Teleport> list)
	{
		log.debug("Refreshing");
		imageMap.clear();
		for (Teleport value : list)
		{
			int id;
			switch (config.style())
			{
				case BADGES:
					id = value.getBannerSpriteId();
					break;
				case TITLES:
					id = value.getTextSpriteId();
					break;
				default:
					continue;
			}
			BufferedImage image = spriteManager.getSprite(id, 0);
			if (image == null)
			{
				continue;
			}
			int s = config.scale();
			int scaledWidth = (int) ((double) image.getWidth() * (s / 100));
			int scaledHeight = (int) ((double) image.getHeight() * (s / 100));
			image = ImageUtil.resizeImage(image, scaledWidth, scaledHeight);
			imageMap.put(value, image);
		}
		refreshed = true;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (imageMap.isEmpty())
		{
			return super.render(graphics);
		}

		if (refreshed)
		{
			log.debug("Refreshed");
			refreshed = false;
			imagePanel.setOrientation(config.align());
			imagePanel.getChildren().clear();
			for (Map.Entry<Teleport, BufferedImage> entry : imageMap.entrySet())
			{
				imagePanel.getChildren().add(new TeleportComponent(entry.getKey(), entry.getValue()));
			}
		}

		Point mouse = new Point(client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY());
		boolean hovering = false;

		for (LayoutableRenderableEntity child : imagePanel.getChildren())
		{
			TeleportComponent c = (TeleportComponent) child;
			Rectangle r = new Rectangle(c.getBounds());
			r.translate(getBounds().x, getBounds().y);
			if (r.contains(mouse))
			{
				if (config.style() == Style.BADGES &&
					!Strings.isNullOrEmpty(c.getTeleport().getTooltip()))
				{
					tooltipManager.add(new Tooltip("Teleport to " + c.getTeleport().getTooltip()));
				}
				hoveredComponent = c;
				hovering = true;
				c.setHovering(true);
			}
			else
			{
				c.setHovering(false);
			}
		}

		if (!hovering)
		{
			hoveredComponent = null;
		}

		panelComponent.getChildren().add(imagePanel);
		return super.render(graphics);
	}
}
