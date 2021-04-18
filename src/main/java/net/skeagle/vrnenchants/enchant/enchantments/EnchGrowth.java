package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.armor.ArmorEnchant;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;

@EnchDescription("Increases your max health by half a heart for each level.")
public class EnchGrowth extends ArmorEnchant {

    private static final Enchantment instance = new EnchGrowth();

    private EnchGrowth() {
        super("Growth", 5);
        setRarity(Rarity.LEGENDARY);
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

    public static Enchantment getInstance() {
        return instance;
    }
}
