package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;

public class EnchCalvary extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchCalvary();

    private EnchCalvary() {
        super("Calvary", 4);
        setRarity(Rarity.RARE);
    }

    public String setDescription() {
        return "Provides extra levels of defense for horses.";
    }
}
