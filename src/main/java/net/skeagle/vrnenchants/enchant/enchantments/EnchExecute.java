package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
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
        super("Execute", 2, EnchantmentTarget.WEAPON);
        setRarity(Rarity.MYTHICAL);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        if (((LivingEntity) e.getEntity()).getHealth() < level) {
            e.setDamage(50);
            CompParticle.FLAME.spawn(e.getEntity().getLocation().add(0, 1, 0));
            CompSound.WITHER_HURT.play(e.getEntity().getLocation(), 0.75f, 0.8F);
            if (e.getEntity() instanceof Player)
                sayNoPrefix(e.getEntity(), "&c&lExecuted &7by " + damager.getName() + "'s insta-kill below &c" + level + "❤");
        }
    }

    public String setDescription() {
        return "Immediately kills the target if it has a specific amount of health left.";
    }
}
