package net.skeagle.vrnenchants.enchant.extended;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.EnchantTarget;
import org.bukkit.enchantments.Enchantment;

public class ArmorEnchantSingle extends ArmorEnchant {

    public ArmorEnchantSingle(String name, int maxlevel, ArmorTarget target) {
        super(name, maxlevel);
    }

    public ArmorEnchantSingle(String name, int maxlevel, ArmorTarget target, Enchantment... conflicting) {
        super(name, maxlevel, conflicting);
    }

    protected enum ArmorTarget {
        HELMET(EnchantTarget.HELMETS),
        CHEST(EnchantTarget.CHESTPLATES),
        LEGS(EnchantTarget.LEGGINGS),
        BOOTS(EnchantTarget.BOOTS);

        @Getter
        private final EnchantTarget target;

        ArmorTarget(EnchantTarget target) {
            this.target = target;
        }
    }
}
