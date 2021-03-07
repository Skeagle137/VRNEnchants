package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.VRNMain;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class EnchThunderStrike extends BaseEnchant implements Listener {

    private static final Enchantment instance = new EnchThunderStrike();

    public EnchThunderStrike() {
        super("Thunder Strike", 3, EnchantmentTarget.WEAPON, VRNEnchants.VRN.VENOM.getEnch());
        setRarity(Rarity.MYTHICAL);
    }

    Map<UUID, LightningStrike> lightning_map = new HashMap<>();

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(8, 4, level)) {
            LightningStrike strike = damager.getWorld().strikeLightning(e.getEntity().getLocation());
            lightning_map.put(damager.getUniqueId(), strike);
            Bukkit.getScheduler().runTaskLater(VRNMain.getInstance(), () -> lightning_map.remove(damager.getUniqueId()), 60);
        }
    }

    @Override
    protected void onDamaged(int level, Player p, EntityDamageEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity en = (LivingEntity) e.getEntity();
        if (e.getCause() != EntityDamageEvent.DamageCause.LIGHTNING || !lightning_map.containsKey(p.getUniqueId()))
            return;
        if (en.getHealth() - e.getFinalDamage() > 0) return;
        e.setCancelled(true);
        en.setHealth(0.1);
        en.damage(0.5, p);
    }

    public String setDescription() {
        return "Chance to hit the target with lightning";
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (e.getCause() != BlockIgniteEvent.IgniteCause.LIGHTNING || e.getIgnitingEntity() == null) return;
        for (UUID uuid : lightning_map.keySet())
            if (lightning_map.get(uuid).getUniqueId().equals(e.getIgnitingEntity().getUniqueId()))
                e.setCancelled(true);
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
