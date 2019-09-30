package net.runelite.client.plugins.oneclick;

import lombok.Getter;

class AlchItem
{
	@Getter
	private String name;
	@Getter
	private int id;
	private int quantity;

	public AlchItem(String name, int id, int quantity)
	{
		this.name = name;
		this.id = id;
		this.quantity = quantity;
	}
}
