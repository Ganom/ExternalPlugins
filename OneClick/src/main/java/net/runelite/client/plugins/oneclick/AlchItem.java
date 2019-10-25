/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, TomC <https://github.com/tomcylke>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.oneclick;

import lombok.Getter;

@Getter
class AlchItem
{
	private String name;
	private int id;

	AlchItem(String name, int id)
	{
		this.name = name;
		this.id = id;
	}
}
