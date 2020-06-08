package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import org.bukkit.enchantments.Enchantment;

public class EnchCalvary extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchCalvary();

    private EnchCalvary() {
        super("Calvary", 4);
        setRarity(60);
        setRarityFactor(20);
    }

    public String setDescription() {
        return "Provides extra levels of defense for horses.";
    }
}
