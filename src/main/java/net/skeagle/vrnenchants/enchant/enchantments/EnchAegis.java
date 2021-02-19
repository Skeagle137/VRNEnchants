package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static net.skeagle.vrnenchants.util.VRNUtil.sayNoPrefix;

public class EnchAegis extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchAegis();

    private EnchAegis() {
        super("Aegis", 2, EnchantmentTarget.ARMOR_LEGS);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onDamaged(int level, Player p, EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (new RNG().calcChance(12,8, level)) {
            e.setCancelled(true);
            if (!(((EntityDamageByEntityEvent) e).getDamager() instanceof Player)) return;
            Player a = (Player) ((EntityDamageByEntityEvent) e).getDamager();
            a.playSound(a.getLocation(), Sound.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 1.0f, 0.85f);
            sayNoPrefix(a, "&cYour attack was blocked by " + p.getName() + "'s Aegis!");
        }
    }

    public String setDescription() {
        return "Has a chance to deflect melee attacks.";
    }

}
