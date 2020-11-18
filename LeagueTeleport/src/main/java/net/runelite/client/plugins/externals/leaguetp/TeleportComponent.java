package net.runelite.client.plugins.externals.leaguetp;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

@Setter
@Getter
@RequiredArgsConstructor
public class TeleportComponent implements LayoutableRenderableEntity
{
	private final Teleport teleport;
	private final BufferedImage image;
	private final Rectangle bounds = new Rectangle();

	private Point preferredLocation = new Point();
	private boolean hovering;
	private int xOffset = 0;
	private int yOffset = 0;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (image == null)
		{
			return null;
		}

		preferredLocation.x += xOffset;
		preferredLocation.y += yOffset;

		graphics.drawImage(image, hovering ? darken() : null, preferredLocation.x, preferredLocation.y);
		final Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
		bounds.setLocation(preferredLocation);
		bounds.setSize(dimension);
		setPreferredSize(dimension);
		return dimension;
	}

	private RescaleOp darken()
	{
		return new RescaleOp(new float[]{0.5f, 0.5f, 0.5f, 1f}, new float[]{0, 0, 0, 0}, null);
	}

	@Override
	public void setPreferredSize(Dimension dimension)
	{
		// Just use image dimensions for now
	}

	public void translate(int x, int y)
	{
		xOffset = x;
		yOffset = y;
	}
}
