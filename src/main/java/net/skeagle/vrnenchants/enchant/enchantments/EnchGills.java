package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SimpleEnchantment;

import java.util.ArrayList;

import static net.skeagle.vrnenchants.util.VRNUtil.sayActionBar;

public class EnchGills extends BaseEnchant implements Listener {
    @Getter
    private static final Enchantment instance = new EnchGills();

    public EnchGills() {
        super("Gills", 2, EnchantmentTarget.ARMOR_FEET);
        setRarity(Rarity.EPIC);
    }

    private final ArrayList<Player> its_just_a_one_time_thing = new ArrayList<>();

    @EventHandler
    public void onDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player p = (Player) e.getEntity();
            final ItemStack item = p.getEquipment().getBoots();
            if (item != null && hasEnchant(item, this)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                    if (p.getRemainingAir() == 0) {
                        if (!its_just_a_one_time_thing.contains(p)) {
                            e.setCancelled(true);
                            p.setRemainingAir(p.getMaximumAir());
                            its_just_a_one_time_thing.add(p);
                            Common.runLater(20 * (60 * getEnchants(item).get(this)), () -> {
                                its_just_a_one_time_thing.remove(p);
                                sayActionBar(p, "&aGills Replenished!");
                            });
                        }
                    }
                }
            }
        }
    }

    public String setDescription() {
        return "Refills your air bar if you run out of air underwater. Only applies once until you come up for air.";
    }
}
