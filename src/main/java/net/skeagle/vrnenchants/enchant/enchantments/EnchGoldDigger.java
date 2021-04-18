package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

@EnchDescription("Killing mobs has a chance to drop gold nuggets, or more ingots if the mob already drops gold.")
public class EnchGoldDigger extends BaseEnchant {

    private static final Enchantment instance = new EnchGoldDigger();

    private EnchGoldDigger() {
        super("Gold Digger", 2, Target.PICKAXES);
        setRarity(Rarity.UNCOMMON);
    }

    @Override
    protected void onKill(int level, Player killer, EntityDeathEvent e) {
        if (new RNG().calcChance(15 + (level * 10))) {

        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}