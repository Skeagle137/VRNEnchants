package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.enchantments.Enchantment;

@EnchDescription("Increases the chances of getting a higher tier enchant on enchanted books when killing mobs.")
public class EnchBookworm extends BaseEnchant {

    private static final Enchantment instance = new EnchBookworm();

    private EnchBookworm() {
        super("Bookworm", 3, Target.SWORDS, Target.AXES);
        setRarity(Rarity.RARE);
    }

    /**
     * handled externally
     * @see net.skeagle.vrnenchants.listener.MobKillListener
     */

    public static Enchantment getInstance() {
        return instance;
    }
}
