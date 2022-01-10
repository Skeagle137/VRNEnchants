package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.VRNEnchants;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@EnchDescription("Items go straight into your inventory when broken.")
public class EnchTelepathy extends BaseEnchant {

    private static final Enchantment instance = new EnchTelepathy();

    public EnchTelepathy() {
        super("Telepathy", 1, Target.PICKAXES, Target.AXES, Target.HOES, Target.SHOVELS);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onBreakBlock(int level, BlockBreakEvent e) {
        List<UUID> itemsAround = new ArrayList<>();
        for (Entity en : e.getBlock().getWorld().getNearbyEntities(e.getBlock().getLocation().clone().add(0.5, 1.3, 0.5), 0.45, 0.38, 0.45, entity -> entity instanceof Item)) {
            if (!(en instanceof Item)) continue;
            Item i = (Item) en;
            itemsAround.add(i.getUniqueId());
        }

        Task.syncDelayed(() -> {
            for (Entity en : getItemsNear(e.getBlock())) {
                if (!(en instanceof Item)) continue;
                Item i = (Item) en;
                i.setVelocity(new Vector());
                i.setPickupDelay(0);
                i.teleport(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5));
            }
        }, 1);

        Task.syncDelayed(() -> {
            for (Entity en : getItemsNear(e.getBlock())) {
                if (!(en instanceof Item)) continue;
                Item i = (Item) en;
                ItemStack item = i.getItemStack();
                if ((e.getPlayer().getInventory().firstEmpty() != -1 || e.getPlayer().getInventory().first(item) != -1) && !itemsAround.contains(i.getUniqueId()))
                    i.teleport(e.getPlayer().getLocation());
            }
        }, 2);
    }

    private Collection<Entity> getItemsNear(Block b) {
        return b.getWorld().getNearbyEntities(b.getLocation().clone().add(0.5, 0.5, 0.5), 0.45, 0.45, 0.45, entity -> entity instanceof Item);
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
