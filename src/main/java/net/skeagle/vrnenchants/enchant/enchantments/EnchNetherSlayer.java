package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@EnchDescription("Increased damage to mobs in the nether.")
public class EnchNetherSlayer extends BaseEnchant {

    private static final Enchantment instance = new EnchNetherSlayer();

    private EnchNetherSlayer() {
        super("Nether Slayer", 2, Target.SWORDS, Target.AXES);
        setRarity(Rarity.RARE);
    }

    @Override
    protected void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent e) {
        if (damager.getWorld().getEnvironment() != World.Environment.NETHER) return;

        e.setDamage(e.getFinalDamage() + (level * 2));
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
