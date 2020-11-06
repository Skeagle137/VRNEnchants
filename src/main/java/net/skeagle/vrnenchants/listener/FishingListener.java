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
            Item caught = ((Item) e.getCaught());
            ItemStack i = new ItemStack(Material.ENCHANTED_BOOK);
            if (caught.getItemStack().equals(i)) {
                int rand = VRNUtil.rng(1, 100);
                if (rand > 25) return;
                i = randomizeEnchant();
                if (i == null) return;
                caught.setItemStack(i);
            }
        }
    }

    private ItemStack randomizeEnchant() {
        Rarity randRarity = new RNG.Randomizer<Rarity>()
                .addEntry(Rarity.COMMON, 2200)
                .addEntry(Rarity.UNCOMMON, 1200)
                .addEntry(Rarity.RARE, 500)
                .addEntry(Rarity.EPIC, 120)
                .addEntry(Rarity.LEGENDARY, 50)
                .addEntry(Rarity.MYTHICAL, 12)
                .addEntry(Rarity.COSMIC, 1).build();
        ArrayList<BaseEnchant> sameRarity = new ArrayList<>();
        for (VRNEnchants.VRN vrn : VRNEnchants.VRN.values()) {
            if (((BaseEnchant) vrn.getEnch()).getRarity() == randRarity)
                sameRarity.add((BaseEnchant) vrn.getEnch());
        }
        int randEnch = VRNUtil.rng(0, sameRarity.size());
        BaseEnchant ench = ((BaseEnchant) VRNEnchants.VRN.values()[randEnch].getEnch());
        int randLevel = new RNG.Randomizer<Integer>()
                .addEntry(1, 80)
                .addEntry(2, 50)
                .addEntry(3, 8)
                .addEntry(4, 2).build();
        return BaseEnchant.generateEnchantBook(ench, randLevel);
    }
}
