package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class EnchSpeedy extends BaseEnchant {

    private static final Enchantment instance = new EnchSpeedy();

    private EnchSpeedy() {
        super("Speedy", 2, EnchantmentTarget.WEAPON);
        setRarity(Rarity.UNCOMMON);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(15, 5, level))
            damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (level * 20) + 20, level - 1, false, true, true));
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
