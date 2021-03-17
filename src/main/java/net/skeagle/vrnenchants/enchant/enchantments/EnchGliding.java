package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.VRNEnchants;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnchGliding extends BaseEnchant implements Listener {

    private static final Enchantment instance = new EnchGliding();

    public EnchGliding() {
        super("Gliding", 6, EnchantmentTarget.ARMOR_LEGS);
        setRarity(Rarity.MYTHICAL);
    }

    private final List<UUID> cooldown = new ArrayList<>();

    @EventHandler
    protected void onSneak(final PlayerToggleSneakEvent e) {
        if (!e.isSneaking() || cooldown.contains(e.getPlayer().getUniqueId())) return;
        final ItemStack item = e.getPlayer().getEquipment().getLeggings();
        if (item == null || !hasEnchant(item, this)) return;
        if (e.getPlayer().getFallDistance() > 55 - (5 * getEnchants(item).get(this)))
            if (e.getPlayer().getPotionEffect(PotionEffectType.SLOW_FALLING) == null) {
                cooldown.add(e.getPlayer().getUniqueId());
                e.getPlayer().setVelocity(e.getPlayer().getVelocity().setY(-0.05));
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 5, 1, false, false, false));
                e.getPlayer().getWorld().spawnParticle(Particle.END_ROD, e.getPlayer().getLocation().clone().add(0, -3.2, 0), 40, 0.5, 0.1, 0.5, 0.02);
                Bukkit.getScheduler().runTaskLater(VRNEnchants.getInstance(), () -> cooldown.remove(e.getPlayer().getUniqueId()), 20 * 15);
            }
    }

    public String setDescription() {
        return "Sneaking when falling for a certain amount of time will grant you slow fall.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}