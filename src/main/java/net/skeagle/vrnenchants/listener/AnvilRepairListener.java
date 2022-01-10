package net.skeagle.vrnenchants.listener;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Target;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.*;

import static net.skeagle.vrnenchants.enchant.BaseEnchant.updateLore;

public class AnvilRepairListener implements Listener {

    private final ItemStack warningItem;

    public AnvilRepairListener() {
        warningItem = new ItemBuilder(Material.PAPER).setName("&#ff0000&lWarning!").glint(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAnvil(PrepareAnvilEvent e) {
        AnvilInventory inv = e.getInventory();
        ItemStack the_item = inv.getItem(0);
        ItemStack book = inv.getItem(1);
        if (the_item == null || the_item.getType() == Material.AIR)
            return;
        if (book != null && book.getType() != Material.ENCHANTED_BOOK && book.getAmount() > 1)
            return;
        if (the_item.getType() != Material.ENCHANTED_BOOK && the_item.getAmount() > 1)
            return;

        ItemStack result = e.getResult();
        if (result == null || result.getType() == Material.AIR)
            result = new ItemStack(the_item);

        int cost = inv.getRepairCost();
        Map<BaseEnchant, Integer> enchs = new HashMap<>();
        for (Map.Entry<BaseEnchant, Integer> en : BaseEnchant.getEnchants(the_item).entrySet())
            enchs.merge(en.getKey(), en.getValue(), Integer::sum);

        List<String> conflicting_targets = new ArrayList<>();
        List<String> conflicting_enchants = new ArrayList<>();
        List<BaseEnchant> cant_combine = new ArrayList<>();

        if (book != null && (book.getType() == Material.ENCHANTED_BOOK || book.getType() == the_item.getType())) {
            for (Map.Entry<BaseEnchant, Integer> en : BaseEnchant.getEnchants(book).entrySet()) {
                if (enchs.keySet().stream().anyMatch(ench -> !Target.matches(ench, en.getKey().getTargets()) ||
                        !en.getKey().canEnchantItem(the_item) || en.getKey().conflictsWith(ench))) {
                    if (cant_combine.contains(en.getKey())) continue;
                    cant_combine.add(en.getKey());
                    result = warningItem;
                }
                else {
                    enchs.merge(en.getKey(), en.getValue(), (oldLvl, newLvl) -> (oldLvl.equals(newLvl)) ? (oldLvl + 1) : Math.max(oldLvl, newLvl));
                }
            }
        }

        if (book != null && the_item.getType() != Material.ENCHANTED_BOOK) {
            for (Map.Entry<BaseEnchant, Integer> en : enchs.entrySet()) {
                if (!Target.matches(the_item, en.getKey().getTargets())) {
                    conflicting_targets.add("&c- " + en.getKey().getName());
                    result = new ItemStack(Material.PAPER);
                }
            }
            for (Map.Entry<BaseEnchant, Integer> en : enchs.entrySet()) {
                if (!en.getKey().canEnchantItem(the_item)) {
                    conflicting_enchants.add("&c- " + en.getKey().getName());
                    result = new ItemStack(Material.PAPER);
                }
            }
        }

        if (result.getType() != Material.PAPER) {
            for (Map.Entry<BaseEnchant, Integer> en : enchs.entrySet()) {
                int lvl = Math.min(en.getKey().getMaxLevel(), en.getValue());
                if (BaseEnchant.applyEnchant(result, en.getKey(), lvl))
                    cost += lvl;
            }
        }

        if (!the_item.equals(result) && result.getType() != Material.PAPER) {
            updateLore(result);
            e.setResult(result);
            int finalCost = cost;
            Task.syncDelayed(() -> inv.setRepairCost(finalCost));
        } else if (result.getType() == Material.PAPER) {
            List<String> lore = new ArrayList<>();
            if (the_item.getType() == Material.ENCHANTED_BOOK) {
                if (cant_combine.size() > 0) {
                    lore.add("&4&lThese enchants cannot be combined:");
                    cant_combine.forEach(en -> lore.add("&c- " + en.getName()));
                }
            }
            else {
                if (conflicting_targets.size() > 0) {
                    lore.add("&4&lThese enchants cannot be applied to " +
                            the_item.getType().toString().toLowerCase().replaceAll("_", " ") + ":");
                    lore.addAll(conflicting_targets);
                }
                if (conflicting_enchants.size() > 0) {
                    lore.add("&4&lThis item conflicts with the following enchants:");
                    lore.addAll(conflicting_enchants);
                }
            }
            e.setResult(new ItemBuilder(warningItem).addLore(lore));
        }
    }

    @EventHandler
    public void onClickAnvil(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !(e.getClickedInventory() instanceof AnvilInventory inv)) return;
        if (inv.getItem(2) != null && inv.getItem(2).isSimilar(warningItem)) {
            e.setCancelled(true);
        }
    }
}
