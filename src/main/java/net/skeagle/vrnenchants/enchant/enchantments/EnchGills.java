package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.ICooldown;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class EnchGills extends BaseEnchant implements ICooldown {

    private static final Enchantment instance = new EnchGills();

    public EnchGills() {
        super("Gills", 2, EnchantmentTarget.ARMOR_FEET);
        setRarity(Rarity.EPIC);
        setCooldownMessage("&aGills Replenished!");
        setCooldownErrorVisible(false);
    }

    @Override
    protected void onDamaged(final int level, final Player p, final EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.DROWNING || p.getRemainingAir() != 0)
            return;
        e.setCancelled(true);
        p.setRemainingAir(p.getMaximumAir() / (4 - level));
        setCooldown(p);
    }

    public String setDescription() {
        return "Partially refills your air bar if you run out of air underwater. Only applies once until you come up for air.";
    }

    public static Enchantment getInstance() {
        return instance;
    }

    @Override
    public int cooldown(int level) {
        return (60 * level);
    }
}
