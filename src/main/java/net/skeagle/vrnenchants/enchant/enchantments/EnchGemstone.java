package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.block.BlockBreakEvent;

public class EnchGemstone extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchGemstone();

    private EnchGemstone() {
        super("Gemstone", 3, EnchantmentTarget.TOOL);
        setRarity(60);
        setRarityFactor(20);
    }

    @Override
    protected void onBreakBlock(final int level, final BlockBreakEvent e) {
        final RNG ge = new RNG();
        if (ge.calcChance(5, level)) {
            e.setExpToDrop(e.getExpToDrop() * 3);
        }
    }

    public String setDescription() {
        return "Chance of getting triple the xp from mining an ore. Higher levels increase the chance.";
    }
}
