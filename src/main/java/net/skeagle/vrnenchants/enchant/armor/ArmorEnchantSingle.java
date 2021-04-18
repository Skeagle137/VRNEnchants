package net.skeagle.vrnenchants.enchant.armor;

import net.skeagle.vrnenchants.enchant.Target;

public class ArmorEnchantSingle extends ArmorEnchant {

    public ArmorEnchantSingle(String name, int maxlevel, ArmorTarget armorTarget) {
        super(name, maxlevel, armorTarget.getTarget());
    }

    protected enum ArmorTarget {
        HELMET(Target.HELMETS),
        CHEST(Target.CHESTPLATES),
        LEGS(Target.LEGGINGS),
        BOOTS(Target.BOOTS),
        ELYTRA(Target.ELYTRA);

        private final Target target;

        ArmorTarget(Target target) {
            this.target = target;
        }

        public Target getTarget() {
            return target;
        }
    }
}
