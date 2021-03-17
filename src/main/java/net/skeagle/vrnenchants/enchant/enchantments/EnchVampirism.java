package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EnchVampirism extends BaseEnchant {

    private static final Enchantment instance = new EnchVampirism();

    private EnchVampirism() {
        super("Vampirism", 2, EnchantmentTarget.WEAPON);
        setRarity(Rarity.LEGENDARY);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(4 + (level * 2))) {
            double health = damager.getHealth() + e.getFinalDamage() * (0.75 - (level > 1 ? 0.15 : 0));
            if ((damager.getHealth() + health) < damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                damager.setHealth(health);
            else
                damager.setHealth(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }
    }

    public String setDescription() {
        return "chance to steal hearts from the target.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
