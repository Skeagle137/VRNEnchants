package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.remain.Remain;

public class EnchStun extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchStun();

    private EnchStun() {
        super("Stun", 3, EnchantmentTarget.WEAPON);
        setRarity(80);
        setRarityFactor(20);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        final RNG rng = new RNG();
        if (rng.calcChance(35 + (level * 5))) {
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (level > 2 ? 3 : 2) * 20, 10, false, true, true));
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, (level > 2 ? 3 : 2) * 20, 10, false, true, true));
            if (e.getEntity() instanceof Player) {

                Remain.sendTitle((Player) e.getEntity(), 0, (level > 2 ? 4 : 3) * 20, 1,
                        "&c&lStunned!", "&cCannot move from " + e.getDamager().getName() + "\'s stun!");
            }
        }

    }

    public String setDescription() {
        return "40% chance to stun the target on first strike. Higher levels have a higher chance of stunning.";
    }


}
