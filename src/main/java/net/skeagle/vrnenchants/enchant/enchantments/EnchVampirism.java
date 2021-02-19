package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EnchVampirism extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchVampirism();

    private EnchVampirism() {
        super("Vampirism", 2, EnchantmentTarget.WEAPON);
        setRarity(Rarity.LEGENDARY);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(6 + (level * 2)))
            damager.setHealth(damager.getHealth() + e.getDamage() * (level * 0.5));
    }

    public String setDescription() {
        return "chance to steal hearts from the target.";
    }
}
