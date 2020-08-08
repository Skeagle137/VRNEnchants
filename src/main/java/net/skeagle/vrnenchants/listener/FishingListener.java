package net.skeagle.vrnenchants.listener;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static net.skeagle.vrnenchants.util.VRNUtil.say;

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
        int randEnch = VRNUtil.rng(1, VRNEnchants.VRN.values().length);
        BaseEnchant ench = ((BaseEnchant) VRNEnchants.VRN.values()[randEnch].getEnch());
        int randLevel = VRNUtil.rng(1, ench.getMaxLevel());
        int points = ench.getRarity() + (ench.getRarityFactor() * randLevel);
        int upperbound;
        if (points >= Rarity.RARE.getIndividualPoints()) {
            upperbound = 6;
        }
        else if (points >= Rarity.EPIC.getIndividualPoints()) {
            upperbound = 24;
        }
        else if (points >= Rarity.LEGENDARY.getIndividualPoints()) {
            upperbound = 48;
        }
        else if (points >= Rarity.MYTHICAL.getIndividualPoints()) {
            return null;
        }
        else {
            upperbound = 3;
        }
        upperbound = upperbound + (randLevel == ench.getMaxLevel() ? (ench.getMaxLevel() > 1 ? 12 : 0) : 0);
        random = VRNUtil.rng(1, upperbound);
        if (random != 1) return null;
        return BaseEnchant.generateEnchantBook(ench, randLevel);

    }
}
