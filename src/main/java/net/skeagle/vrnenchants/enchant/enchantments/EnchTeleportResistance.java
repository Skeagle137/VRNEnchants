package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.VRNMain;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnchTeleportResistance extends BaseEnchant implements Listener {

    private static final Enchantment instance = new EnchTeleportResistance();

    public EnchTeleportResistance() {
        super("Teleport Resistance", 1, EnchantmentTarget.ARMOR_FEET);
        setRarity(Rarity.LEGENDARY);
    }

    private final List<UUID> pearlMap = new ArrayList<>();

    @Override
    protected void onDamaged(int level, Player p, EntityDamageEvent e) {
        if (!pearlMap.contains(p.getUniqueId()) && e.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        e.setCancelled(true);

    }

    @Override
    protected void onShoot(int level, LivingEntity shooter, ProjectileLaunchEvent e) {
        if (e.getEntityType() != EntityType.ENDER_PEARL) return;
        final ItemStack item = shooter.getEquipment().getBoots();
        if (item == null || !hasEnchant(item, this)) return;
        pearlMap.add(shooter.getUniqueId());
    }

    @EventHandler
    protected void onTeleport(final PlayerTeleportEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
        final ItemStack item = e.getPlayer().getEquipment().getBoots();
        if (item == null || !hasEnchant(item, this)) return;
        Bukkit.getScheduler().runTaskLater(VRNMain.getInstance(), () -> pearlMap.remove(e.getPlayer().getUniqueId()), 10);
    }

    public String setDescription() {
        return "Gives immunity to damage from teleporting with ender pearls.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
