package net.skeagle.vrnenchants.enchant;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Target {
    SWORDS("_SWORD"),
    PICKAXES("_PICKAXE"),
    AXES("_AXE"),
    SHOVELS("_SHOVEL"),
    HOES("_HOE"),
    ROD("_ROD"),
    BOW("BOW"),
    HELMETS("_HELMET"),
    CHESTPLATES("_CHESTPLATE"),
    LEGGINGS("_LEGGINGS"),
    BOOTS("_BOOTS");

    private final String keyword;

    Target(String keyword) {
        this.keyword = keyword;
    }

    public static boolean matches(ItemStack i) {
        for (Target et : Target.values())
            if (i.getType().toString().endsWith(et.keyword))
                return true;
        return false;
    }

    public static boolean matches(ItemStack i, Target... targets) {
        if (i == null || i.getType() == Material.AIR) return false;
        for (Target et : targets)
            if (i.getType().toString().endsWith(et.keyword))
                return true;
        return false;
    }

    public static boolean isArmor(ItemStack i) {
        return matches(i, HELMETS, CHESTPLATES, LEGGINGS, BOOTS);
    }

    public static boolean isTool(ItemStack i) {
        return matches(i, PICKAXES, AXES, SHOVELS, HOES);
    }
}
