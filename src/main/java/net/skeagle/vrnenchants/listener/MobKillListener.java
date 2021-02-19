package net.skeagle.vrnenchants.listener;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.enchant.enchantments.EnchBookworm;
import net.skeagle.vrnenchants.enchant.enchantments.EnchFisherman;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MobKillListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKill(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Monster) || e.getEntity().getKiller() != null) return;
        int rand = VRNUtil.rng(1, 800);
        if (BaseEnchant.hasEnchant(e.getEntity().getKiller().getEquipment().getItemInMainHand(), Enchantment.LOOT_BONUS_MOBS)) return;
        int level = BaseEnchant.getEnchants(e.getEntity().getKiller().getEquipment().getItemInMainHand()).get(Enchantment.LOOT_BONUS_MOBS);
        if (rand > (int) (1 + (level * 0.5))) return;
        ItemStack i = randomizeEnchant(e.getEntity().getKiller());
        e.getDrops().add(i);
    }

    private ItemStack randomizeEnchant(LivingEntity e) {
        int level = 0;
        if (BaseEnchant.hasEnchant(e.getEquipment().getItemInMainHand(), EnchBookworm.getInstance()))
            level = BaseEnchant.getEnchants(e.getEquipment().getItemInMainHand()).get(EnchBookworm.getInstance());
        Rarity randRarity = new RNG.Randomizer<Rarity>()
                .addEntry(Rarity.COMMON, 800 - (level * 20))
                .addEntry(Rarity.UNCOMMON, 650 + (level * 20))
                .addEntry(Rarity.RARE, 500 + (level > 1 ? 40 : 0))
                .addEntry(Rarity.EPIC, 300 + (level > 1 ? 20 : 0))
                .addEntry(Rarity.LEGENDARY, 100 + (level > 2 ? 15 : 0))
                .addEntry(Rarity.MYTHICAL, 20 + (level > 2 ? 5 : 0)).build(); //cant get cosmic enchants from mobs
        ArrayList<VRNEnchants.VRN> sameRarity = new ArrayList<>();
        for (VRNEnchants.VRN vrn : VRNEnchants.VRN.values())
            if (((BaseEnchant) vrn.getEnch()).getRarity() == randRarity)
                sameRarity.add(vrn);
        int randEnch = VRNUtil.rng(0, sameRarity.size() - 1);
        BaseEnchant ench = (BaseEnchant) VRNEnchants.VRN.valueOf(sameRarity.get(randEnch).toString()).getEnch();
        int randLevel;
        if (ench.getRarity().getIndividualPoints() < 15) { //not legendary or higher
            randLevel = new RNG.Randomizer<Integer>()
                    .addEntry(1, 80 - (level * 3))
                    .addEntry(2, 60 + (level > 1 ? 10 : 0))
                    .addEntry(3, 25 + ((level > 2 ? 5 : 0))).build();
        }
        else {
            randLevel = new RNG.Randomizer<Integer>()
                    .addEntry(1, 100 - (level * 5))
                    .addEntry(2, 60 + (level > 1 ? 10 : 0))
                    .addEntry(3, 20 + (level > 2 ? 5 : 0)).build();
        }
        if (randLevel > ench.getMaxLevel()) randLevel = ench.getMaxLevel();
        return BaseEnchant.generateEnchantBook(ench, randLevel);
    }
}
