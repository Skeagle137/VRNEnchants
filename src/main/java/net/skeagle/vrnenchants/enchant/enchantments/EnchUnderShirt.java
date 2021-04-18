package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

@EnchDescription("Receiving a fatal blow from a direct hit will not kill you, but instead leave you on half a heart.")
public class EnchUnderShirt extends BaseEnchant implements ICooldown {

    private static final Enchantment instance = new EnchUnderShirt();

    private EnchUnderShirt() {
        super("UnderShirt", 1, Target.CHESTPLATES);
        setRarity(Rarity.EPIC);
    }

    @Override
    protected void onDamaged(int level, Player p, EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
                e.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK &&
                e.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return;
        if (p.getHealth() - e.getFinalDamage() > 0) return;
        e.setDamage(0.05);
        p.setHealth(0.5);
        setCooldown(p);
    }

    public static Enchantment getInstance() {
        return instance;
    }

    @Override
    public int cooldown(int level) {
        return 15;
    }
}
