package net.skeagle.vrnenchants.listener;

import net.skeagle.vrnenchants.VRNMain;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static net.skeagle.vrnenchants.enchant.BaseEnchant.applyEnchant;
import static net.skeagle.vrnenchants.enchant.BaseEnchant.updateLore;

public class AnvilRepairListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvil(final PrepareAnvilEvent e) {
        final AnvilInventory inv = e.getInventory();
        final ItemStack the_item = inv.getItem(0);
        final ItemStack the_book = inv.getItem(1);
        if (the_item == null || the_item.getType() == Material.AIR) {
            return;
        }
        ItemStack result = e.getResult();
        if (result == null || result.getType() == Material.AIR) {
            result = new ItemStack(the_item);
        }
        int cost = inv.getRepairCost();
        Map<BaseEnchant, Integer> enchAdd = new HashMap<>();
        for (final Map.Entry<BaseEnchant, Integer> en : BaseEnchant.getEnchants(the_item).entrySet()) {
            enchAdd.merge(en.getKey(), en.getValue(), Integer::sum);
        }
        if (the_book != null && (the_book.getType() == Material.ENCHANTED_BOOK || the_book.getType() == the_item.getType())) {
            for (final Map.Entry<BaseEnchant, Integer> ench : BaseEnchant.getEnchants(the_book).entrySet()) {
                enchAdd.merge(ench.getKey(), ench.getValue(), (oldLvl, newLvl) -> (oldLvl.equals(newLvl)) ? (oldLvl + 1) : Math.max(oldLvl, newLvl));
            }
        }
        for (final Map.Entry<BaseEnchant, Integer> ench : enchAdd.entrySet()) {
            final int lvl = Math.min(ench.getKey().getMaxLevel(), ench.getValue());
            if (applyEnchant(result, ench.getKey(), lvl)) {
                cost += lvl;
            }
        }
        if (!the_item.equals(result)) {
            updateLore(result);
            e.setResult(result);
            final int finalCost = cost;
            Bukkit.getScheduler().runTask(VRNMain.getInstance(), () -> inv.setRepairCost(finalCost));
        }
    }
}
