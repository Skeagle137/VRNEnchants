package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@EnchDescription("Chance to do double damage to the target.")
public class EnchDoubleStrike extends BaseEnchant {

    private static final Enchantment instance = new EnchDoubleStrike();

    private EnchDoubleStrike() {
        super("Double Strike", 3, Target.SWORDS);
        setRarity(Rarity.COSMIC);
    }

    @Override
    protected void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent e) {
        if (new RNG().calcChance(0, 2, level)) {
            e.setDamage(e.getFinalDamage() * 2);
            damager.getWorld().spawnParticle(Particle.LAVA, e.getEntity().getLocation().clone().add(0, 1, 0), 5, 0.3, 0.3, 0.3, 0.1);
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
