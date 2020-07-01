package net.skeagle.vrnenchants.enchant;

import net.skeagle.vrnenchants.listener.ProjectileTracker;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.mineacademy.fo.EntityUtil;
import java.util.Map;
import java.util.function.BiConsumer;

public class EnchantListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof LivingEntity)) return;
        run((LivingEntity) e.getDamager(), (enchant, level) ->
                enchant.onDamage(level, (LivingEntity) e.getDamager(), e));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
            run(e.getPlayer(), (enchant, level) -> {
                if (e.getHand() == EquipmentSlot.OFF_HAND) return;
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
        if (projectileSource instanceof LivingEntity) {
            final LivingEntity shooter = (LivingEntity) projectileSource;
            run(shooter, (enchant, level) -> enchant.onShoot(level, shooter, e));
            ProjectileTracker.track(e.getEntity(), inground -> run(shooter, (enchant, level) -> enchant.onHit(level, shooter, inground)));
        }
    }

    private void run(LivingEntity source, BiConsumer<BaseEnchant, Integer> ench) {
        final ItemStack i = source.getEquipment().getItemInMainHand();
        if (i == null) return;
        for (final Map.Entry<BaseEnchant, Integer> e : BaseEnchant.getEnchants(i).entrySet())
            ench.accept(e.getKey(), e.getValue());
    }
}
