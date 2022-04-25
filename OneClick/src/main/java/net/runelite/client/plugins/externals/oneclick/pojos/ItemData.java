package net.runelite.client.plugins.externals.oneclick.pojos;

import lombok.Value;
import net.runelite.api.ItemComposition;

@Value
public class ItemData
{
	int id;
	int quantity;
	int index;
	String name;
	ItemComposition definition;
}