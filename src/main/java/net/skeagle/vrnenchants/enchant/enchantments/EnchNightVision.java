package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.extended.ArmorEnchantSingle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnchNightVision extends ArmorEnchantSingle {

    private static final Enchantment instance = new EnchNightVision();

    private EnchNightVision() {
        super("Night Vision", 3, ArmorTarget.HELMET);
        setRarity(Rarity.UNCOMMON);
    }

    public String setDescription() {
        return "Helps you to see in the dark.";
    }

    @Override
    public void onTick(LivingEntity e, int parts, int level) {
        if (parts != 0)
            e.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 216, level - 1, false, false, false));
    }

    @Override
    protected void onUnEquip(LivingEntity e, int parts, int level) {
        e.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
