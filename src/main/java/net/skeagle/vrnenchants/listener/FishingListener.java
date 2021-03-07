package net.skeagle.vrnenchants.listener;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.enchant.enchantments.EnchFisherman;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
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
            int rand = VRNUtil.rng(1, 180);
            ItemStack hand = e.getPlayer().getEquipment().getItemInMainHand();
            int level = 0;
            if (BaseEnchant.hasEnchant(hand, Enchantment.LUCK))
                level = hand.getEnchantments().get(Enchantment.LUCK);
            if (rand > (1 + (level * 2))) return;
            ItemStack i = randomizeEnchant(e.getPlayer());
            ((Item) e.getCaught()).setItemStack(i);
        }
    }

    private ItemStack randomizeEnchant(Player p) {
        int level = 0;
        if (BaseEnchant.hasEnchant(p.getEquipment().getItemInMainHand(), EnchFisherman.getInstance()))
            level = BaseEnchant.getEnchants(p.getEquipment().getItemInMainHand()).get(EnchFisherman.getInstance());
        Rarity randRarity = new RNG.Randomizer<Rarity>()
                .addEntry(Rarity.COMMON, 1050 - (level * 40))
                .addEntry(Rarity.UNCOMMON, 900 + (level * 45))
                .addEntry(Rarity.RARE, 750 + (level * 25))
                .addEntry(Rarity.EPIC, 500 + (level * 20))
                .addEntry(Rarity.LEGENDARY, 250 + (level > 1 ? 30 : 0))
                .addEntry(Rarity.MYTHICAL, 75 + (level > 1 ? 20 : 0))
                .addEntry(Rarity.COSMIC, 10 + (level > 1 ? 10 : 0)).build();
        ArrayList<VRNEnchants.VRN> sameRarity = new ArrayList<>();
        for (VRNEnchants.VRN vrn : VRNEnchants.VRN.values())
            if (((BaseEnchant) vrn.getEnch()).getRarity() == randRarity)
                sameRarity.add(vrn);
        int randEnch = VRNUtil.rng(0, sameRarity.size() - 1);
        BaseEnchant ench = (BaseEnchant) VRNEnchants.VRN.valueOf(sameRarity.get(randEnch).toString()).getEnch();
        int randLevel;
        if (ench.getRarity().getIndividualPoints() < 15) { //not legendary or higher
            randLevel = new RNG.Randomizer<Integer>()
                    .addEntry(1, 80 - (level * 5))
                    .addEntry(2, 60 + (level * 5))
                    .addEntry(3, 30 + (level * 5))
                    .addEntry(4, 10 + (level > 1 ? 10 : 0)).build();
        }
        else {
            randLevel = new RNG.Randomizer<Integer>()
                    .addEntry(1, 60 - (level > 1 ? 10 : 0))
                    .addEntry(2, 25 + (level > 1 ? 5 : 0))
                    .addEntry(3, 5 + (level > 1 ? 5 : 0)).build();
        }
        if (randLevel > ench.getMaxLevel()) randLevel = ench.getMaxLevel();
        return BaseEnchant.generateEnchantBook(ench, randLevel);
    }
}
