package net.skeagle.vrnenchants.enchant;

import net.skeagle.vrnenchants.util.VRNUtil;

import java.util.HashMap;
import java.util.Map;

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

    public static class Randomizer<T> {

        HashMap<T, Integer> map;
        int bounds;

        public Randomizer() {
            map = new HashMap<>();
        }

        public Randomizer<T> addEntry(T obj, int chance) {
            map.put(obj, (bounds + chance));
            bounds += chance;
            return this;
        }

        public T build() {
            int rand = VRNUtil.rng(1, bounds);
            int distance = Math.abs(map.entrySet().iterator().next().getValue() - rand);
            int closest = 0;
            T key = null;
            for (Map.Entry<T, Integer> entry : map.entrySet()) {
                int chance = entry.getValue();
                int entry_distance = Math.abs(chance - rand);
                if (entry_distance == 0)
                    return entry.getKey();
                if (entry_distance <= distance) {
                    distance = entry_distance;
                    closest = entry.getValue();
                }
            }
            for (Map.Entry<T, Integer> entry : map.entrySet()) {
                if (entry.getValue() == closest) {
                    key = entry.getKey();
                }
            }
            return key;
        }
    }
}
