package net.skeagle.vrnenchants.listener;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FishingListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFish(PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Item caught = ((Item) e.getCaught());
            ItemStack i = new ItemStack(Material.ENCHANTED_BOOK);
            if (caught.getItemStack().equals(i)) {
                i = randomizeEnchant();
                if (i == null) return;
                caught.setItemStack(i);
            }
        }
    }

    private ItemStack randomizeEnchant() {
        int random;
        int randEnch = new Random().nextInt(VRNEnchants.VRN.values().length);
        BaseEnchant ench = ((BaseEnchant) VRNEnchants.VRN.values()[randEnch].getEnch());
        int randLevel = new Random().nextInt(ench.getMaxLevel() > 1 ? (ench.getMaxLevel() - 1 > 1 ? ench.getMaxLevel() - 1 : 2) : 1);
        if ((ench.getRarity() + (ench.getRarityFactor() * randLevel)) >= Rarity.RARE.getIndividualPoints()) {
            random = new Random().nextInt(5); //1 in 6
            if (random != 5) return null;
        }
        else if ((ench.getRarity() + (ench.getRarityFactor() * randLevel)) >= Rarity.EPIC.getIndividualPoints()) {
            random = new Random().nextInt(31); //1 in 32
            if (random != 31) return null;
        }
        else if ((ench.getRarity() + (ench.getRarityFactor() * randLevel)) >= Rarity.LEGENDARY.getIndividualPoints()) {
            random = new Random().nextInt(63); // 1 in 64
            if (random != 31) return null;
        }
        else if ((ench.getRarity() + (ench.getRarityFactor() * randLevel)) >= Rarity.MYTHICAL.getIndividualPoints()) {
            return null;
        }
        else {
            random = new Random().nextInt(2); //1 in 3
            if (random != 2) return null;
        }
        return BaseEnchant.generateEnchantBook(ench, randLevel);

    }
}
