package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

@EnchDescription("If you die, there is a chance for the item to stay in your inventory.")
public class EnchSoulBound extends BaseEnchant {

    private static final Enchantment instance = new EnchSoulBound();

    private EnchSoulBound() {
        super("Soulbound", 3, Target.ANY);
        setRarity(Rarity.MYTHICAL);
    }

    @Override
    protected void onDeath(int level, Player p, ItemStack item, PlayerDeathEvent e) {
        if (new RNG().calcChance(100)) {
            for (ItemStack stack : p.getInventory()) {
                if (!hasEnchant(item, this)) {
                    p.getWorld().dropItem(p.getLocation(), stack);
                    p.getInventory().remove(stack);
                }
            }
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
