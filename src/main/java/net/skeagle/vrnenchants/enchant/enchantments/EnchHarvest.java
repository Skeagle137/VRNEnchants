package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;

@EnchDescription("Chance of reaping double or triple the items from the amount of a single crop.")
public class EnchHarvest extends BaseEnchant {

    private static final Enchantment instance = new EnchHarvest();

    private EnchHarvest() {
        super("Harvest", 3, Target.HOES);
        setRarity(Rarity.RARE);
    }

    @Override
    protected void onBreakBlock(int level, BlockBreakEvent e) {
        if (new RNG().calcChance(30, level)) {
            if (!(e.getBlock().getBlockData() instanceof Ageable crop)) return;
            if (crop.getMaximumAge() != crop.getAge()) return;
            e.getBlock().getDrops().forEach(drop ->
                drop.setAmount(((int) (drop.getAmount() * (1.5 + (level / 2))))));
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
