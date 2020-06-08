package net.skeagle.vrnenchants.enchant;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.model.SimpleEnchantment;

import java.util.Map;
import java.util.Random;

public class RNG {
    private double chanceFactor = 0;

    public boolean checkEnchant(final ItemStack item, final Enchantment enchant) {
        final Map enchants = SimpleEnchantment.findEnchantments(item);
        return enchants.containsKey(enchant);
    }

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
