package net.skeagle.vrnenchants.listener;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class FishingListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFish(PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            int rand = VRNUtil.rng(1, 200);
            if (rand != 50) return;
            ItemStack i = randomizeEnchant();
            ((Item) e.getCaught()).setItemStack(i);
        }
    }

    private ItemStack randomizeEnchant() {
        Rarity randRarity = new RNG.Randomizer<Rarity>()
                .addEntry(Rarity.COMMON, 2200)
                .addEntry(Rarity.UNCOMMON, 1200)
                .addEntry(Rarity.RARE, 500)
                .addEntry(Rarity.EPIC, 200)
                .addEntry(Rarity.LEGENDARY, 60)
                .addEntry(Rarity.MYTHICAL, 18)
                .addEntry(Rarity.COSMIC, 2).build();
        ArrayList<VRNEnchants.VRN> sameRarity = new ArrayList<>();
        for (VRNEnchants.VRN vrn : VRNEnchants.VRN.values())
            if (((BaseEnchant) vrn.getEnch()).getRarity() == randRarity)
                sameRarity.add(vrn);
        int randEnch = VRNUtil.rng(0, sameRarity.size() - 1);
        BaseEnchant ench = (BaseEnchant) VRNEnchants.VRN.valueOf(sameRarity.get(randEnch).toString()).getEnch();
        int randLevel;
        if (ench.getRarity().getIndividualPoints() < 15) { //not legendary or higher
            randLevel = new RNG.Randomizer<Integer>()
                    .addEntry(1, 100)
                    .addEntry(2, 70)
                    .addEntry(3, 25)
                    .addEntry(4, 5).build();
        }
        else {
            randLevel = new RNG.Randomizer<Integer>()
                    .addEntry(1, 80)
                    .addEntry(2, 30)
                    .addEntry(3, 2).build();
        }
        if (randLevel > ench.getMaxLevel()) randLevel = ench.getMaxLevel();
        return BaseEnchant.generateEnchantBook(ench, randLevel);
    }
}
