package net.skeagle.vrnenchants.listener;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Target;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import net.skeagle.vrnlib.itemutils.ItemTrait;
import net.skeagle.vrnlib.itemutils.ItemUtils;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static net.skeagle.vrnenchants.enchant.BaseEnchant.updateLore;

public class AnvilRepairListener implements Listener {

    private final ItemBuilder warningItem;

    public AnvilRepairListener() {
        warningItem = new ItemBuilder(Material.PAPER).setName("&#ff0000&lWarning!").glint(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAnvil(PrepareAnvilEvent e) {
        AnvilInventory inv = e.getInventory();
        ItemStack item = inv.getItem(0);
        ItemStack applying = inv.getItem(1);
        if (item == null || item.getType() == Material.AIR)
            return;
        if (applying != null && ((applying.getType() != Material.ENCHANTED_BOOK && item.getType() != applying.getType()) || applying.getAmount() > 1))
            return;
        if (item.getType() != Material.ENCHANTED_BOOK && item.getAmount() > 1)
            return;

        ItemStack result = e.getResult();
        if (result == null || result.getType() == Material.AIR) {
            result = new ItemStack(item);
        }

        Map<BaseEnchant, Integer> itemEnchants = new HashMap<>();
        Map<BaseEnchant, Integer> applyingEnchants = new HashMap<>();

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            itemEnchants = BaseEnchant.getEnchants(item);
            for (Map.Entry<BaseEnchant, Integer> en : itemEnchants.entrySet()) {
                itemEnchants.merge(en.getKey(), en.getValue(), Integer::sum);
            }
        }

        List<Enchantment> cantCombine = new ArrayList<>();
        List<Enchantment> cantAdd = new ArrayList<>();

        if (applying != null) {
            ItemMeta applyingMeta = applying.getItemMeta();
            if (applyingMeta == null) return;
            applyingEnchants = BaseEnchant.getEnchants(applying);
            if (applyingEnchants.isEmpty()) return;

            for (Map.Entry<BaseEnchant, Integer> en : applyingEnchants.entrySet()) {
                if (!en.getKey().canEnchantItem(item)) {
                    if (cantCombine.contains(en.getKey())) continue;
                    cantCombine.add(en.getKey());
                } else if (item.getType() != Material.ENCHANTED_BOOK && !Target.matches(item, en.getKey().getTargets())) {
                    if (cantAdd.contains(en.getKey())) continue;
                    cantAdd.add(en.getKey());
                } else {
                    itemEnchants.merge(en.getKey(), en.getValue(), (oldLvl, newLvl) -> (oldLvl.equals(newLvl)) ? (oldLvl + 1) : Math.max(oldLvl, newLvl));
                }
            }
        }

        if (cantCombine.isEmpty() && cantAdd.isEmpty()) {
            int cost = inv.getRepairCost();
            for (Map.Entry<BaseEnchant, Integer> en : itemEnchants.entrySet()) {
                int lvl = Math.min(en.getKey().getMaxLevel(), en.getValue());
                if (BaseEnchant.applyEnchant(result, en.getKey(), lvl)) {
                    cost += lvl;
                }
            }
            updateLore(result);
            e.setResult(result);
            int finalCost = cost;
            Task.syncDelayed(() -> inv.setRepairCost(finalCost));
            return;
        }
        List<String> lore = new ArrayList<>();
        if (!cantCombine.isEmpty()) {
            lore.add("&4&lCannot combine due to conflicting enchants:");
            this.addEntries(lore, itemEnchants.keySet(), cantCombine);
        }
        if (!cantAdd.isEmpty()) {
            lore.add("&4&lThese enchants cannot be applied to " + item.getType().toString().toLowerCase().replaceAll("_", " ") + ":");
            this.addEntries(lore, itemEnchants.keySet(), cantAdd);
        }
        ItemBuilder stack = new ItemBuilder(warningItem.clone());
        lore.forEach(stack::addLore);
        e.setResult(stack);
    }

    private void addEntries(List<String> lore, Set<BaseEnchant> itemEnchants, List<Enchantment> conflicting) {
        itemEnchants.stream()
                .filter(conflicting::contains)
                .forEach(en -> {
                    this.addConflictingEntry(lore, en);
                    conflicting.remove(en);
                });
        conflicting.forEach(en -> this.addConflictingEntry(lore, en));
    }

    private void addConflictingEntry(List<String> lore, Enchantment enchant) {
        lore.add("&c- " + enchant.getKey().getKey().replaceAll("_", " "));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClickAnvil(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !(e.getClickedInventory() instanceof AnvilInventory inv) || inv.getItem(2) == null) return;
        if (e.getSlot() == 2 && ItemUtils.compare(inv.getItem(2), warningItem, ItemTrait.TYPE, ItemTrait.NAME, ItemTrait.ENCHANTMENTS)) {
            e.setCancelled(true);
        }
    }
}
