package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@EnchDescription("Chance to hit the target with lightning, also causing area of effect damage.")
public class EnchThunderStrike extends BaseEnchant implements Listener {

    private static final Enchantment instance = new EnchThunderStrike();

    public EnchThunderStrike() {
        super("Thunder Strike", 2, Target.SWORDS);
        setRarity(Rarity.MYTHICAL);
    }

    @Override
    protected void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(6, 2, level)) {
            LightningStrike strike = damager.getWorld().strikeLightningEffect(e.getEntity().getLocation());
            for (Entity en : strike.getNearbyEntities(1 + (level), 1 + (level), 1 + (level))) {
                if (!(en instanceof LivingEntity) || en == damager) continue;
                ((LivingEntity) en).damage(2 + level, damager);
            }
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
