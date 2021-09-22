package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

@EnchDescription("Killing a skeletons, creepers, zombies, or other players has a chance of dropping their head.")
public class EnchDecapitation extends BaseEnchant {

    private static final Enchantment instance = new EnchDecapitation();

    public EnchDecapitation() {
        super("Decapitation", 3, Target.SWORDS, Target.AXES);
        setRarity(Rarity.LEGENDARY);
    }

    @Override
    protected void onKill(int level, Player killer, EntityDeathEvent e) {
        if (new RNG().calcChance(1, 1, level)) {
            ItemStack skull = switch (e.getEntity().getType()) {
                case SKELETON -> new ItemStack(Material.SKELETON_SKULL);
                case ZOMBIE, CREEPER, PLAYER -> new ItemStack(Material.valueOf(e.getEntityType() + "_HEAD"));
                default -> null;
            };
            if (skull == null) return;
            if (e.getEntity() instanceof Player) {
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                if (meta == null) return;
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(e.getEntity().getUniqueId()));
                skull.setItemMeta(meta);
            }
            e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), skull);
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
