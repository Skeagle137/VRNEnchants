package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.mineacademy.fo.Common;

import java.util.ArrayList;

public class EnchUnderShirt extends BaseEnchant {

    private static final Enchantment instance = new EnchUnderShirt();

    private EnchUnderShirt() {
        super("UnderShirt", 1, EnchantmentTarget.ARMOR_TORSO);
        setRarity(Rarity.EPIC);
    }

    private final ArrayList<Player> cooldown = new ArrayList<>();

    @Override
    protected void onDamaged(final int level, final Player p, final EntityDamageEvent e) {
        if (p.getHealth() - e.getDamage() > 0 || cooldown.contains(p)) return;
        cooldown.add(p);
        e.setDamage(0.0);
        p.setHealth(0.5);
        Common.runLater(20 * 10, () -> cooldown.remove(p));
    }

    public String setDescription() {
        return "Receiving a fatal blow will not kill you, but instead leave you on half a heart.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
