package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnchVenom extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchVenom();

    private EnchVenom() {
        super("Venom", 3);
        setRarity(Rarity.COMMON);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        final RNG rng = new RNG();
        if (rng.calcChance(15)) {
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, level * 20, 1, false, true, true));
        }
    }

    public String setDescription() {
        return "15% chance to inflict the target with poison. The greater the enchant's power, the longer the effect lasts.";
    }
}
