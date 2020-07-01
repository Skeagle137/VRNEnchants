package net.skeagle.vrnenchants.enchant;

import java.util.Random;

public class RNG {
    private double chanceFactor = 0;

    public boolean calcChance(final double chanceFactor, final int level) {
        this.chanceFactor = chanceFactor;
        final Random random = new Random();
        return random.nextInt(101) <= calcFactor(level);
    }

    public boolean calcChance(final double chanceFactor) {
        final Random random = new Random();
        return random.nextInt(101) <= chanceFactor;
    }

    private double calcFactor(final int level) {
        if (level < 2) {
            return 1;
        }
        return chanceFactor * level;
    }
}
