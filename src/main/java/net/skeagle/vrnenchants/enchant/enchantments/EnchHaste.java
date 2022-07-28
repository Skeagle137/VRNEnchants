package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@EnchDescription("Breaking a block has a small chance of giving haste.")
public class EnchHaste extends BaseEnchant {

    private static final Enchantment instance = new EnchHaste();

    private EnchHaste() {
        super("Haste", 3, Target.PICKAXES, Target.AXES, Target.HOES, Target.SHOVELS);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onBreakBlock(int level, BlockBreakEvent e) {
        if (e.getPlayer().getPotionEffect(PotionEffectType.FAST_DIGGING) != null) return;
        if (new RNG().calcChance(2, 1, level, 200))
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 + (level * 20), level - 1, false, false, true));
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
