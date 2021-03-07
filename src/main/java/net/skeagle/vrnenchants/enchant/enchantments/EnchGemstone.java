package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.block.BlockBreakEvent;

public class EnchGemstone extends BaseEnchant {

    private static final Enchantment instance = new EnchGemstone();

    private EnchGemstone() {
        super("Gemstone", 4, EnchantmentTarget.TOOL);
        setRarity(Rarity.UNCOMMON);
    }

    @Override
    protected void onBreakBlock(final int level, final BlockBreakEvent e) {
        if (new RNG().calcChance(10, 5, level))
            e.setExpToDrop(e.getExpToDrop() * 3);
    }

    public String setDescription() {
        return "Chance of getting triple the xp from mining an ore. Higher levels increase the chance.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
