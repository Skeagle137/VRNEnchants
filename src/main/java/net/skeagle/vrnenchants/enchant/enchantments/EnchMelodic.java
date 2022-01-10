package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

@EnchDescription("Allows you to have a chance of getting a disc from killing creepers directly.")
public class EnchMelodic extends BaseEnchant {

    private static final Enchantment instance = new EnchMelodic();

    private EnchMelodic() {
        super("Melodic", 3, Target.SWORDS, Target.AXES);
        setRarity(Rarity.UNCOMMON);
    }

    @Override
    protected void onKill(int level, Player killer, EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Creeper)) return;
        if (new RNG().calcChance(0, 1, level, 1200)) {
            int disc = VRNUtil.rng(1, 13);
            Material m = switch (disc) {
                case 1 -> Material.MUSIC_DISC_CAT;
                case 2 -> Material.MUSIC_DISC_BLOCKS;
                case 3 -> Material.MUSIC_DISC_CHIRP;
                case 4 -> Material.MUSIC_DISC_FAR;
                case 5 -> Material.MUSIC_DISC_MALL;
                case 6 -> Material.MUSIC_DISC_MELLOHI;
                case 7 -> Material.MUSIC_DISC_STAL;
                case 8 -> Material.MUSIC_DISC_STRAD;
                case 9 -> Material.MUSIC_DISC_WARD;
                case 10 -> Material.MUSIC_DISC_11;
                case 11 -> Material.MUSIC_DISC_WAIT;
                case 12 -> Material.MUSIC_DISC_PIGSTEP;
                default -> Material.MUSIC_DISC_13;
            };
            killer.getWorld().dropItem(e.getEntity().getLocation(), new ItemStack(m));
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
