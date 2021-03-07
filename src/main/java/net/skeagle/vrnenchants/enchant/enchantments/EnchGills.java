package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.VRNMain;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

import static net.skeagle.vrnenchants.util.VRNUtil.sayActionBar;

public class EnchGills extends BaseEnchant {

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
        Bukkit.getScheduler().runTaskLater(VRNMain.getInstance(), () -> {
            its_just_a_one_time_thing.remove(p);
            sayActionBar(p, "&aGills Replenished!");
        }, 20 * (60 * level));
    }

    public String setDescription() {
        return "Partially refills your air bar if you run out of air underwater. Only applies once until you come up for air.";
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
