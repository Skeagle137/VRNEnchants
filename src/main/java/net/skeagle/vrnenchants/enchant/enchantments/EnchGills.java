package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;

import java.util.ArrayList;

import static net.skeagle.vrnenchants.util.VRNUtil.sayActionBar;

public class EnchGills extends BaseEnchant {
    @Getter
    private static final Enchantment instance = new EnchGills();

    public EnchGills() {
        super("Gills", 2, EnchantmentTarget.ARMOR_FEET);
        setRarity(Rarity.EPIC);
    }

    private final ArrayList<Player> its_just_a_one_time_thing = new ArrayList<>();

    @Override
    protected void onDamaged(final int level, final Player p, final EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.DROWNING || p.getRemainingAir() != 0 || its_just_a_one_time_thing.contains(p))
            return;
        e.setCancelled(true);
        p.setRemainingAir(p.getMaximumAir() / (4 - level));
        its_just_a_one_time_thing.add(p);
        Common.runLater(20 * (60 * level), () -> {
            its_just_a_one_time_thing.remove(p);
            sayActionBar(p, "&aGills Replenished!");
        });
    }

    public String setDescription() {
        return "Partially refills your air bar if you run out of air underwater. Only applies once until you come up for air.";
    }
}
