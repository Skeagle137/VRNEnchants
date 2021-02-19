package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.extended.ArmorEnchant;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;

public class EnchGrowth extends ArmorEnchant {

    @Getter
    private static final Enchantment instance = new EnchGrowth();

    private EnchGrowth() {
        super("Growth", 5);
        setRarity(Rarity.LEGENDARY);
    }

    public String setDescription() {
        return "Increases your max health by half a heart for each level.";
    }

    @Override
    public void onEquip(LivingEntity e, int parts, int level) {
        e.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + (level));
    }

    @Override
    public void onUnEquip(LivingEntity e, int parts, int level) {
        if (parts == 0) {
            e.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
            return;
        }
        e.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - (level));
    }
}
