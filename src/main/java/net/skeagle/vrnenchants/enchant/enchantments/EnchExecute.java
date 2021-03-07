package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static net.skeagle.vrnenchants.util.VRNUtil.sayNoPrefix;

public class EnchExecute extends BaseEnchant {

    private static final Enchantment instance = new EnchExecute();

    private EnchExecute() {
        super("Execute", 2, EnchantmentTarget.WEAPON);
        setRarity(Rarity.MYTHICAL);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        if (((LivingEntity) e.getEntity()).getHealth() < level) {
            ((LivingEntity) e.getEntity()).setHealth(0.1);
            ((LivingEntity) e.getEntity()).damage(0.5);
            damager.getWorld().spawnParticle(Particle.FLAME, e.getEntity().getLocation().clone().add(0, 1, 0), 10, 0.3, 0.3, 0.3, 0.1);
            damager.getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.6f, 0.8f);
            if (e.getEntity() instanceof Player)
                sayNoPrefix(e.getEntity(), "&c&lExecuted &7by " + damager.getName() + "'s insta-kill below &c" + level + "❤");
        }
    }

    public String setDescription() {
        return "Immediately kills the target if it has a specific amount of health left.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
