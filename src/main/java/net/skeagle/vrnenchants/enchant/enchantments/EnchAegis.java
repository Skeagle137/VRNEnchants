package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static net.skeagle.vrnenchants.util.VRNUtil.sayNoPrefix;

@EnchDescription("Has a chance to deflect melee attacks.")
public class EnchAegis extends BaseEnchant {

    private static final Enchantment instance = new EnchAegis();

    private EnchAegis() {
        super("Aegis", 2, Target.LEGGINGS);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onDamaged(int level, Player player, EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (new RNG().calcChance(0, 1, level)) {
            e.setCancelled(true);
            if (!(((EntityDamageByEntityEvent) e).getDamager() instanceof Player target)) return;
            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 1.0f, 0.85f);
            sayNoPrefix(target, "&cYour attack was blocked by " + player.getName() + "'s Aegis!");
            sayNoPrefix(player, "&b" + target.getName() + "'s attack was blocked by Aegis!");
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }

}
