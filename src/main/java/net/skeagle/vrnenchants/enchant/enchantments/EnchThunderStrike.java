package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.EnchantRegistry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EnchThunderStrike extends BaseEnchant implements Listener {

    private static final Enchantment instance = new EnchThunderStrike();

    public EnchThunderStrike() {
        super("Thunder Strike", 2, EnchantmentTarget.WEAPON, EnchantRegistry.VRN.VENOM.getEnch());
        setRarity(Rarity.MYTHICAL);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(6, 2, level)) {
            LightningStrike strike = damager.getWorld().strikeLightningEffect(e.getEntity().getLocation());
            for (Entity en : strike.getNearbyEntities(1 + (level), 1 + (level), 1 + (level))) {
                if (!(en instanceof LivingEntity) || en == damager) continue;
                ((LivingEntity) en).damage(2 + level, damager);
            }
        }
    }

    public String setDescription() {
        return "Chance to hit the target with lightning, also causing area of effect damage.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
