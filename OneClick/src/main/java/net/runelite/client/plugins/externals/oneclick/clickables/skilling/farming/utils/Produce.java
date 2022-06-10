/*
 * Copyright (c) 2018 Abex
 * Copyright (c) 2018, NotFoxtrot <https://github.com/NotFoxtrot>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Produce
{
	WEEDS("Weeds"),
	SCARECROW("Scarecrow"),

	// Allotment crops
	POTATO("Potato"),
	ONION("Onion"),
	CABBAGE("Cabbage"),
	TOMATO("Tomato"),
	SWEETCORN("Sweetcorn"),
	STRAWBERRY("Strawberry"),
	WATERMELON("Watermelon"),
	SNAPE_GRASS("Snape grass"),

	// Flower crops
	MARIGOLD("Marigold"),
	ROSEMARY("Rosemary"),
	NASTURTIUM("Nasturtium"),
	WOAD("Woad"),
	LIMPWURT("Limpwurt"),
	WHITE_LILY("White lily"),

	// Bush crops
	REDBERRIES("Redberry"),
	CADAVABERRIES("Cadavaberry"),
	DWELLBERRIES("Dwellberry"),
	JANGERBERRIES("Jangerberry"),
	WHITEBERRIES("Whiteberry"),
	POISON_IVY("Poison ivy"),

	// Hop crops
	BARLEY("Barley"),
	HAMMERSTONE("Hammerstone"),
	ASGARNIAN("Asgarnian"),
	JUTE("Jute"),
	YANILLIAN("Yanillian"),
	KRANDORIAN("Krandorian"),
	WILDBLOOD("Wildblood"),

	// Herb crops
	GUAM("Guam"),
	MARRENTILL("Marrentill"),
	TARROMIN("Tarromin"),
	HARRALANDER("Harralander"),
	RANARR("Ranarr"),
	TOADFLAX("Toadflax"),
	IRIT("Irit"),
	AVANTOE("Avantoe"),
	KWUARM("Kwuarm"),
	SNAPDRAGON("Snapdragon"),
	CADANTINE("Cadantine"),
	LANTADYME("Lantadyme"),
	DWARF_WEED("Dwarf weed"),
	TORSTOL("Torstol"),
	GOUTWEED("Goutweed"),
	ANYHERB("Any herb"),

	// Tree crops
	OAK("Oak"),
	WILLOW("Willow"),
	MAPLE("Maple"),
	YEW("Yew"),
	MAGIC("Magic"),

	// Fruit tree crops
	APPLE("Apple"),
	BANANA("Banana"),
	ORANGE("Orange"),
	CURRY("Curry"),
	PINEAPPLE("Pineapple"),
	PAPAYA("Papaya"),
	PALM("Palm"),
	DRAGONFRUIT("Dragonfruit"),

	// Cactus
	CACTUS("Cactus"),
	POTATO_CACTUS("Potato cactus"),

	// Hardwood
	TEAK("Teak"),
	MAHOGANY("Mahogany"),

	// Anima
	ATTAS("Attas"),
	IASOR("Iasor"),
	KRONOS("Kronos"),

	// Special crops
	SEAWEED("Seaweed"),
	GRAPE("Grape"),
	MUSHROOM("Mushroom"),
	BELLADONNA("Belladonna"),
	CALQUAT("Calquat"),
	SPIRIT_TREE("Spirit tree"),
	CELASTRUS("Celastrus"),
	REDWOOD("Redwood"),
	HESPORI("Hespori"),
	CRYSTAL_TREE("Crystal tree"),
	;
	/**
	 * User-visible name
	 */
	private final String name;
}
