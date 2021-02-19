package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class EnchExtinguish extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchExtinguish();

    private EnchExtinguish() {
        super("Extinguish", 2, EnchantmentTarget.ARMOR_TORSO);
        setRarity(Rarity.RARE);
    }

    @Override
    protected void onDamaged(int level, Player p, EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK && p.getFireTicks() > 0)
            if (new RNG().calcChance(10,5, level))
                p.setFireTicks(0);
    }

    public String setDescription() {
        return "Has a chance to extinguish yourself early when burning.";
    }
}
