package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.block.BlockBreakEvent;

public class EnchHarvest extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchHarvest();

    private EnchHarvest() {
        super("Harvest", 3, EnchantmentTarget.TOOL);
        setRarity(Rarity.RARE);
    }

    @Override
    protected void onBreakBlock(final int level, final BlockBreakEvent e) {
        final RNG r = new RNG();
        if (r.calcChance(5, level)) {
            if (!(e.getBlock().getBlockData() instanceof Ageable)) return;
            Ageable crop = (Ageable) e.getBlock().getBlockData();
            if (crop.getMaximumAge() != crop.getAge()) return;
            e.getBlock().getDrops().forEach(drop ->
                drop.setAmount(((int) (drop.getAmount() * (1.5 + (level / 2))))));
        }
    }

    public String setDescription() {
        return "Chance of reaping double or triple the items from the amount of a single crop.";
    }
}
