package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EnchDoubleStrike extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchDoubleStrike();

    private EnchDoubleStrike() {
        super("Double Strike", 3, EnchantmentTarget.WEAPON);
        setRarity(Rarity.COSMIC);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        final RNG rng = new RNG();
        if (rng.calcChance(1.5, level))
            e.setDamage(e.getDamage() * 2);
    }

    public String setDescription() {
        return "chance to do double damage to the target.";
    }
}
