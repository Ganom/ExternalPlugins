package net.runelite.client.plugins.externals.shayzien;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Objects;
import java.util.Set;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

@Singleton
public class InfirmaryOverlay extends Overlay
{
	private final Set<Integer> ids;
	private final Client client;
	private final ModelOutlineRenderer renderer;
	private final ShayzienAssist plugin;

	@Inject
	InfirmaryOverlay(Client client, ModelOutlineRenderer renderer, ShayzienAssist plugin)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGHEST);
		setLayer(OverlayLayer.ABOVE_SCENE);
		this.client = client;
		this.renderer = renderer;
		this.plugin = plugin;
		this.ids = Set.of(
			6826, 6828, 6830, 6832, 6834, 6836,
			6838, 6840, 6842, 6844, 6846, 6848,
			6850, 6852, 6854, 6856
		);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (plugin.getRegionId() != 5944 && plugin.getRegionId() != 6200)
		{
			return null;
		}
		client.getNpcs()
			.stream()
			.filter(Objects::nonNull)
			.filter((n) -> ids.contains(n.getId()) && n.getModel() != null)
			.forEach((n) -> renderer.drawOutline(n, 1, Color.GREEN, 0));
		return null;
	}
}
