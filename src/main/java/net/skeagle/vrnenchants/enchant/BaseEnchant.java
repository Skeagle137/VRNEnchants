package net.skeagle.vrnenchants.enchant;

import net.skeagle.vrnenchants.VRNMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.*;

import static net.skeagle.vrnenchants.enchant.VRNEnchants.registerEnchant;
import static net.skeagle.vrnenchants.util.VRNUtil.color;

public class BaseEnchant extends Enchantment {

    private static final String[] NUMERALS = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };
    private String name;
    private int maxlevel;
    private EnchantmentTarget target;
    private Enchantment[] conflicting;
    private boolean treasure = false;
    private boolean cursed = false;
    private int rarity;
    private int rarityfactor;

    public BaseEnchant(String name) {
        super(new NamespacedKey(VRNMain.getInstance(), name.toLowerCase().replaceAll(" ", "_")));
        this.name = name;
        registerEnchant(this);
    }

    public BaseEnchant(String name, int maxlevel) {
        super(new NamespacedKey(VRNMain.getInstance(), name.toLowerCase().replaceAll(" ", "_")));
        this.name = name;
        this.maxlevel = maxlevel;
        registerEnchant(this);
    }

    public BaseEnchant(String name, int maxlevel, EnchantmentTarget target) {
        super(new NamespacedKey(VRNMain.getInstance(), name.toLowerCase().replaceAll(" ", "_")));
        this.name = name;
        this.maxlevel = maxlevel;
        this.target = target;
        registerEnchant(this);
    }

    public BaseEnchant(String name, int maxlevel, EnchantmentTarget target, Enchantment... conflicting) {
        super(new NamespacedKey(VRNMain.getInstance(), name.toLowerCase().replaceAll(" ", "_")));
        this.name = name;
        this.maxlevel = maxlevel;
        this.target = target;
        this.conflicting = conflicting;
        this.conflictingEnchants(conflicting);
        registerEnchant(this);
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return (maxlevel > 0 ? maxlevel : 1);
    }

    @Override
    public final int getStartLevel() {
        return 1;
    }

    @Override
    public final EnchantmentTarget getItemTarget() {
        return (target != null ? target : EnchantmentTarget.ALL);
    }

    @Override
    public final boolean isTreasure() {
        return treasure;
    }

    @Override
    public final boolean isCursed() {
        return cursed;
    }

    public final int getRarity() {
        return rarity;
    }

    public final int setRarity(int rarity) {
        return this.rarity = rarity;
    }

    public final int getRarityFactor() {
        return rarityfactor;
    }

    public final int setRarityFactor(int rarityfactor) {
        return this.rarityfactor = rarityfactor;
    }

    @Override
    public final boolean conflictsWith(Enchantment other) {
        return conflicting != null;
    }

    @Override
    public final boolean canEnchantItem(ItemStack item) {
        return true;
    }

    private ArrayList<Enchantment> conflictingEnchants(Enchantment[] conflicting) {
        return new ArrayList<>(Arrays.asList(conflicting));
    }

    protected final boolean setTreasure() {
        return treasure = true;
    }

    protected final boolean setCursed() {
        return cursed = true;
    }

    protected void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent e) {
    }

    protected void onInteract(int level, PlayerInteractEvent e) {
    }

    protected void onBreakBlock(int level, BlockBreakEvent e) {
    }

    protected void onShoot(int level, LivingEntity shooter, ProjectileLaunchEvent e) {
    }

    protected void onHit(int level, LivingEntity shooter, ProjectileHitEvent e) {
    }

    public static boolean hasEnchant(ItemStack i, Enchantment enchant) {
        if(i.getItemMeta() != null && i.getItemMeta().getEnchants() != null && i.getItemMeta().getEnchants().size() > 0){
            for (Map.Entry<Enchantment, Integer> enchants : i.getItemMeta().getEnchants().entrySet()) {
                if (enchants.getKey().equals(enchant)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Map<BaseEnchant, Integer> getEnchants(ItemStack i) {
        final Map<BaseEnchant, Integer> map = new HashMap<>();
        if (i == null)
            return map;
        final Map<Enchantment, Integer> vanilla;
        try {
            vanilla = i.hasItemMeta() ? i.getItemMeta().getEnchants() : new HashMap<>();
        } catch (final NullPointerException ex) {
            return map;
        }
        for (final Map.Entry<Enchantment, Integer> e : vanilla.entrySet()) {
            final Enchantment ench = e.getKey();
            final int level = e.getValue();

            if (ench instanceof BaseEnchant)
                map.put((BaseEnchant) ench, level);
        }
        return map;
    }

    public static String applyEnchantName(Enchantment ench, int level) {
        if (!(ench instanceof BaseEnchant)) return null;
        BaseEnchant enchant = (BaseEnchant) ench;
        String prefix = Rarity.getPrefixFromIndividualPoints(enchant.getRarity() + (enchant.getRarityFactor() * (level - 1)));
        if (level == 1 && enchant.getMaxLevel() == 1) {
            return color(prefix + enchant.getName() + "&r");
        }
        if (level > 10 || level <= 0) {
            return color(prefix + enchant.getName() + " enchantment.level." + level + "&r");
        }
        return color(prefix + enchant.getName() + " " + NUMERALS[level - 1] + "&r");
    }

    public static void updateLore(ItemStack i) {
        final ItemMeta meta = i.getItemMeta();
        if (meta == null) {
            return;
        }
        Map<Enchantment, Integer> enchants;
        if (meta instanceof EnchantmentStorageMeta) {
            final EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta)meta;
            enchants = meta2.getStoredEnchants();
        }
        else {
            enchants = meta.getEnchants();
        }
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        else {
            lore.removeIf(line -> !line.startsWith(color("&7")));
        }
        for (final Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
            if (!(e.getKey() instanceof BaseEnchant)) {
                continue;
            }
            final BaseEnchant ench = (BaseEnchant) e.getKey();
            String enchlore;
            String prefix = Rarity.getPrefixFromIndividualPoints(ench.getRarity() + (ench.getRarityFactor() * (e.getValue() - 1)));
            if (e.getValue() == 1 && ench.getMaxLevel() == 1) {
                enchlore = color(prefix + ench.getName() + "&r");
            }
            else if (e.getValue() > 10 || e.getValue() <= 0) {
                enchlore = color(prefix + ench.getName() + " enchantment.level." + e.getValue() + "&r");
            }
            else {
                enchlore = color(prefix + ench.getName() + " " + NUMERALS[e.getValue() - 1] + "&r");
            }
            lore.add(0, enchlore);
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
    }

    public static boolean applyEnchant(final ItemStack item, final Enchantment ench, final int level) {
        removeEnchant(item, ench, level);
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        if (ench instanceof BaseEnchant) {
            final String name = applyEnchantName(ench, level);
            lore.add(0, name);
        }
        if (meta instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta)meta).addStoredEnchant(ench, level, true);
        }
        else {
            meta.addEnchant(ench, level, true);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return true;
    }

    public static ItemStack generateEnchantBook (final Enchantment ench, final int level) {
        final ItemStack i = new ItemStack(Material.ENCHANTED_BOOK);
        final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) i.getItemMeta();
        meta.addStoredEnchant(ench, level, true);
        i.setItemMeta(meta);
        updateLore(i);
        return i;
    }

    private static void removeEnchant(final ItemStack item, final Enchantment ench, final int level) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasEnchant(ench)) {
            return;
        }
        final List<String> lore = meta.getLore();
        if (ench instanceof BaseEnchant) {
            BaseEnchant e = (BaseEnchant) ench;
            if (lore != null) {
                String oldlore;
                String prefix = Rarity.getPrefixFromIndividualPoints(e.getRarity() + (e.getRarityFactor() * (level - 1)));
                if (level == 1 && ench.getMaxLevel() == 1) {
                    oldlore = color(prefix + ench.getName() + "&r");
                } else if (level > 10 || level <= 0) {
                    oldlore = color(prefix + ench.getName() + " enchantment.level." + level + "&r");
                } else {
                    oldlore = color(prefix + ench.getName() + " " + NUMERALS[level - 1] + "&r");
                }
                lore.remove(oldlore);
                meta.setLore(lore);
            }
        }
        meta.removeEnchant(ench);
        item.setItemMeta(meta);
    }


}
