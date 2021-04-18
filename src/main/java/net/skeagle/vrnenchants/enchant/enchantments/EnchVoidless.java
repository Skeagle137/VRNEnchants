package net.skeagle.vrnenchants.enchant.enchantments;

import net.minecraft.server.v1_16_R3.EntityThrownTrident;
import net.skeagle.vrnenchants.VRNEnchants;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchDescription;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.Target;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftTrident;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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

    private static final Map<UUID, Integer> tridentMap = new HashMap<>();

    @Override
    protected void onShoot(int level, LivingEntity shooter, ProjectileLaunchEvent e) {
        if (!(shooter instanceof Player) || e.getEntity().getType() != EntityType.TRIDENT) return;
        if (tridentMap.containsKey(shooter.getUniqueId())) return;
        Trident tri = (Trident) e.getEntity();
        EntityThrownTrident NMSTrident = ((CraftTrident) tri).getHandle();
        int task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!e.getEntity().isDead()) return;
                e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                this.cancel();
                Player p = (Player) shooter;
                ItemStack i = CraftItemStack.asCraftMirror(NMSTrident.trident);
                if (p.getInventory().firstEmpty() != -1)
                    p.getInventory().addItem(i);
                else
                    p.getWorld().dropItem(p.getLocation(), i);
                tridentMap.remove(shooter.getUniqueId());
            }
        }.runTaskTimer(VRNEnchants.getInstance(), 0, 1).getTaskId();
        tridentMap.put(shooter.getUniqueId(), task);
    }

    @Override
    protected void onHit(int level, LivingEntity shooter, ProjectileHitEvent e) {
        Bukkit.getScheduler().cancelTask(tridentMap.get(shooter.getUniqueId()));
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
