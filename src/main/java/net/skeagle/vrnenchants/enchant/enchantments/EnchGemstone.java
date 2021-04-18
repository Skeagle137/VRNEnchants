package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;

@EnchDescription("Chance of getting triple the xp from mining an ore. Higher levels increase the chance.")
public class EnchGemstone extends BaseEnchant {

    private static final Enchantment instance = new EnchGemstone();

    private EnchGemstone() {
        super("Gemstone", 4, Target.PICKAXES);
        setRarity(Rarity.UNCOMMON);
    }

    @Override
    protected void onBreakBlock(int level, BlockBreakEvent e) {
        if (new RNG().calcChance(10, 5, level))
            e.setExpToDrop(e.getExpToDrop() * 3);
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
