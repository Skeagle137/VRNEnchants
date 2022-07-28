package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@EnchDescription("Increased damage to animals.")
public class EnchHunter extends BaseEnchant {

    private static final Enchantment instance = new EnchHunter();

    private EnchHunter() {
        super("Hunter", 2, Target.SWORDS, Target.AXES);
        setRarity(Rarity.UNCOMMON);
    }

    @Override
    protected void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent e) {
        e.setDamage(e.getFinalDamage() + (level * 2));
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
