package net.skeagle.vrnenchants.enchant;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.mineacademy.fo.EntityUtil;
import java.util.Map;
import java.util.function.BiConsumer;

public class EnchantListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        final Entity damager = e.getDamager();

        if (damager instanceof LivingEntity) {
            execute((LivingEntity) damager, (enchant, level) -> enchant.onDamage(level, (LivingEntity) damager, e));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        execute(e.getPlayer(), (enchant, level) -> enchant.onInteract(level, e));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreakBlock(BlockBreakEvent e) {
        execute(e.getPlayer(), (enchant, level) -> enchant.onBreakBlock(level, e));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onShoot(ProjectileLaunchEvent e) {
        final ProjectileSource projectileSource = e.getEntity().getShooter();
        if (projectileSource instanceof LivingEntity) {
            final LivingEntity shooter = (LivingEntity) projectileSource;
            execute(shooter, (enchant, level) -> enchant.onShoot(level, shooter, e));
            EntityUtil.trackHit(e.getEntity(), hitEvent -> execute(shooter, (enchant, level) -> enchant.onHit(level, shooter, hitEvent)));
        }
    }

    private void execute(LivingEntity source, BiConsumer<BaseEnchant, Integer> ench) {
        final ItemStack i = source.getEquipment().getItemInMainHand();
        if (i == null) return;
        for (final Map.Entry<BaseEnchant, Integer> e : BaseEnchant.getEnchants(i).entrySet())
            ench.accept(e.getKey(), e.getValue());
    }
}
