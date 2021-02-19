package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class EnchTeleportResistance extends BaseEnchant implements Listener {

    @Getter
    private static final Enchantment instance = new EnchTeleportResistance();

    public EnchTeleportResistance() {
        super("Teleport Immunity", 1, EnchantmentTarget.ARMOR_FEET);
        setRarity(Rarity.LEGENDARY);
    }

    @EventHandler
    protected void onTeleport(final PlayerTeleportEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
        final ItemStack item = e.getPlayer().getEquipment().getBoots();
        if (item == null || !hasEnchant(item, this)) return;
        Location to = e.getTo();
        if (e.isCancelled() || to == null) return;
        e.setCancelled(true);
        e.getPlayer().teleport(to);

    }

    public String setDescription() {
        return "Gives immunity to damage from teleporting with ender pearls.";
    }
}
