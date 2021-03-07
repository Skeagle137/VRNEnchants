package net.skeagle.vrnenchants.listener;

import net.skeagle.vrnenchants.VRNMain;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getServer;

public class EnchantmentTableListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEnchantEnchantmentTable(final EnchantItemEvent e) {
        System.out.println("running");
        final ItemStack target = e.getItem();
        boolean enchantAdded = false;
        int randAmount = new RNG.Randomizer<Integer>()
                .addEntry(1, 150)
                .addEntry(2, 70)
                .addEntry(3, 10).build();
        for (int i = 0; i < 3; ++i) {
            //int custom_chance = VRNUtil.rng(1, 100);
            //if (custom_chance != 50) return;
            Rarity randRarity = new RNG.Randomizer<Rarity>()
                    .addEntry(Rarity.COMMON, 1200)
                    .addEntry(Rarity.UNCOMMON, 800)
                    .addEntry(Rarity.RARE, 500)
                    .addEntry(Rarity.EPIC, 120)
                    .addEntry(Rarity.LEGENDARY, 30)
                    .addEntry(Rarity.MYTHICAL, 8)
                    .addEntry(Rarity.COSMIC, 1).build();
            ArrayList<VRNEnchants.VRN> sameRarity = new ArrayList<>();
            for (VRNEnchants.VRN vrn : VRNEnchants.VRN.values())
                if (((BaseEnchant) vrn.getEnch()).getRarity() == randRarity)
                    sameRarity.add(vrn);
            int randEnch = VRNUtil.rng(0, sameRarity.size() - 1);
            BaseEnchant ench = (BaseEnchant) VRNEnchants.VRN.valueOf(sameRarity.get(randEnch).toString()).getEnch();
            int randLevel;
            if (ench.getRarity().getIndividualPoints() < 15) //not legendary or higher
                randLevel = new RNG.Randomizer<Integer>()
                        .addEntry(1, 140)
                        .addEntry(2, 100)
                        .addEntry(3, 25)
                        .addEntry(4, 10)
                        .addEntry(5, 2).build();
            else
                randLevel = new RNG.Randomizer<Integer>()
                        .addEntry(1, 80)
                        .addEntry(2, 20)
                        .addEntry(3, 1).build();
            if (randLevel > ench.getMaxLevel()) randLevel = ench.getMaxLevel();
            if (BaseEnchant.canEnchant(target, ench, randLevel)) {
                e.getEnchantsToAdd().put(ench, randLevel);
                enchantAdded = true;
            }
        }
        if (enchantAdded) {
            System.out.println("gets here");
            final ItemStack result = e.getInventory().getItem(0);
            if (result == null) return;
            getServer().getScheduler().runTask(VRNMain.getInstance(), () -> {
                e.getEnchantsToAdd().forEach((en, lvl) -> {
                    BaseEnchant.applyEnchant(result, en, lvl);
                    System.out.println("this is run");
                });
                BaseEnchant.updateLore(result);
                e.getInventory().setItem(0, result);
            });
        }
    }
}
