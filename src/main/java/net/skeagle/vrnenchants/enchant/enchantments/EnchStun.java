package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.skeagle.vrncommands.BukkitUtils.color;

@EnchDescription("Chance to stun the target on first strike.")
public class EnchStun extends BaseEnchant {

    private static final Enchantment instance = new EnchStun();

    private EnchStun() {
        super("Stun", 2, Target.SWORDS, Target.AXES);
        setRarity(Rarity.LEGENDARY);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(2, level)) {
            LivingEntity en = ((LivingEntity) e.getEntity());
            en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (level + 1) * 20, 6, false, false, false));
            en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, (level + 1) * 20, 6, false, false, false));
            if (e.getEntity() instanceof Player) {
                ((Player) e.getEntity()).playSound(e.getEntity().getLocation(), Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0f, 0.5f);
                ((Player) e.getEntity()).sendTitle(color("&c&lStunned!"), color("&cCannot move from " + e.getDamager().getName() + "'s stun!"),
                        0, (level + 1) * 20, 1);
            }
        }

    }

    public static Enchantment getInstance() {
        return instance;
    }


}
