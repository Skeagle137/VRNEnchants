package net.skeagle.vrnenchants.enchant;

import net.skeagle.vrnenchants.listener.ProjectileTracker;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import java.util.Map;
import java.util.function.BiConsumer;

public class EnchantListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof LivingEntity)) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.THORNS) return;
        run((LivingEntity) e.getDamager(), (enchant, level) ->
                enchant.onDamage(level, (LivingEntity) e.getDamager(), e));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        run(e.getPlayer(), (enchant, level) -> {
            if (e.getHand() == EquipmentSlot.OFF_HAND) return;
            if (e.getClickedBlock() != null && e.getClickedBlock().getType().isInteractable()) return;
            enchant.onInteract(level, e);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreakBlock(BlockBreakEvent e) {
        run(e.getPlayer(), (enchant, level) -> enchant.onBreakBlock(level, e));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onShoot(ProjectileLaunchEvent e) {
        final ProjectileSource projectileSource = e.getEntity().getShooter();
        if (!(projectileSource instanceof LivingEntity)) return;
        final LivingEntity shooter = (LivingEntity) projectileSource;
        run(shooter, (enchant, level) -> enchant.onShoot(level, shooter, e));
        ProjectileTracker.track(e.getEntity(), inground -> run(shooter, (enchant, level) -> enchant.onHit(level, shooter, inground)));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamaged(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        runArmor((Player) e.getEntity(), (enchant, level) -> enchant.onDamaged(level, (Player) e.getEntity(), e));
    }

    @SuppressWarnings("all")
    private void run(LivingEntity entity, BiConsumer<BaseEnchant, Integer> ench) {
        final ItemStack i = entity.getEquipment().getItemInHand();
        if (i == null) return;
        for (final Map.Entry<BaseEnchant, Integer> e : BaseEnchant.getEnchants(i).entrySet())
            ench.accept(e.getKey(), e.getValue());
    }

    private void runArmor(LivingEntity entity, BiConsumer<BaseEnchant, Integer> ench) {
        for (ItemStack i : entity.getEquipment().getArmorContents()) {
            if (i == null || i.getType() == Material.AIR) return;
            for (final Map.Entry<BaseEnchant, Integer> e : BaseEnchant.getEnchants(i).entrySet())
                if (e.getKey().getItemTarget().includes(i.getType()))
                    ench.accept(e.getKey(), e.getValue());
        }
    }
}
