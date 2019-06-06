package net.runelite.client.plugins.gearswapper.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionType
{
	FLEXO("Flexo"),
	MOUSEEVENTS("MouseEvents"),
	MENUACTIONS("MenuActions");

	private String name;

	@Override
	public String toString()
	{
		return getName();
	}
}
