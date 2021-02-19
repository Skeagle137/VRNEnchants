package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

//postponed, probably need a dedicated death event

public class EnchGoldDigger/* extends BaseEnchant*/ {

    /*
    @Getter
    private static final Enchantment instance = new EnchGoldDigger();

    private EnchGoldDigger() {
        super("Gold Digger", 2, EnchantmentTarget.WEAPON);
        setRarity(Rarity.UNCOMMON);
    }

    @Override
    protected void onEntityKilled(int level, LivingEntity entity, EntityDamageByEntityEvent e) {
        final RNG r = new RNG();
        if (r.calcChance(15 + (level * 10))) {
            if (e.getEntity())
        }
    }

    public String setDescription() {
        return "Killing mobs has a chance to drop gold nuggets, or more ingots if the mob already drops gold.";
    }

     */
}