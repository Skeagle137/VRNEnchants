package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.VRNMain;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.skeagle.vrnenchants.util.VRNUtil.sayNoPrefix;


public class EnchExecute extends BaseEnchant {

    private static final Enchantment instance = new EnchExecute();

    private EnchExecute() {
        super("Execute", 2, EnchantmentTarget.WEAPON);
        setRarity(Rarity.MYTHICAL);
    }

    private final List<UUID> hitList = new ArrayList<>();

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        if (e.isCancelled() || hitList.contains(damager.getUniqueId())) return;
        hitList.add(damager.getUniqueId());
        Bukkit.getScheduler().runTask(VRNMain.getInstance(), () -> hitList.remove(damager.getUniqueId()));
        LivingEntity en = (LivingEntity) e.getEntity();
        if (en.getHealth() < (level + 2)) {
            en.setHealth(0.1);
            en.damage(0.5, damager);
            damager.getWorld().spawnParticle(Particle.FLAME, en.getLocation().clone().add(0, 1, 0), 10, 0.3, 0.3, 0.3, 0.1);
            damager.getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.6f, 0.8f);
            if (e.getEntity() instanceof Player)
                sayNoPrefix(e.getEntity(), "&c&lExecuted &7by " + damager.getName() + "'s insta-kill below &c" + ((level + 2) / 2) + "❤");
        }
    }

    public String setDescription() {
        return "Immediately kills the target if it has a specific amount of health left.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
