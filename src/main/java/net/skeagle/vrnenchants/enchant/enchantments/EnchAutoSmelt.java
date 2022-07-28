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
        ItemStack stack = e.getBlock().getDrops().stream().findFirst().orElse(null);
        if (stack == null || !stack.getType().toString().startsWith("RAW_") || stack.getType().isBlock()) return;
        Material ingot = Material.valueOf(stack.getType().toString().replaceAll("RAW_", "") + "_INGOT");
        int amount = stack.getAmount();
        ItemStack item = e.getPlayer().getEquipment().getItemInMainHand();

        if (ingot == Material.COPPER_INGOT && item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
            int i = VRNUtil.rng(1, (item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2) - 2);
            amount = amount * (i + 1);
        }
        e.setDropItems(false);

        int i = VRNUtil.rng(2, 5) * e.getBlock().getDrops().size();
        if (BaseEnchant.hasEnchant(item, EnchGemstone.getInstance())) {
            if (new RNG().calcChance(10, 5, level))
                i *= 3;
        }

        ExperienceOrb orb = (ExperienceOrb) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation().add(0.5, 0.5, 0.5), EntityType.EXPERIENCE_ORB);
        orb.setExperience(i);

        if (!BaseEnchant.hasEnchant(item, EnchTelepathy.getInstance())) {
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(ingot, amount));
            return;
        }
        this.dropTelepathy(e.getPlayer(), new ItemStack(ingot, amount));
    }

    private void dropTelepathy(Player p, ItemStack item) {
        if (p.getInventory().firstEmpty() != -1 || p.getInventory().first(item) != -1)
            p.getInventory().addItem(item);
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
