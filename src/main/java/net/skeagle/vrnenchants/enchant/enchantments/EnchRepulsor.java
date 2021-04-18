package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.VRNEnchants;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EnchDescription("Repulses attacks by putting up your shield at the right time")
public class EnchRepulsor extends BaseEnchant {

    private static final Enchantment instance = new EnchRepulsor();

    private EnchRepulsor() {
        super("Repulsor", 3, Target.SHIELD);
        setRarity(Rarity.EPIC);
    }

    private static final List<UUID> repulseList = new ArrayList<>();

    @Override
    protected void onInteract(int level, PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (repulseList.contains(e.getPlayer().getUniqueId())) return;
        repulseList.add(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskLater(VRNEnchants.getInstance(), () -> repulseList.remove(e.getPlayer().getUniqueId()), (int) (3 + (level * 0.5)));
    }

    @Override
    protected void onDamaged(int level, Player p, EntityDamageEvent e) {
        if (!repulseList.contains(p.getUniqueId())) return;
        e.setCancelled(true);
        repulseList.remove(p.getUniqueId());
        p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().clone().add(0, 1, 0), 15, 0.2, 0.2, 0.2, 0.35);
        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 1.0F, 1.5F);
        Entity en = ((EntityDamageByEntityEvent) e).getDamager();
        if (en instanceof Projectile)
            en.getLocation().setYaw(en.getLocation().getYaw() + 180);
        else
            en.setVelocity(en.getLocation().getDirection().multiply(-0.5 - (level * 0.5)).setY(0.6));
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
