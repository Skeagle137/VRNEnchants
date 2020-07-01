package net.skeagle.vrnenchants.enchant;

public enum Rarity {
    COMMON("&7Common (1✫)", 0, 0, "&r&7"),
    UNCOMMON("&aUncommon (2✫)", 30, 100, "&r&a"),
    RARE("&9Rare (3✫)", 60, 220, "&r&9"),
    EPIC("&5&oEpic &r&5(4✫)", 100, 350, "&r&5"),
    LEGENDARY("&6&lLegendary &r&6(5✫)", 150, 550,"&r&6"),
    MYTHICAL("&b&lMythical &r&b(6✫)", 220, 800, "&r&b"),
    ANCIENT("&4&k:&r&c&lAncient&r&4&k: &r&c(7✫)", 320, 1200, "&r&4");

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

    public static String getRarityFromIndividualPoints(final int points) {
        String rarity = COMMON.getRarity();
        for (final Rarity r : Rarity.values()) {
            if (points >= r.getIndividualPoints()) {
                rarity = r.getRarity();
            }
        }
        return rarity;
    }

    public static String getRarityFromCombinedPoints(final int points) {
        String rarity = COMMON.getRarity();
        for (final Rarity r : Rarity.values()) {
            if (points >= r.getCombinedPoints()) {
                rarity = r.getRarity();
            }
        }
        return rarity;
    }

    public static String getPrefixFromIndividualPoints(final int points) {
        String prefix = COMMON.getPrefix();
        for (final Rarity r : Rarity.values()) {
            if (points >= r.getIndividualPoints()) {
                prefix = r.getPrefix();
            }
        }
        return prefix;
    }

    public static String getPrefixFromCombinedPoints(final int points) {
        String prefix = COMMON.getPrefix();
        for (final Rarity r : Rarity.values()) {
            if (points >= r.getCombinedPoints()) {
                prefix = r.getPrefix();
            }
        }
        return prefix;
    }
}
