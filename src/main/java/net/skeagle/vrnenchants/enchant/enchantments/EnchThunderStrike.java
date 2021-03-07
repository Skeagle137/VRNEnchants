package net.skeagle.vrnenchants.enchant.enchantments;

import net.minecraft.server.v1_16_R3.EntityLiving;
import net.skeagle.vrnenchants.VRNMain;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.*;

public class EnchThunderStrike extends BaseEnchant implements Listener {

    private static final Enchantment instance = new EnchThunderStrike();

    public EnchThunderStrike() {
        super("Thunder Strike", 3, EnchantmentTarget.WEAPON, VRNEnchants.VRN.VENOM.getEnch());
        setRarity(Rarity.MYTHICAL);
    }

    private static final Map<LightningStrike, Player> lightning_map = new HashMap<>();

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity) || !(damager instanceof Player)) return;
        Player p = (Player) damager;

        if (new RNG().calcChance(16, 4, level)) {
            LightningStrike strike = p.getWorld().strikeLightning(e.getEntity().getLocation());
            lightning_map.put(strike, p);
            Bukkit.getScheduler().runTaskLater(VRNMain.getInstance(), () -> lightning_map.remove(strike), 200);
        }
    }

    @EventHandler
    public void onDamageLightning(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof LightningStrike)) return;
        LightningStrike strike = (LightningStrike) e.getDamager();
        if (e.getEntity() instanceof ExperienceOrb)
            System.out.println(e.getCause());
        if (!lightning_map.containsKey(strike)) return;
        Player p = lightning_map.get(strike);
        if (!(e.getEntity() instanceof LivingEntity) || e.getEntity().getType() == EntityType.DROPPED_ITEM || e.getEntity().getType() == EntityType.EXPERIENCE_ORB) {
            e.setCancelled(true);
            return;
        }
        System.out.println("HERE");
        if (e.getEntity() == p) {
            e.setCancelled(true);
            return;
        }
        if (((LivingEntity) e.getEntity()).getHealth() - e.getFinalDamage() > 0) return;
        e.setCancelled(true);
        ((LivingEntity) e.getEntity()).setHealth(0.1);
        ((LivingEntity) e.getEntity()).damage(0.5, p);
    }

    @EventHandler
    public void onCombustLightning(EntityCombustByEntityEvent e) {
        if (!(e.getCombuster() instanceof LightningStrike)) return;
        LightningStrike strike = (LightningStrike) e.getCombuster();
        if (!lightning_map.containsKey(strike)) return;
        if (!(e.getEntity() instanceof LivingEntity) || e.getEntity().getType() == EntityType.DROPPED_ITEM || e.getEntity().getType() == EntityType.EXPERIENCE_ORB)
            e.setCancelled(true);
    }

    @EventHandler
    public void onDeathLightning(EntityDeathEvent e) {
        if (!lightning_map.containsValue(e.getEntity().getKiller())) return;
        boolean found_killer = false;
        for (LightningStrike strike : lightning_map.keySet()) {
            if (lightning_map.get(strike) == e.getEntity().getKiller())
                found_killer = true;
        }
        if (!found_killer) return;
        Bukkit.getScheduler().runTaskLater(VRNMain.getInstance(), () -> {
            int i = 0;
            while (i < e.getDroppedExp()) {
                e.getEntity().getWorld().spawn(e.getEntity().getLocation(), ExperienceOrb.class);
                i++;
            }
        }, 25);
    }

    public String setDescription() {
        return "Chance to hit the target with lightning";
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (e.getCause() != BlockIgniteEvent.IgniteCause.LIGHTNING || e.getIgnitingEntity() == null) return;

        if (lightning_map.containsKey(e.getIgnitingEntity()))
                e.setCancelled(true);
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
