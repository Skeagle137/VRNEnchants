package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EnchExplosive extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchExplosive();

    private EnchExplosive() {
        super("Explosive", 1, EnchantmentTarget.BOW);
        setRarity(Rarity.RARE);
    }

    @Override
    protected void onHit(final int level, final LivingEntity shooter, final ProjectileHitEvent e) {
        e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 1, false, false);
    }

    public String setDescription() {
        return "Arrows explode when they hit an object or the ground.";
    }
}
