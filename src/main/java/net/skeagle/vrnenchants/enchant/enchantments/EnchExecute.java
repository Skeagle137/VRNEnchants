package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import static net.skeagle.vrnenchants.util.VRNUtil.sayNoPrefix;

@EnchDescription("Immediately kills the target if it has a specific amount of health left.")
public class EnchExecute extends BaseEnchant {

    private static final Enchantment instance = new EnchExecute();

    private EnchExecute() {
        super("Execute", 2, Target.SWORDS);
        setRarity(Rarity.MYTHICAL);
    }

    @Override
    protected void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity en)) return;
        if (en.getHealth() < (level + 2)) {
            en.damage(en.getHealth(), damager);
        }
    }

    @Override
    protected void onKill(int level, Player killer, EntityDeathEvent e) {
        e.getEntity().getWorld().spawnParticle(Particle.FLAME, e.getEntity().getLocation().clone().add(0, 1, 0), 10, 0.3, 0.3, 0.3, 0.1);
        e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.6f, 0.8f);
        if (e.getEntity() instanceof Player)
            sayNoPrefix(e.getEntity(), "&c&lExecuted &7by " + killer.getName() + "'s insta-kill below &c" + ((level + 2) / 2) + "❤");
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
