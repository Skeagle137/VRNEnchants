package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.VRNEnchants;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.*;

import static net.skeagle.vrnenchants.util.VRNUtil.sayNoPrefix;


public class EnchExecute extends BaseEnchant {

    private static final Enchantment instance = new EnchExecute();

    private EnchExecute() {
        super("Execute", 2, EnchantmentTarget.WEAPON);
        setRarity(Rarity.MYTHICAL);
        setCooldownErrorVisible(false);
    }

    private final Map<UUID, UUID> hitList = new HashMap<>();

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity en = (LivingEntity) e.getEntity();
        if (hitList.get(damager.getUniqueId()) != null)
            if (hitList.get(damager.getUniqueId()).equals(e.getEntity().getUniqueId()))
                return;
        if (en.getHealth() < (level + 2)) {
            hitList.put(damager.getUniqueId(), en.getUniqueId());
            Bukkit.getScheduler().runTaskLater(VRNEnchants.getInstance(), () -> hitList.remove(damager.getUniqueId()), 100);
            en.damage(en.getHealth(), damager);
            e.getEntity().getWorld().spawnParticle(Particle.FLAME, e.getEntity().getLocation().clone().add(0, 1, 0), 10, 0.3, 0.3, 0.3, 0.1);
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.6f, 0.8f);
            if (e.getEntity() instanceof Player)
                sayNoPrefix(e.getEntity(), "&c&lExecuted &7by " + damager.getName() + "'s insta-kill below &c" + ((level + 2) / 2) + "❤");
        }
    }

    @Override
    protected void onKill(int level, Player killer, EntityDeathEvent e) {
    }

    public String setDescription() {
        return "Immediately kills the target if it has a specific amount of health left.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
