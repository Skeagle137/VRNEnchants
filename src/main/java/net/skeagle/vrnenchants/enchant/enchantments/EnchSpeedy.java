package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class EnchSpeedy extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchSpeedy();

    private EnchSpeedy() {
        super("Speedy", 3, EnchantmentTarget.WEAPON);
        setRarity(20);
        setRarityFactor(20);
    }

    @Override
    public void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
    }
}
