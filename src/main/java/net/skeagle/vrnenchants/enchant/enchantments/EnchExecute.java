package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.CompSound;

import static net.skeagle.vrnenchants.util.VRNUtil.sayNoPrefix;

public class EnchExecute extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchExecute();

    private EnchExecute() {
        super("Execute", 5);
        setRarity(60);
        setRarityFactor(25);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        final RNG rng = new RNG();
        if (rng.calcChance(8, level)) {
            if (((LivingEntity) e.getEntity()).getHealth() < 1 + level) {
                ((LivingEntity) e.getEntity()).setHealth(0);
                CompParticle.FLAME.spawn(e.getEntity().getLocation().add(0, 1, 0));
                CompSound.WITHER_HURT.play(e.getEntity().getLocation(), 1, 0.8F);
                if (e.getEntity() instanceof Player) {
                    sayNoPrefix(e.getEntity(), "&c&lEXECUTED &7by " + damager.getName() + "'s insta-kill below &c" + (1 + level) + "❤");
                }
            }
        }
    }

    public String setDescription() {
        return "Chance to immediately kill the target if it has a specific amount of health left.";
    }
}
