package net.skeagle.vrnenchants.enchant.enchantments;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.Vec3;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EnchDescription("Tridents will return when thrown into the void.")
public class EnchVoidless extends BaseEnchant {

    private static final Enchantment instance = new EnchVoidless();

    public EnchVoidless() {
        super("Voidless", 1, Target.TRIDENT);
        setRarity(Rarity.LEGENDARY);
    }

    private final Map<UUID, Task> tridentMap = new HashMap<>();

    @Override
    protected void onShoot(int level, LivingEntity shooter, ProjectileLaunchEvent e) {
        if (!(shooter instanceof Player) || e.getEntity().getType() != EntityType.TRIDENT) return;
        Trident tri = (Trident) e.getEntity();
        Task task = Task.syncRepeating(() -> {
            if (tri.getLocation().getY() > (tri.getWorld().getEnvironment() == World.Environment.NORMAL ? -80 : -40))
                return;
            new VoidlessTrident(tri);
            tridentMap.remove(e.getEntity().getUniqueId()).cancel();
            tri.remove();
        }, 1, 1);
        tridentMap.put(tri.getUniqueId(), task);
    }

    @Override
    protected void onHit(int level, LivingEntity shooter, ProjectileHitEvent e) {
        if (!(shooter instanceof Player) || e.getEntity().getType() != EntityType.TRIDENT) return;
        tridentMap.remove(e.getEntity().getUniqueId()).cancel();
    }

    private static class VoidlessTrident extends ThrownTrident {

        private final Trident trident;
        private final int loyalty;

        public VoidlessTrident(Trident trident) {
            super(net.minecraft.world.entity.EntityType.TRIDENT, ((CraftWorld) trident.getWorld()).getHandle());
            this.tridentItem = CraftItemStack.asNMSCopy(trident.getItem());
            this.trident = trident;
            this.loyalty = trident.getItem().getItemMeta().getEnchantLevel(Enchantment.LOYALTY);
            addtoWorld();
        }

        private void addtoWorld() {
            this.level.addFreshEntity(this);
            setPos(trident.getLocation().getX(), trident.getLocation().getY(), trident.getLocation().getZ());
            setOwner(((CraftPlayer) trident.getShooter()).getHandle());
            setNoPhysics(true);
            trident.getWorld().playSound(trident.getLocation(), Sound.ITEM_TRIDENT_HIT, 10.0F, 1.0F);
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -1.2D, -0.01D));
        }

        @Override
        public void tick() {
            Entity entity = this.getOwner();
            if (this.isNoPhysics() && entity != null) {
                if (!this.isAcceptibleReturnOwner()) {
                    if (!this.level.isClientSide && this.pickup == Pickup.ALLOWED)
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    this.discard();
                } else {
                    setNoPhysics(true);
                    Vec3 vec3d = entity.getEyePosition().subtract(this.position());
                    this.setPosRaw(this.getX(), this.getY() + vec3d.y * 0.015D * Math.max(this.loyalty, 0.5), this.getZ());
                    if (this.level.isClientSide)
                        this.yOld = this.getY();

                    this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3d.normalize().scale(Math.max(this.loyalty, 0.5))));
                    if (this.clientSideReturnTridentTickCount == 0)
                        trident.getWorld().playSound(trident.getLocation(), Sound.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                    ++this.clientSideReturnTridentTickCount;
                }
            }
            super.tick();
        }

        private boolean isAcceptibleReturnOwner() {
            Entity entity = this.getOwner();
            return entity != null && entity.isAlive() && (!(entity instanceof ServerPlayer) || !entity.isSpectator());
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
