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

import static net.skeagle.vrnenchants.enchant.BaseEnchant.updateLore;

public class AnvilRepairListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onAnvil(final PrepareAnvilEvent e) {
        final AnvilInventory inv = e.getInventory();
        final ItemStack the_item = inv.getItem(0);
        final ItemStack book = inv.getItem(1);
        if (the_item == null || the_item.getType() == Material.AIR) {
            return;
        }
        if (book != null && book.getType() != Material.ENCHANTED_BOOK && book.getAmount() > 1) {
            return;
        }
        if (the_item.getType() != Material.ENCHANTED_BOOK && the_item.getAmount() > 1) {
            return;
        }
        ItemStack result = e.getResult();
        if (result == null || result.getType() == Material.AIR) {
            result = new ItemStack(the_item);
        }
        int cost = inv.getRepairCost();
        final Map<BaseEnchant, Integer> enchAdd = new HashMap<>();
        for (final Map.Entry<BaseEnchant, Integer> en : BaseEnchant.getEnchants(the_item).entrySet()) {
            enchAdd.merge(en.getKey(), en.getValue(), Integer::sum);
        }
        if (book != null && (book.getType() == Material.ENCHANTED_BOOK || book.getType() == the_item.getType())) {
            for (final Map.Entry<BaseEnchant, Integer> en : BaseEnchant.getEnchants(book).entrySet()) {
                enchAdd.merge(en.getKey(), en.getValue(), (oldLvl, newLvl) -> (oldLvl.equals(newLvl)) ? (oldLvl + 1) : Math.max(oldLvl, newLvl));
            }
        }
        for (final Map.Entry<BaseEnchant, Integer> ent : enchAdd.entrySet()) {
            final int lvl = Math.min(ent.getKey().getMaxLevel(), ent.getValue());
            if (BaseEnchant.applyEnchant(result, ent.getKey(), lvl)) {
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
