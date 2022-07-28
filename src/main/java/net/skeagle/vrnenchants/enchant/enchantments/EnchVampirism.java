package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

@EnchDescription("Chance to steal hearts from the target.")
public class EnchVampirism extends BaseEnchant implements IConflicting {

    private static final Enchantment instance = new EnchVampirism();

    private EnchVampirism() {
        super("Vampirism", 2, Target.SWORDS);
        setRarity(Rarity.LEGENDARY);
    }

    @Override
    protected void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent e) {
        if (new RNG().calcChance(10 + (level * 5))) {
            double health = damager.getHealth() + e.getFinalDamage() * (0.75 - (level > 1 ? 0.15 : 0));
            if ((damager.getHealth() + health) < damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                damager.setHealth(health);
            else
                damager.setHealth(damager.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }
    }

    @Override
    public List<Enchantment> enchants() {
        List<Enchantment> enchs = new ArrayList<>();
        enchs.add(EnchStun.getInstance());
        return enchs;
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
