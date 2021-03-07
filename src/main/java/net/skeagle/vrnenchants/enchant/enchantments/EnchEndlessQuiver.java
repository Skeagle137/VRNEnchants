package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public class EnchEndlessQuiver extends BaseEnchant {

    private static final Enchantment instance = new EnchEndlessQuiver();

    private EnchEndlessQuiver() {
        super("Endless Quiver", 5, EnchantmentTarget.BOW);
        setRarity(Rarity.RARE);
    }

    @Override
    protected void onHit(int level, LivingEntity shooter, ProjectileHitEvent e) {
        if (!(e.getHitEntity() instanceof LivingEntity) || e.getHitEntity() == null || !(shooter instanceof Player) || e.getHitEntity().isInvulnerable()) return;
        Player p = (Player) shooter;
        if (new RNG().calcChance(25, 5, level)) {
            if (e.getEntityType() == EntityType.SPECTRAL_ARROW)
                p.getInventory().addItem(new ItemStack(Material.SPECTRAL_ARROW));
            else if (e.getEntityType() == EntityType.ARROW) {
                Arrow arrow = (Arrow) e.getEntity();
                ItemStack i = new ItemStack(Material.TIPPED_ARROW);
                PotionMeta meta = ((PotionMeta) i.getItemMeta());
                if (meta.getBasePotionData().getType() == PotionType.UNCRAFTABLE) return;
                meta.setBasePotionData(arrow.getBasePotionData());
                if (arrow.hasCustomEffects())
                    for (PotionEffect pe : arrow.getCustomEffects())
                        meta.addCustomEffect(pe, true);
                i.setItemMeta(meta);
                p.getInventory().addItem(i);
            }
        }
    }

    public String setDescription() {
        return "Similar to infinity, but only applies to spectral and tipped arrows.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
