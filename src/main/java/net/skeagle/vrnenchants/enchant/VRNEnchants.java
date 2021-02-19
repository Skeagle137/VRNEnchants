package net.skeagle.vrnenchants.enchant;

import net.skeagle.vrnenchants.enchant.enchantments.*;
import net.skeagle.vrnenchants.util.ReflectionUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class VRNEnchants {

    public enum VRN {
        EXECUTE(EnchExecute.getInstance()),
        EXPLOSIVE(EnchExplosive.getInstance()),
        VENOM(EnchVenom.getInstance()),
        GILLS(EnchGills.getInstance()),
        AUTOSMELT(EnchAutoSmelt.getInstance()),
        GEMSTONE(EnchGemstone.getInstance()),
        MINESIGHT(EnchMineSight.getInstance()),
        STUN(EnchStun.getInstance()),
        SPEEDY(EnchSpeedy.getInstance()),
        DOUBLESTRIKE(EnchDoubleStrike.getInstance()),
        HARVEST(EnchHarvest.getInstance()),
        GLIDING(EnchGliding.getInstance()),
        WITHERING(EnchWithering.getInstance()),
        UNDERSHIRT(EnchUnderShirt.getInstance()),
        GROWTH(EnchGrowth.getInstance()),
        NIGHTVISION(EnchNightVision.getInstance()),
        VAMPIRISM(EnchVampirism.getInstance()),
        ENDERSHOT(EnchEnderShot.getInstance()),
        ENDLESSQUIVER(EnchEndlessQuiver.getInstance()),
        //SOULBOUND(EnchSoulBound.getInstance()),
        TELEPORTRESISTANCE(EnchTeleportResistance.getInstance()),
        BOOKWORM(EnchBookworm.getInstance()),
        FISHERMAN(EnchFisherman.getInstance()),
        EXTINGUISH(EnchExtinguish.getInstance()),
        THUNDERSTRIKE(EnchThunderStrike.getInstance()),
        AEGIS(EnchAegis.getInstance()),
        //VOLLEY(EnchVolley.getInstance()),
        //GOLDDIGGER(EnchGoldDigger.getInstance()),
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
