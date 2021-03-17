package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EnchExplosive extends BaseEnchant {

    private static final Enchantment instance = new EnchExplosive();

    private EnchExplosive() {
        super("Explosive", 6, EnchantmentTarget.BOW);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onHit(final int level, final LivingEntity shooter, final ProjectileHitEvent e) {
        if (new RNG().calcChance(6, 2, level)) {
            int i = 400 + (level * 5);
            e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) ((VRNUtil.rng((Math.min(i, 500)), 500) / 100) / 2), false, false, shooter);
        }
    }

    public String setDescription() {
        return "Chance for arrows to explode when they hit an object or the ground.";
    }

    public static Enchantment getInstance() {
        return instance;
    }

}
