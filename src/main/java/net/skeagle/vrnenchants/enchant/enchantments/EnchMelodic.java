package net.skeagle.vrnenchants.enchant.enchantments;

import net.skeagle.vrnenchants.enchant.*;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@EnchDescription("Allows you to have a chance of getting a disc from killing creepers directly.")
public class EnchMelodic extends BaseEnchant {

    private static final Enchantment instance = new EnchMelodic();
    private final List<Material> discs;

    private EnchMelodic() {
        super("Melodic", 3, Target.SWORDS, Target.AXES);
        setRarity(Rarity.UNCOMMON);
        this.discs = Arrays.stream(Material.values()).filter(mat -> mat.toString().startsWith("MUSIC_DISC")).toList();
    }

    @Override
    protected void onKill(int level, Player killer, EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Creeper)) return;
        if (new RNG().calcChance(0, 1, level, 1200)) {
            int disc = VRNUtil.rng(0, discs.size() - 1);
            killer.getWorld().dropItem(e.getEntity().getLocation(), new ItemStack(discs.get(disc)));
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }
}
