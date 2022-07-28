package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;

@EnchDescription("Chance for arrows to explode when they hit an object or the ground.")
public class EnchExplosive extends BaseEnchant {

    private static final Enchantment instance = new EnchExplosive();

    private EnchExplosive() {
        super("Explosive", 6, Target.BOW, Target.CROSSBOW);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onHit(int level, LivingEntity shooter, ProjectileHitEvent e) {
        if (new RNG().calcChance(4, 4, level)) {
            int i = 400 + (level * 5);
            e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) ((VRNUtil.rng((Math.min(i, 500)), 500) / 100) / 2), false, false, shooter);
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }

}
