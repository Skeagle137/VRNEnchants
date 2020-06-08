package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.concurrent.ThreadLocalRandom;

public class EnchAutoSmelt extends BaseEnchant {
    @Getter
    private static final Enchantment instance = new EnchAutoSmelt();

    private EnchAutoSmelt() {
        super("Auto Smelt", 1, EnchantmentTarget.TOOL);
        setRarity(100);
    }

    @Override
    protected void onBreakBlock(final int level, final BlockBreakEvent e) {
        CompMaterial mat;
        final ItemStack item = e.getPlayer().getEquipment().getItemInMainHand();
        int fortune = 0;
        if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
            fortune = calcFortune(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS));

        }
        if (e.getBlock().getType() == CompMaterial.GOLD_ORE.getMaterial()) {
            mat = CompMaterial.GOLD_INGOT;
            e.setDropItems(false);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), mat.toItem(fortune));
        }
        if (e.getBlock().getType() == CompMaterial.IRON_ORE.getMaterial()) {
            mat = CompMaterial.IRON_INGOT;
            e.setDropItems(false);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), mat.toItem(fortune));
        }
    }

    private int calcFortune(final int level) {
        final int min = 1;
        final int max = level + 1;
        if (Math.random() > (float) (100 / (max + 1))) return min;
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    public String setDescription() {
        return "Automatically smelts ores.";
    }
}
