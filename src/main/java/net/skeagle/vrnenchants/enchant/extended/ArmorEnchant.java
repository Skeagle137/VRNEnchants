package net.skeagle.vrnenchants.enchant.extended;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;

public abstract class ArmorEnchant extends BaseEnchant {

    public ArmorEnchant(String name, int maxlevel) {
        super(name, maxlevel, EnchantmentTarget.ARMOR);
    }

    public ArmorEnchant(String name, int maxlevel, Enchantment... conflicting) {
        super(name, maxlevel, EnchantmentTarget.ARMOR, conflicting);
    }

    protected void onTick(LivingEntity entity, int armorparts, int level) {
    }

    protected void onEquip(LivingEntity entity, int armorparts, int level) {
    }

    protected void onUnEquip(LivingEntity entity, int armorparts, int level) {
    }
}
