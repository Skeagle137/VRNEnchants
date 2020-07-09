package net.skeagle.vrnenchants.enchant;

import net.skeagle.vrnenchants.enchant.enchantments.*;
import net.skeagle.vrnenchants.util.ReflectionUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class VRNEnchants {

    protected enum Vanilla {
        PROTECTION_ENVIRONMENTAL(Enchantment.PROTECTION_ENVIRONMENTAL, 20, 25),
        PROTECTION_FIRE(Enchantment.PROTECTION_FIRE, 20, 20),
        PROTECTION_FALL(Enchantment.PROTECTION_FALL, 10, 15),
        PROTECTION_EXPLOSIONS(Enchantment.PROTECTION_EXPLOSIONS, 20, 20),
        PROTECTION_PROJECTILE(Enchantment.PROTECTION_PROJECTILE, 10, 20),
        OXYGEN(Enchantment.OXYGEN, 10, 10),
        WATER_WORKER(Enchantment.WATER_WORKER, 20),
        THORNS(Enchantment.THORNS, 10, 20),
        DEPTH_STRIDER(Enchantment.DEPTH_STRIDER, 20, 20),
        FROST_WALKER(Enchantment.FROST_WALKER, 30, 30),
        DAMAGE_ALL(Enchantment.DAMAGE_ALL, 10, 25),
        DAMAGE_UNDEAD(Enchantment.DAMAGE_UNDEAD, 10, 10),
        DAMAGE_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS, 10, 10),
        KNOCKBACK(Enchantment.KNOCKBACK, 20, 20),
        FIRE_ASPECT(Enchantment.FIRE_ASPECT, 20, 20),
        LOOT_BONUS_MOBS(Enchantment.LOOT_BONUS_MOBS, 10, 20),
        SWEEPING_EDGE(Enchantment.SWEEPING_EDGE, 10, 10),
        DIG_SPEED(Enchantment.DIG_SPEED, 20, 15),
        SILK_TOUCH(Enchantment.SILK_TOUCH, 60),
        DURABILITY(Enchantment.DURABILITY, 20, 20),
        LOOT_BONUS_BLOCKS(Enchantment.LOOT_BONUS_BLOCKS, 20, 20),
        ARROW_DAMAGE(Enchantment.ARROW_DAMAGE, 10, 20),
        ARROW_KNOCKBACK(Enchantment.ARROW_KNOCKBACK, 20, 20),
        ARROW_FIRE(Enchantment.ARROW_FIRE, 60),
        ARROW_INFINITE(Enchantment.ARROW_INFINITE, 60),
        LUCK(Enchantment.LUCK, 20, 30),
        LURE(Enchantment.LURE, 25, 25),
        LOYALTY(Enchantment.LOYALTY, 30, 10),
        IMPALING(Enchantment.IMPALING, 30, 15),
        RIPTIDE(Enchantment.RIPTIDE, 40, 30),
        CHANNELING(Enchantment.CHANNELING, 50),
        MULTISHOT(Enchantment.MULTISHOT, 30),
        QUICK_CHARGE(Enchantment.QUICK_CHARGE, 30, 20),
        PIERCING(Enchantment.PIERCING, 20, 20),
        MENDING(Enchantment.MENDING, 60),
        VANISHING_CURSE(Enchantment.VANISHING_CURSE),
        BINDING_CURSE(Enchantment.BINDING_CURSE);

        private final Enchantment enchant;
        private int rarity;
        private int rarityfactor;
        private boolean iscursed;

        Vanilla(final Enchantment enchant, final int rarity) {
            this.enchant = enchant;
            this.rarity = rarity;
            this.rarityfactor = 0;
        }

        Vanilla(final Enchantment enchant, final int rarity, final int rarityfactor) {
            this.enchant = enchant;
            this.rarity = rarity;
            this.rarityfactor = rarityfactor;
        }

        Vanilla(final Enchantment enchant) {
            this.enchant = enchant;
            this.iscursed = true;
        }

        public Enchantment getEnch() {
            return this.enchant;
        }

        public int getRarity() {
            return this.rarity;
        }

        public int getRarityfactor() {
            return this.rarityfactor;
        }

        public boolean isCursed() {
            return this.iscursed;
        }
    }

    public enum VRN {
        EXECUTE(EnchExecute.getInstance()),
        EXPLOSIVE(EnchExplosive.getInstance()),
        VENOM(EnchVenom.getInstance()),
        GILLS(EnchGills.getInstance()),
        AUTOSMELT(EnchAutoSmelt.getInstance()),
        GEMSTONE(EnchGemstone.getInstance()),
        MINESIGHT(EnchMineSight.getInstance()),
        STUN(EnchStun.getInstance()),
        SPEEDY(EnchSpeedy.getInstance())
        ;

        private final Enchantment enchant;

        VRN(final Enchantment enchant) {
            this.enchant = enchant;
        }

        public Enchantment getEnch() {
            return this.enchant;
        }
    }

    public static void registerEnchant(Enchantment ench) {
        boolean isregistered = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(ench);
        if (isregistered) return;
        ReflectionUtils.setAccessable(Enchantment.class, "acceptingNew");
        Enchantment.registerEnchantment(ench);
    }

    public static void registerOnStart() {
        Arrays.stream(VRN.values()).forEach(vrn -> registerEnchant(vrn.getEnch()));
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public static void unregisterEnchant(Enchantment ench) {
        HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>)
                ReflectionUtils.getFieldContent(Enchantment.class, "byKey", null);
        byKey.remove(ench.getKey());
        HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>)
                ReflectionUtils.getFieldContent(Enchantment.class, "byName", null);
        byName.remove(ench.getName());
    }
}
