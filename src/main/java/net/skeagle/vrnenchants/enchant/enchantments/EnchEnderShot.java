package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;

import static net.skeagle.vrnenchants.util.VRNUtil.sayActionBar;

public class EnchEnderShot extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchEnderShot();

    private EnchEnderShot() {
        super("Ender Shot", 1, EnchantmentTarget.BOW);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onHit(int level, LivingEntity shooter, ProjectileHitEvent e) {
        Block b = e.getEntity().getLocation().clone().add(0, 1, 0).getBlock();
        if (b.isPassable() && b.getType() != Material.LAVA) {
            shooter.teleport(new Location(e.getEntity().getWorld(), e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY(), e.getEntity().getLocation().getZ(),
                    shooter.getLocation().getYaw(), shooter.getLocation().getPitch()));
        }
        else {
            if (shooter instanceof Player)
                sayActionBar((Player) shooter, "&cPrevented teleporting since the location is unsafe to teleport to!");
        }
    }
}
