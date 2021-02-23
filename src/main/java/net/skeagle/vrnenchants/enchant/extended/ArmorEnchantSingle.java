package net.skeagle.vrnenchants.enchant.extended;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.enchantments.Enchantment;

public class ArmorEnchantSingle extends ArmorEnchant {

    public ArmorEnchantSingle(String name, int maxlevel, ArmorTarget target) {
        super(name, maxlevel);
    }

    public ArmorEnchantSingle(String name, int maxlevel, ArmorTarget target, Enchantment... conflicting) {
        super(name, maxlevel, conflicting);
    }

    protected enum ArmorTarget {
        HELMET(Target.HELMETS),
        CHEST(Target.CHESTPLATES),
        LEGS(Target.LEGGINGS),
        BOOTS(Target.BOOTS);

        @Getter
        private final Target target;

        ArmorTarget(Target target) {
            this.target = target;
        }
    }
}
