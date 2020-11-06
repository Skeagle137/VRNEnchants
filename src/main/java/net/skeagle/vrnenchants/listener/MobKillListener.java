package net.skeagle.vrnenchants.listener;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MobKillListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFish(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Monster)) return;
        int rand = VRNUtil.rng(1, 1000);
        if (rand != 200) return;
        ItemStack i = randomizeEnchant();
        e.getDrops().add(i);
    }

    private ItemStack randomizeEnchant() {
        Rarity randRarity = new RNG.Randomizer<Rarity>()
                .addEntry(Rarity.COMMON, 1400)
                .addEntry(Rarity.UNCOMMON, 800)
                .addEntry(Rarity.RARE, 400)
                .addEntry(Rarity.EPIC, 120)
                .addEntry(Rarity.LEGENDARY, 30)
                .addEntry(Rarity.MYTHICAL, 4).build(); //cant get cosmic enchants from mobs
        ArrayList<BaseEnchant> sameRarity = new ArrayList<>();
        for (VRNEnchants.VRN vrn : VRNEnchants.VRN.values()) {
            if (((BaseEnchant) vrn.getEnch()).getRarity() == randRarity)
                sameRarity.add((BaseEnchant) vrn.getEnch());
        }
        int randEnch = VRNUtil.rng(0, sameRarity.size());
        BaseEnchant ench = ((BaseEnchant) VRNEnchants.VRN.values()[randEnch].getEnch());
        int randLevel;
        if (ench.getRarity().getIndividualPoints() < 15) { //not legendary or higher
            randLevel = new RNG.Randomizer<Integer>()
                    .addEntry(1, 110)
                    .addEntry(2, 70)
                    .addEntry(3, 20).build();
        }
        else {
            randLevel = new RNG.Randomizer<Integer>()
                    .addEntry(1, 140)
                    .addEntry(2, 50)
                    .addEntry(3, 10).build();
        }
        return BaseEnchant.generateEnchantBook(ench, randLevel);
    }
}
