package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@EnchDescription("Items go straight into your inventory when broken.")
public class EnchTelepathy extends BaseEnchant {

    private static final Enchantment instance = new EnchTelepathy();

    public EnchTelepathy() {
        super("Telepathy", 1, Target.PICKAXES, Target.AXES, Target.HOES, Target.SHOVELS);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onBreakBlock(int level, BlockBreakEvent e) {
        e.setDropItems(false);
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
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
