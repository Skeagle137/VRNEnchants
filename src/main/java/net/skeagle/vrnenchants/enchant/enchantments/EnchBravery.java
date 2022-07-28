package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@EnchDescription("Taking damage gives you resistance for a short time.")
public class EnchBravery extends BaseEnchant implements ICooldown {

    private static final Enchantment instance = new EnchBravery();

    private EnchBravery() {
        super("Bravery", 3, Target.LEGGINGS);
        setRarity(Rarity.RARE);
    }

    @Override
    protected void onDamaged(int level, Player p, EntityDamageEvent e) {
        if (p.getHealth() < 3 + level)
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (level + 1) * 20, Math.max(level - 2, 0), false, true, true));
    }

    public static Enchantment getInstance() {
        return instance;
    }

    @Override
    public int cooldown(int level) {
        return 20;
    }
}
