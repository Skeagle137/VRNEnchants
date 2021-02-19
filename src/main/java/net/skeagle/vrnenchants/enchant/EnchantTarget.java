package net.skeagle.vrnenchants.enchant;

import org.bukkit.inventory.ItemStack;

public enum EnchantTarget {
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

    private String keyword;

    EnchantTarget(String keyword) {
        this.keyword = keyword;
    }

    public static boolean parse(ItemStack i) {
        for (EnchantTarget et : EnchantTarget.values())
            if (i.getType().toString().endsWith(et.keyword))
                return true;
        return false;
    }

    public static boolean parse(ItemStack i, EnchantTarget... targets) {
        for (EnchantTarget et : targets)
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
