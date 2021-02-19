package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.extended.ArmorEnchantSingle;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnchNightVision extends ArmorEnchantSingle {

    @Getter
    private static final Enchantment instance = new EnchNightVision();

    private EnchNightVision() {
        super("Night Vision", 3, ArmorTarget.HELMET);
        setRarity(Rarity.UNCOMMON);
    }

    public String setDescription() {
        return "Increases your max health by half a heart for each level.";
    }

    @Override
    public void onTick(LivingEntity e, int parts, int level) {
        if (parts != 0)
            e.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 215, level - 1, false, false, false));
    }

    @Override
    protected void onUnEquip(LivingEntity e, int parts, int level) {
        e.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
