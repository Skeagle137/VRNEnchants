package net.skeagle.vrnenchants.enchant.enchantments;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
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
            if (tri.getLocation().getY() > -45)
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

    private static class VoidlessTrident extends AbstractArrow {

        private static final EntityDataAccessor<Boolean> ID_FOIL;
        private final Trident trident;
        public int clientSideReturnTridentTickCount;

        static {
            ID_FOIL = SynchedEntityData.defineId(VoidlessTrident.class, EntityDataSerializers.BOOLEAN);
        }

        public VoidlessTrident(Trident trident) {
            super(net.minecraft.world.entity.EntityType.TRIDENT, trident.getLocation().getX(), trident.getLocation().getY(),
                    trident.getLocation().getZ(), ((CraftWorld) trident.getWorld()).getHandle());
            this.trident = trident;
            addtoWorld();
        }

        protected void defineSynchedData() {
            super.defineSynchedData();
            this.entityData.define(ID_FOIL, true);
        }

        private void addtoWorld() {
            this.level.addFreshEntity(this);
            setOwner(((CraftPlayer) trident.getShooter()).getHandle());
            setNoPhysics(true);
            trident.getWorld().playSound(trident.getLocation(), Sound.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -1.2D, -0.01D));
        }

        public void tick() {
            Entity entity = this.getOwner();
            if (this.isNoPhysics() && entity != null) {
                double d = 0.18D;
                if (!this.isAcceptibleReturnOwner()) {
                    if (!this.level.isClientSide && this.pickup == Pickup.ALLOWED)
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    this.discard();
                } else {
                    setNoPhysics(true);
                    Vec3 vec3d = entity.getEyePosition().subtract(this.position());
                    this.setPosRaw(this.getX(), this.getY() + vec3d.y * 0.015D * d, this.getZ());
                    if (this.level.isClientSide)
                        this.yOld = this.getY();

                    this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3d.normalize().scale(d)));
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

        protected net.minecraft.world.item.ItemStack getPickupItem() {
            return CraftItemStack.asNMSCopy(trident.getItem()).copy();
        }

        protected boolean tryPickup(net.minecraft.world.entity.player.Player entityhuman) {
            return super.tryPickup(entityhuman) || this.isNoPhysics() && this.ownedBy(entityhuman) && entityhuman.getInventory().add(this.getPickupItem());
        }

        public void playerTouch(net.minecraft.world.entity.player.Player entityhuman) {
            if (this.ownedBy(entityhuman) || this.getOwner() == null) {
                super.playerTouch(entityhuman);
            }
        }

        public void tickDespawn() {
            if (this.pickup != Pickup.ALLOWED) {
                super.tickDespawn();
            }
        }

        protected float getWaterInertia() {
            return 0.99F;
        }

        public boolean shouldRender(double d0, double d1, double d2) {
            return true;
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
