package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
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
        if (e.getBlock().getType() == Material.NETHER_GOLD_ORE || (!e.getBlock().getType().name().endsWith("GOLD_ORE") &&
                !e.getBlock().getType().name().endsWith("IRON_ORE")) && !e.getBlock().getType().name().endsWith("COPPER_ORE")) {
            return;
        }
        Material mat = Material.valueOf(e.getBlock().getType().name().replaceAll("DEEPSLATE_", "").replaceAll("ORE", "INGOT"));
        ItemStack item = e.getPlayer().getEquipment().getItemInMainHand();
        int fortune = 1;
        if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
            fortune = calcFortune(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS));
        }
        e.setDropItems(false);

        int i = VRNUtil.rng(2, 5);
        if (BaseEnchant.hasEnchant(item, EnchGemstone.getInstance())) {
            if (new RNG().calcChance(10, 5, level))
                i *= 3;
        }

        ExperienceOrb orb = (ExperienceOrb) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation().add(0.5, 0.5, 0.5), EntityType.EXPERIENCE_ORB);
        orb.setExperience(i);

        if (!BaseEnchant.hasEnchant(item, EnchTelepathy.getInstance())) {
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(mat, fortune));
            return;
        }
        dropTelepathy(e.getPlayer(), new ItemStack(mat, fortune));
    }

    private int calcFortune(int level) {
        final int min = 1;
        int max = level + 1;
        if (VRNUtil.rng(min, max) > (float) (100 / (max + 1))) return min;
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    private void dropTelepathy(Player p, ItemStack item) {
        if (p.getInventory().firstEmpty() != -1 || p.getInventory().first(item) != -1)
            p.getInventory().addItem(item);
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
