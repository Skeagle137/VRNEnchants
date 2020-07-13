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
import org.mineacademy.fo.remain.CompMaterial;

import java.util.HashMap;
import java.util.Map;

import static net.skeagle.vrnenchants.enchant.BaseEnchant.applyEnchant;
import static net.skeagle.vrnenchants.enchant.BaseEnchant.updateLore;

public class AnvilRepairListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvil(final PrepareAnvilEvent e) {
        final AnvilInventory inv = e.getInventory();
        final ItemStack source = inv.getItem(0);
        final ItemStack i = inv.getItem(1);
        if (source == null || source.getType() == CompMaterial.AIR.getMaterial()) {
            return;
        }
        ItemStack result = e.getResult();
        if (result == null || result.getType() == CompMaterial.AIR.getMaterial()) {
            result = new ItemStack(source);
        }
        int cost = inv.getRepairCost();
        Map<BaseEnchant, Integer> enchAdd = new HashMap<>();
        for (final Map.Entry<BaseEnchant, Integer> en : BaseEnchant.getEnchants(source).entrySet()) {
            enchAdd.merge(en.getKey(), en.getValue(), Integer::sum);
        }
        if (i != null && (i.getType() == Material.ENCHANTED_BOOK || i.getType() == source.getType())) {
            for (final Map.Entry<BaseEnchant, Integer> ench : BaseEnchant.getEnchants(i).entrySet()) {
                enchAdd.merge(ench.getKey(), ench.getValue(), (oldLvl, newLvl) -> (oldLvl.equals(newLvl)) ? (oldLvl + 1) : Math.max(oldLvl, newLvl));
            }
        }
        for (final Map.Entry<BaseEnchant, Integer> ench : enchAdd.entrySet()) {
            final int lvl = Math.min(ench.getKey().getMaxLevel(), ench.getValue());
            if (applyEnchant(result, ench.getKey(), lvl)) {
                cost += lvl;
            }
        }
        if (!source.equals(result)) {
            updateLore(result);
            e.setResult(result);
            final int finalCost = cost;
            Bukkit.getScheduler().runTask(VRNMain.getInstance(), () -> inv.setRepairCost(finalCost));
        }
    }
}
