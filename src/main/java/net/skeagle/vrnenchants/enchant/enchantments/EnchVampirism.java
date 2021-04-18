package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@EnchDescription("Chance to steal hearts from the target.")
public class EnchVampirism extends BaseEnchant {

    private static final Enchantment instance = new EnchVampirism();

    private EnchVampirism() {
        super("Vampirism", 2, Target.SWORDS);
        setRarity(Rarity.LEGENDARY);
    }

    @Override
    protected void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(4 + (level * 2))) {
            double health = damager.getHealth() + e.getFinalDamage() * (0.75 - (level > 1 ? 0.15 : 0));
            if ((damager.getHealth() + health) < damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                damager.setHealth(health);
            else
                damager.setHealth(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
