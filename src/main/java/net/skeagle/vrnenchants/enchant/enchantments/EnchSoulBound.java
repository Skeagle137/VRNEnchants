package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

public class EnchSoulBound extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchSoulBound();

    private EnchSoulBound() {
        super("Soulbound", 3, EnchantmentTarget.WEAPON);
        setRarity(Rarity.MYTHICAL);
    }



    public String setDescription() {
        return "If you die, there is a chance for the item to stay in your inventory.";
    }
}
