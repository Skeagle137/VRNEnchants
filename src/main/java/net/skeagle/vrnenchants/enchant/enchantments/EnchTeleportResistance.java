package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EnchDescription("Gives immunity to damage from teleporting with ender pearls.")
public class EnchTeleportResistance extends BaseEnchant implements Listener {

    private static final Enchantment instance = new EnchTeleportResistance();

    public EnchTeleportResistance() {
        super("Teleport Resistance", 1, Target.BOOTS);
        setRarity(Rarity.EPIC);
    }

    private static final List<UUID> pearlMap = new ArrayList<>();

    @Override
    protected void onDamaged(int level, Player p, EntityDamageEvent e) {
        if (pearlMap.contains(p.getUniqueId()) && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
            pearlMap.remove(p.getUniqueId());
        }
    }

    @EventHandler
    protected void onTeleport(final PlayerTeleportEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
        final ItemStack item = e.getPlayer().getEquipment().getBoots();
        if (item == null || !hasEnchant(item, this)) return;
        pearlMap.add(e.getPlayer().getUniqueId());
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
