package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

@EnchDescription("Has a chance to extinguish yourself early when burning.")
public class EnchExtinguish extends BaseEnchant {

    private static final Enchantment instance = new EnchExtinguish();

    private EnchExtinguish() {
        super("Extinguish", 2, Target.CHESTPLATES);
        setRarity(Rarity.RARE);
    }

    @Override
    protected void onDamaged(int level, Player p, EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK && p.getFireTicks() > 0)
            if (new RNG().calcChance(10,5, level))
                p.setFireTicks(0);
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
