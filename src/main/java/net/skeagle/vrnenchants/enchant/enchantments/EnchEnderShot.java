package net.skeagle.vrnenchants.enchant.enchantments;

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

    private static final Enchantment instance = new EnchEnderShot();

    private EnchEnderShot() {
        super("Ender Shot", 1, EnchantmentTarget.BOW);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onHit(int level, LivingEntity shooter, ProjectileHitEvent e) {
        if (!(shooter instanceof Player) || e.getHitBlock() == null || e.getHitBlockFace() == null) return;
        Block b = e.getHitBlock().getRelative(e.getHitBlockFace());
        Block b2 = b.getLocation().clone().add(0, 1, 0).getBlock();
        Block b3 = b.getLocation().clone().add(0, -1, 0).getBlock();
        Location loc = new Location(e.getEntity().getWorld(), b.getX() + 0.5, b.getY(), b.getZ() + 0.5,
                shooter.getLocation().getYaw(), shooter.getLocation().getPitch());
        if (isSafe(b)) {
            if (!isSafe(b2)) {
                if (isSafe(b3))
                    updateLoc(loc, b3);
                else
                    ((Player) shooter).setSneaking(true);
            }
            boolean safe = false;
            Block b4;
            for (int i = b.getY() - 1; i > b.getY() - 21; i--) {
                b4 = shooter.getWorld().getBlockAt(b.getX(), i, b.getZ());
                if (b4.getType() != Material.LAVA) {
                    if (!b4.isPassable()) {
                        safe = true;
                        break;
                    }
                }
                else
                    break;
            }
            if (!safe) {
                sendWarning((Player) shooter);
                return;
            }
        }
        else {
            sendWarning((Player) shooter);
            return;
        }
        shooter.teleport(loc);
    }

    private boolean isSafe(Block b) {
        return b.isPassable() && b.getType() != Material.LAVA;
    }

    private void sendWarning(Player p) {
        sayActionBar(p, "&cPrevented teleporting since the location is unsafe to teleport to!");
    }

    private void updateLoc(Location loc, Block b) {
        loc.setX(b.getX() + 0.5);
        loc.setY(b.getY());
        loc.setZ(b.getZ() + 0.5);
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
