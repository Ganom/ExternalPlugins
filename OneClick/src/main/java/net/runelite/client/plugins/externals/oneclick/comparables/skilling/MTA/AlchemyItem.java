package net.runelite.client.plugins.externals.oneclick.comparables.skilling.MTA;

import lombok.Getter;
import net.runelite.api.ItemID;

public enum AlchemyItem
{
	LEATHER_BOOTS("Leather Boots", ItemID.LEATHER_BOOTS_6893),
	ADAMANT_KITESHIELD("Adamant Kiteshield", ItemID.ADAMANT_KITESHIELD_6894),
	ADAMANT_MED_HELM("Adamant Helm", ItemID.ADAMANT_MED_HELM_6895),
	EMERALD("Emerald", ItemID.EMERALD_6896),
	RUNE_LONGSWORD("Rune Longsword", ItemID.RUNE_LONGSWORD_6897);

	@Getter
	private final int id;
	@Getter
	private final String name;

	AlchemyItem(String name, int id)
	{
		this.id = id;
		this.name = name;
	}

	public static AlchemyItem find(String item)
	{
		for (AlchemyItem alchemyItem : values())
		{
			if (item.toLowerCase().contains(alchemyItem.name.toLowerCase()))
			{
				return alchemyItem;
			}
		}
		return null;
	}
}