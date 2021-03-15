package net.skeagle.vrnenchants.enchant;

public enum Rarity {
    COMMON("&7Common (1✫)", 2, 0, "&r&f"),
    UNCOMMON("&aUncommon (2✫)", 4, 15, "&r&a"),
    RARE("&9Rare (3✫)", 6, 30, "&r&9"),
    EPIC("&5&oEpic &r&5(4✫)", 10, 60, "&r&5"),
    LEGENDARY("&6&lLegendary &r&6(5✫)", 15, 120,"&r&6"),
    MYTHICAL("&b&lMythical &r&b(6✫)", 25, 240, "&r&b"),
    COSMIC("&4&k:&r&c&lCosmic&r&4&k: &r&c(7✫)", 50, 400, "&r&4"),
    FABLED("&5&k&l:&r&d&l&oFabled&r&4&k&l: &r&d(8✫)", 50, 400, "&r&d&l");

    private final String rarity;
    private final int individual;
    private final int combined;
    private final String prefix;

    Rarity(final String rarity, final int individual, final int combined, final String prefix) {
        this.rarity = rarity;
        this.individual = individual;
        this.combined = combined;
        this.prefix = prefix;
    }

    public String getRarity() {
        return rarity;
    }

    public int getIndividualPoints() {
        return individual;
    }

    public int getCombinedPoints() {
        return combined;
    }

    public String getPrefix() {
        return prefix;
    }

    public static Rarity getRarityFromCombinedPoints(final int points) {
        Rarity rarity = COMMON;
        for (final Rarity r : Rarity.values())
            if (points >= r.getCombinedPoints())
                rarity = r;
        return rarity;
    }

    public static String getPrefixFromCombinedPoints(final int points) {
        String prefix = COMMON.getPrefix();
        for (final Rarity r : Rarity.values())
            if (points >= r.getCombinedPoints())
                prefix = r.getPrefix();
        return prefix;
    }
}
