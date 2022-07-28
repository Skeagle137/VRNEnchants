package net.skeagle.vrnenchants.enchant;

import net.md_5.bungee.api.ChatColor;

public enum Rarity {
    COMMON("&7Common (1✫)", ChatColor.WHITE),
    UNCOMMON("&aUncommon (2✫)", ChatColor.GREEN),
    RARE("&9Rare (3✫)", ChatColor.BLUE),
    EPIC("&5&oEpic &r&5(4✫)", ChatColor.DARK_PURPLE),
    LEGENDARY("&6&lLegendary &r&6(5✫)", ChatColor.GOLD),
    MYTHICAL("&b&lMythical &r&b(6✫)", ChatColor.AQUA),
    COSMIC("&4&k:&r&c&lCosmic&r&4&k: &r&c(7✫)", ChatColor.DARK_RED);

    private final String rarity;
    private final ChatColor color;

    Rarity(final String rarity, final ChatColor color) {
        this.rarity = rarity;
        this.color = color;
    }

    public String getRarity() {
        return rarity;
    }

    public ChatColor getColor() {
        return color;
    }
}
