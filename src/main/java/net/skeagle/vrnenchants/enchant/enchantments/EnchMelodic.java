package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

@EnchDescription("Allows you to have a chance of getting a disc from any hostile mob.")
public class EnchMelodic extends BaseEnchant {

    private static final Enchantment instance = new EnchMelodic();

    private EnchMelodic() {
        super("Melodic", 3, Target.SWORDS, Target.AXES);
        setRarity(Rarity.UNCOMMON);
    }

    @Override
    protected void onKill(int level, Player killer, EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Monster)) return;
        if (new RNG().calcChance(5, 15, level, 1000)) {
            int disc = VRNUtil.rng(1, 13);
            Material m;
            switch (disc) {
                case 1:
                    m = Material.MUSIC_DISC_CAT;
                    break;
                case 2:
                    m = Material.MUSIC_DISC_BLOCKS;
                    break;
                case 3:
                    m = Material.MUSIC_DISC_CHIRP;
                    break;
                case 4:
                    m = Material.MUSIC_DISC_FAR;
                    break;
                case 5:
                    m = Material.MUSIC_DISC_MALL;
                    break;
                case 6:
                    m = Material.MUSIC_DISC_MELLOHI;
                    break;
                case 7:
                    m = Material.MUSIC_DISC_STAL;
                    break;
                case 8:
                    m = Material.MUSIC_DISC_STRAD;
                    break;
                case 9:
                    m = Material.MUSIC_DISC_WARD;
                    break;
                case 10:
                    m = Material.MUSIC_DISC_11;
                    break;
                case 11:
                    m = Material.MUSIC_DISC_WAIT;
                    break;
                case 12:
                    m = Material.MUSIC_DISC_PIGSTEP;
                    break;
                default:
                    m = Material.MUSIC_DISC_13;
                    break;
            }
            killer.getWorld().dropItem(e.getEntity().getLocation(), new ItemStack(m));
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
