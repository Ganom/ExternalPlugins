package net.runelite.client.plugins.externals.oneclick;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Spells {
    HIGH_ALCH("High Alch"),
    SUPERHEAT("Superheat"),
    ENCHANT_SAPPHIRE("Sapphire Jewelery"),
    ENCHANT_EMERALD("Emerald Jewelery"),
    ENCHANT_RUBY("Ruby Jewelery"),
    ENCHANT_DIAMOND("Diamond Jewelery"),
    ENCHANT_DRAGONSTONE("Dragonstone Jewelery"),
    ENCHANT_ONYX("Onyx Jewelery"),
    ENCHANT_ZENYTE("Zenyte Jewelery"),
    NONE("None");

    private String spells;

    @Override
    public String toString() {
        return getSpells();
    }
}
