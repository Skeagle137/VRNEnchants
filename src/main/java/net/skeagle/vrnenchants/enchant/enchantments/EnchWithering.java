package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

@EnchDescription("Chance to inflict the target with wither.")
public class EnchWithering extends BaseEnchant implements IConflicting {

    private static final Enchantment instance = new EnchWithering();

    private EnchWithering() {
    super("Withering", 3, Target.SWORDS);
        setRarity(Rarity.COMMON);
    }

    @Override
    protected void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent e) {
        if (new RNG().calcChance(40 + (level * 15)))
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, level * 25, Math.max(level - 2, 0), false, false, true));
    }

    public static Enchantment getInstance() {
        return instance;
    }

    @Override
    public List<Enchantment> enchants() {
        List<Enchantment> enchs = new ArrayList<>();
        enchs.add(EnchVenom.getInstance());
        enchs.add(EnchBlinding.getInstance());
        return enchs;
    }
}
