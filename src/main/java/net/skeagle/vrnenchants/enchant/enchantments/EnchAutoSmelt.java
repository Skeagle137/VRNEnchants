package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

@EnchDescription("Automatically smelts ores when you mine them.")
public class EnchAutoSmelt extends BaseEnchant {

    private static final Enchantment instance = new EnchAutoSmelt();

    private EnchAutoSmelt() {
        super("Auto Smelt", 1, Target.PICKAXES);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onBreakBlock(int level, BlockBreakEvent e) {
        Material mat;
        ItemStack item = e.getPlayer().getEquipment().getItemInMainHand();
        int fortune = 1;
        if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS))
            fortune = calcFortune(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS));

        if (e.getBlock().getType() == Material.GOLD_ORE) {
            mat = Material.GOLD_INGOT;
            e.setDropItems(false);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(mat, fortune));
        }
        if (e.getBlock().getType() == Material.IRON_ORE) {
            mat = Material.IRON_INGOT;
            e.setDropItems(false);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(mat, fortune));
        }
    }

    private int calcFortune(int level) {
        final int min = 1;
        int max = level + 1;
        if (VRNUtil.rng(min, max) > (float) (100 / (max + 1))) return min;
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
