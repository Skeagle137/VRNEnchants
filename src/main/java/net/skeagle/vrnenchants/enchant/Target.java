package net.skeagle.vrnenchants.enchant;

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

    public static boolean parse(ItemStack i) {
        for (Target et : Target.values())
            if (i.getType().toString().endsWith(et.keyword))
                return true;
        return false;
    }

    public static boolean parse(ItemStack i, Target... targets) {
        for (Target et : targets)
            if (i.getType().toString().endsWith(et.keyword))
                return true;
        return false;
    }

    public static boolean isArmor(ItemStack i) {
        return parse(i, HELMETS, CHESTPLATES, LEGGINGS, BOOTS);
    }

    public static boolean isTool(ItemStack i) {
        return parse(i, PICKAXES, AXES, SHOVELS, HOES);
    }
}
