package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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
        super("Stun", 2, EnchantmentTarget.WEAPON);
        setRarity(Rarity.LEGENDARY);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(2, level)) {
            LivingEntity en = ((LivingEntity) e.getEntity());
            en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (level > 2 ? 3 : 2) * 20, 6, false, false, false));
            en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, (level > 2 ? 3 : 2) * 20, 6, false, false, false));
            if (e.getEntity() instanceof Player) {
                ((Player) e.getEntity()).playSound(e.getEntity().getLocation(), Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0f, 0.5f);
                Remain.sendTitle((Player) e.getEntity(), 0, (level > 2 ? 4 : 3) * 20, 1,
                        "&c&lStunned!", "&cCannot move from " + e.getDamager().getName() + "'s stun!");
            }
        }

    }

    public String setDescription() {
        return "chance to stun the target on first strike. Higher levels have a higher chance of stunning.";
    }


}
