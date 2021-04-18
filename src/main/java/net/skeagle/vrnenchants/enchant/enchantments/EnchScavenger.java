package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

@EnchDescription("Looting for a bow.")
public class EnchScavenger extends BaseEnchant {

    private static final Enchantment instance = new EnchScavenger();

    private EnchScavenger() {
        super("Scavenger", 3, Target.BOW, Target.CROSSBOW);
        setRarity(Rarity.RARE);
    }

    @Override
    protected void onKill(int level, Player killer, EntityDeathEvent e) {
        for (ItemStack stack : e.getDrops())
            if (VRNUtil.rng(0, 100) < level / (level + 1))
                killer.getWorld().dropItem(e.getEntity().getLocation(), stack);
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
