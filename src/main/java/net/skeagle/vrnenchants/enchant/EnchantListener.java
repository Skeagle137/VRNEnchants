package net.skeagle.vrnenchants.enchant;

import net.skeagle.vrnenchants.listener.ProjectileTracker;
import net.skeagle.vrnlib.misc.TimeUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Map;
import java.util.function.BiConsumer;

import static net.skeagle.vrnenchants.util.VRNUtil.say;

public class EnchantListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof LivingEntity)) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.THORNS) return;
        run((LivingEntity) e.getDamager(), (enchant, level) -> {
            prepareCooldown(enchant, level);
            if (hasCooldown(enchant, (LivingEntity) e.getDamager()))
                return;
            enchant.onDamage(level, (LivingEntity) e.getDamager(), e);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null && e.getClickedBlock().getType().isInteractable()) return;
        if (Target.isArmor(e.getItem())) return;
        if (e.getHand() == EquipmentSlot.HAND) {
            run(e.getPlayer(), (enchant, level) -> {
                prepareCooldown(enchant, level);
                if (hasCooldown(enchant, e.getPlayer())) {
                    if (enchant.isCooldownErrorVisible())
                        say(e.getPlayer(), "&cYou must wait " + TimeUtil.timeToMessage(enchant.getCooldownMap().get(e.getPlayer())) + " to use " + enchant.getName() + " again.");
                } else
                    enchant.onInteract(level, e);
            });
        }
        else if (e.getHand() == EquipmentSlot.OFF_HAND && e.getItem().getType() == Material.SHIELD) {
            runOffHand(e.getPlayer(), (enchant, level) -> {
                prepareCooldown(enchant, level);
                if (hasCooldown(enchant, e.getPlayer())) {
                    if (enchant.isCooldownErrorVisible())
                        say(e.getPlayer(), "&cYou must wait " + TimeUtil.timeToMessage(enchant.getCooldownMap().get(e.getPlayer())) + " to use " + enchant.getName() + " again.");
                } else
                    enchant.onInteract(level, e);
            });
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreakBlock(BlockBreakEvent e) {
        run(e.getPlayer(), (enchant, level) -> {
            prepareCooldown(enchant, level);
            if (hasCooldown(enchant, e.getPlayer()))
                return;
            enchant.onBreakBlock(level, e);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onShoot(ProjectileLaunchEvent e) {
        ProjectileSource projectileSource = e.getEntity().getShooter();
        if (!(projectileSource instanceof LivingEntity)) return;
        LivingEntity shooter = (LivingEntity) projectileSource;
        run(shooter, (enchant, level) -> {
            prepareCooldown(enchant, level);
            if (hasCooldown(enchant, shooter)) {
                if (enchant.isCooldownErrorVisible())
                    say(shooter, "&cYou must wait " + TimeUtil.timeToMessage(enchant.getCooldownMap().get(shooter)) + " to use " + enchant.getName() + " again.");
                return;
            }
            enchant.onShoot(level, shooter, e);
        });
        ProjectileTracker.track(e.getEntity(), inground -> run(shooter, (enchant, level) -> enchant.onHit(level, shooter, inground)));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        Player killer = e.getEntity().getKiller();
        run(killer, (enchant, level) -> {
            prepareCooldown(enchant, level);
            if (hasCooldown(enchant, killer))
                return;
            enchant.onKill(level, killer, e);
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamaged(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        runArmorAll((Player) e.getEntity(), (enchant, level) -> {
            prepareCooldown(enchant, level);
            if (!hasCooldown(enchant, (Player) e.getEntity()))
                enchant.onDamaged(level, (Player) e.getEntity(), e);
        });
        runOffHand((Player) e.getEntity(), (enchant, level) -> {
            prepareCooldown(enchant, level);
            if (!hasCooldown(enchant, (Player) e.getEntity()))
                enchant.onDamaged(level, (Player) e.getEntity(), e);
        });
    }

    @SuppressWarnings("all")
    private void run(LivingEntity entity, BiConsumer<BaseEnchant, Integer> ench) {
        final ItemStack i = entity.getEquipment().getItemInMainHand();
        if (i == null || i.getType() == Material.AIR) return;
        for (final Map.Entry<BaseEnchant, Integer> e : BaseEnchant.getEnchants(i).entrySet()) {
            if (!Target.matches(i, e.getKey().getTargets()) && isNotCreative(entity))
                return;
            ench.accept(e.getKey(), e.getValue());
        }
    }

    @SuppressWarnings("all")
    private void runOffHand(LivingEntity entity, BiConsumer<BaseEnchant, Integer> ench) {
        final ItemStack i = entity.getEquipment().getItemInOffHand();
        if (i == null || i.getType() == Material.AIR) return;
        for (final Map.Entry<BaseEnchant, Integer> e : BaseEnchant.getEnchants(i).entrySet()) {
            if (!Target.matches(i, e.getKey().getTargets()) && isNotCreative(entity))
                return;
            ench.accept(e.getKey(), e.getValue());
        }
    }

    private void runArmorAll(LivingEntity entity, BiConsumer<BaseEnchant, Integer> ench) {
        for (ItemStack i : entity.getEquipment().getArmorContents()) {
            if (i == null || i.getType() == Material.AIR) continue;
            for (Map.Entry<BaseEnchant, Integer> e : BaseEnchant.getEnchants(i).entrySet()) {
                if (!Target.matches(i, e.getKey().getTargets()) && isNotCreative(entity))
                    return;
                ench.accept(e.getKey(), e.getValue());
            }
        }
    }

    private void runArmor(LivingEntity entity, BiConsumer<BaseEnchant, Integer> ench, ItemStack i) {
        if (i == null || i.getType() == Material.AIR) return;
        for (Map.Entry<BaseEnchant, Integer> e : BaseEnchant.getEnchants(i).entrySet()) {
            if (!Target.matches(i, e.getKey().getTargets()) && isNotCreative(entity))
                return;
            ench.accept(e.getKey(), e.getValue());
        }
    }

    private void prepareCooldown(BaseEnchant ench, int level) {
        if (ench instanceof ICooldown) {
            int i = ((ICooldown) ench).cooldown(level);
            ench.setInitialCooldown(i);
        }
    }

    private boolean hasCooldown(BaseEnchant ench, LivingEntity e) {
        return ench.getCooldownMap().contains(e) && ench.getCooldown() != 0;
    }

    private boolean isNotCreative(LivingEntity en) {
        return !(en instanceof Player) || ((Player) en).getGameMode() != GameMode.CREATIVE;
    }
}
