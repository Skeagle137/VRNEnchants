package net.skeagle.vrnenchants.enchant;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Target {
    SWORDS("_SWORD", "Swords"),
    PICKAXES("_PICKAXE", "Pickaxes"),
    AXES("_AXE", "Axes"),
    SHOVELS("_SHOVEL", "Shovels"),
    HOES("_HOE", "Hoes"),
    ROD("_ROD", "Fishing Rods"),
    BOW("BOW", "Bows"),
    CROSSBOW("CROSSBOW", "Crossbows"),
    HELMETS("_HELMET", "Helmets"),
    CHESTPLATES("_CHESTPLATE", "Chestplates"),
    LEGGINGS("_LEGGINGS", "Leggings"),
    BOOTS("_BOOTS", "Boots"),
    ELYTRA("ELYTRA", "Elytras"),
    SHIELD("SHIELD", "Shields"),
    TRIDENT("TRIDENT", "Tridents"),
    ANY("Any tool or armor");

    private final String keyword;
    private final String name;

    Target(String keyword, String name) {
        this.keyword = keyword;
        this.name = name;
    }

    Target(String name) {
        this.keyword = null;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean matches(ItemStack i, Target... targets) {
        if (i == null || i.getType() == Material.AIR) return false;
        return Arrays.stream(targets).anyMatch(t -> t == Target.ANY || (t.keyword != null && i.getType().toString().endsWith(t.keyword)));
    }

    public static boolean matches(BaseEnchant ench, Target... targets) {
        return Arrays.stream(targets).anyMatch(et -> Arrays.stream(ench.getTargets()).anyMatch(t -> t == et || t == Target.ANY));
    }

    public static boolean isArmor(ItemStack i) {
        return matches(i, HELMETS, CHESTPLATES, LEGGINGS, BOOTS);
    }

    public static boolean isTool(ItemStack i) {
        return matches(i, PICKAXES, AXES, SHOVELS, HOES);
    }
}
