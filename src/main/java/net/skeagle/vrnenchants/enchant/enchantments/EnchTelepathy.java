package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.VRNEnchants;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;

@EnchDescription("Items go straight into your inventory when broken.")
public class EnchTelepathy extends BaseEnchant {

    private static final Enchantment instance = new EnchTelepathy();

    public EnchTelepathy() {
        super("Telepathy", 1, Target.PICKAXES, Target.AXES, Target.HOES, Target.SHOVELS);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onBreakBlock(int level, BlockBreakEvent e) {
        System.out.println(e.getBlock().getState().getType() == Material.SHULKER_BOX);
        System.out.println(e.getBlock().getState() instanceof ShulkerBox);
        //if (e.getBlock().getState().getType() == Material.SHULKER_BOX) return;
        /*if (BaseEnchant.hasEnchant(e.getPlayer().getEquipment().getItemInMainHand(), EnchAutoSmelt.getInstance()))
            if (e.getBlock().getType() == Material.IRON_ORE || e.getBlock().getType() == Material.GOLD_ORE) return; //handled through auto smelt
        e.setDropItems(false);
        if (e.getBlock().getBlockData() instanceof org.bukkit.block.data.type.Jukebox) {
            ItemStack disc = ((Jukebox) e.getBlock().getState()).getRecord();
            if (e.getPlayer().getInventory().firstEmpty() != -1)
                e.getPlayer().getInventory().addItem(disc);
            else
                e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), disc);
        }
        if (e.getBlock().getState() instanceof InventoryHolder)
            ((InventoryHolder) e.getBlock().getState()).getInventory().forEach(i -> {
                if (i != null)
                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), i);
            });
        List<ItemStack> dropNaturally = new ArrayList<>();
        for (ItemStack item : e.getBlock().getDrops()) {
            if (e.getPlayer().getInventory().firstEmpty() != -1 || e.getPlayer().getInventory().first(item) != -1)
                e.getPlayer().getInventory().addItem(item);
            else
                dropNaturally.add(item);
        }
        dropNaturally.forEach(i -> e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), i));
         */
        //List<UUID> itemsAround = new ArrayList<>();
        Bukkit.getScheduler().runTaskLater(VRNEnchants.getInstance(), () -> {
            for (Entity en : getItemsNear(e.getBlock())) {
                if (!(en instanceof Item)) continue;
                Item i = (Item) en;
                i.setVelocity(new Vector());
                i.setPickupDelay(0);
                i.teleport(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5));
                System.out.println("item: " + i.getItemStack().getType());
                //itemsAround.add(i.getUniqueId());
            }
        }, 1);


        Bukkit.getScheduler().runTaskLater(VRNEnchants.getInstance(), () -> {
            for (Entity en : getItemsNear(e.getBlock())) {
                if (!(en instanceof Item)) continue;
                Item i = (Item) en;
                i.setVelocity(new Vector(0, 0, 0));
                i.setPickupDelay(0);
                i.teleport(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5));
                ItemStack item = i.getItemStack();
                if ((e.getPlayer().getInventory().firstEmpty() != -1 || e.getPlayer().getInventory().first(item) != -1)/* && !itemsAround.contains(i.getUniqueId())*/)
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
