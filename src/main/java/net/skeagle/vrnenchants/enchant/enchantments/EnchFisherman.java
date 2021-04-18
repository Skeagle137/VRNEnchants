package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.enchantments.Enchantment;

@EnchDescription("Increases the chances of getting a higher tier enchant on enchanted books when fishing.")
public class EnchFisherman extends BaseEnchant {

    private static final Enchantment instance = new EnchFisherman();

    private EnchFisherman() {
        super("Fisherman", 2, Target.ROD);
        setRarity(Rarity.EPIC);
    }

    /**
     * handled externally
     * @see net.skeagle.vrnenchants.listener.FishingListener
     */

    public static Enchantment getInstance() {
        return instance;
    }
}
