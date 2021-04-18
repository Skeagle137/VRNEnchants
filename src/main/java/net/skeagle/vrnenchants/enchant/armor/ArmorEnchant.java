package net.skeagle.vrnenchants.enchant.armor;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.entity.LivingEntity;

public abstract class ArmorEnchant extends BaseEnchant {

    public ArmorEnchant(String name, int maxlevel) {
        super(name, maxlevel, Target.HELMETS, Target.CHESTPLATES, Target.LEGGINGS, Target.BOOTS, Target.ELYTRA);
    }

    public ArmorEnchant(String name, int maxlevel, Target... target) {
        super(name, maxlevel, target);
    }

    protected void onTick(LivingEntity entity, int armorparts, int level) {
    }

    protected void onEquip(LivingEntity entity, int armorparts, int level) {
    }

    protected void onUnEquip(LivingEntity entity, int armorparts, int level) {
    }
}
