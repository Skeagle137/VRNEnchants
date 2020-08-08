package net.skeagle.vrnenchants.enchant;

import net.skeagle.vrnenchants.util.VRNUtil;

public class RNG {
    private double chanceFactor = 0;

    public boolean calcChance(final double chanceFactor, final int level) {
        this.chanceFactor = chanceFactor;
        return VRNUtil.rng(1, 100) <= calcFactor(level);
    }

    public boolean calcChance(final double chanceFactor) {
        return VRNUtil.rng(1, 100) <= chanceFactor;
    }

    private double calcFactor(final int level) {
        if (level < 2) {
            return 1;
        }
        return chanceFactor * level;
    }
}
