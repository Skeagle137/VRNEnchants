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

public class EnchWithering extends BaseEnchant {

    private static final Enchantment instance = new EnchWithering();

    private EnchWithering() {
        super("Withering", 3, EnchantmentTarget.WEAPON);
        setRarity(Rarity.COMMON);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(3 + level))
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, level * 25, 1, false, true, true));
    }

    public String setDescription() {
        return "chance to inflict the target with wither. The higher the level, the longer the effect lasts.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
