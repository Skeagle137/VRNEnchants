package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

public class EnchFisherman extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchFisherman();

    private EnchFisherman() {
        super("Bookworm", 2, EnchantmentTarget.FISHING_ROD);
        setRarity(Rarity.EPIC);
    }

    //handled externally - see listeners with enchant chances tables

    public String setDescription() {
        return "Increases the chances of getting a higher tier enchant on enchanted books when fishing.";
    }
}
