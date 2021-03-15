package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class EnchUnderShirt extends BaseEnchant {

    private static final Enchantment instance = new EnchUnderShirt();

    private EnchUnderShirt() {
        super("UnderShirt", 1, EnchantmentTarget.ARMOR_TORSO);
        setRarity(Rarity.EPIC);
        setInitialCooldown(10);
    }

    @Override
    protected void onDamaged(final int level, final Player p, final EntityDamageEvent e) {
        if (p.getHealth() - e.getFinalDamage() > 0) return;
        e.setDamage(0.05);
        p.setHealth(0.5);
        setCooldown(p);
    }

    public String setDescription() {
        return "Receiving a fatal blow will not kill you, but instead leave you on half a heart.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
