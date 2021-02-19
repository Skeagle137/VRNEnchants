package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.RNG;
import net.skeagle.vrnenchants.enchant.Rarity;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.mineacademy.fo.Common;

import java.util.ArrayList;
import java.util.List;

public class EnchThunderStrike extends BaseEnchant implements Listener {

    @Getter
    private static final Enchantment instance = new EnchThunderStrike();

    public EnchThunderStrike() {
        super("Thunder Strike", 3, EnchantmentTarget.WEAPON, VRNEnchants.VRN.VENOM.getEnch());
        setRarity(Rarity.MYTHICAL);
    }

    List<LightningStrike> lightning_list = new ArrayList<>();

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        if (new RNG().calcChance(8, 4, level)) {
            LightningStrike strike = damager.getWorld().strikeLightning(e.getEntity().getLocation());
            lightning_list.add(strike);
            Common.runLater(100, () -> lightning_list.remove(strike));
        }
    }

    @Override
    protected void onDamaged(int level, Player player, EntityDamageEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING && !lightning_list.isEmpty())
            e.setCancelled(true);
    }

    public String setDescription() {
        return "Chance to hit the target with lightning";
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (e.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING && !lightning_list.isEmpty())
            e.setCancelled(true);
    }
}
