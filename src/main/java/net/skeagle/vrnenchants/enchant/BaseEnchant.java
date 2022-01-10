package net.skeagle.vrnenchants.enchant;

import net.skeagle.vrnenchants.VRNEnchants;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.skeagle.vrncommands.BukkitUtils.color;
import static net.skeagle.vrnenchants.enchant.EnchantRegistry.registerEnchant;
import static net.skeagle.vrnenchants.util.VRNUtil.*;

public class BaseEnchant extends Enchantment {

    private static final String[] NUMERALS = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };
    private final String name;
    private final int maxlevel;
    private final Target[] targets;
    private Rarity rarity;
    private List<Enchantment> conflicting;
    private final EnchantCooldown enchantCooldown = new EnchantCooldown();
    private long cooldown;
    private String cooldownMessage;
    private boolean cooldownErrorVisible = true;
    private EnchantCooldown.CooldownMessageType type = EnchantCooldown.CooldownMessageType.CHAT;

    public BaseEnchant(String name, int maxlevel, Target... targets) {
        super(new NamespacedKey(VRNEnchants.getInstance(), name.toLowerCase().replaceAll(" ", "_")));
        this.name = name;
        this.maxlevel = maxlevel;
        this.targets = targets;
        if (this instanceof IConflicting)
            conflicting = ((IConflicting) this).enchants();
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

    @Override @SuppressWarnings("deprecation")
    public final EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public final boolean isTreasure() {
        return false;
    }

    @Override
    public final boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment ench) {
        return conflicting != null && conflicting.contains(ench);
    }

    public final Rarity getRarity() {
        return rarity;
    }

    public final Rarity setRarity(Rarity rarity) {
        return this.rarity = rarity;
    }

    public final EnchantCooldown getCooldownMap() {
        return enchantCooldown;
    }

    public final long getCooldown() {
        return cooldown;
    }

    protected final void setInitialCooldown(long seconds) {
        this.cooldown = seconds;
    }

    public final void setCooldownMessage(String cooldownMessage) {
        this.cooldownMessage = cooldownMessage;
    }

    public final void setCooldownMessageType(EnchantCooldown.CooldownMessageType type) {
        this.type = type;
    }

    public final boolean isCooldownErrorVisible() {
        return cooldownErrorVisible;
    }

    public final void setCooldownErrorVisible(boolean cooldownErrorVisible) {
        this.cooldownErrorVisible = cooldownErrorVisible;
    }

    public final void setConflicting(IConflicting conflicting) {
        this.conflicting = conflicting.enchants();
    }

    public Target[] getTargets() {
        return targets;
    }

    public final void setCooldown(LivingEntity e) {
        enchantCooldown.set(e, cooldown);
        Task.syncRepeating(task -> {
            enchantCooldown.set(e, enchantCooldown.get(e) - 1);
            if (enchantCooldown.get(e) <= 0) {
                task.cancel();
                enchantCooldown.remove(e);
                if (cooldownMessage != null && !cooldownMessage.isEmpty() && e instanceof Player) {
                    if (type == EnchantCooldown.CooldownMessageType.ACTION)
                        sayActionBar((Player) e, cooldownMessage);
                    else if (type == EnchantCooldown.CooldownMessageType.CHAT)
                        sayNoPrefix(e, cooldownMessage);

                }
            }
        }, 20L, 20L);
    }

    @Override
    public final boolean canEnchantItem(ItemStack item) {
        return !itemConflicts(item, this);
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

    protected void onDamaged(int level, Player player, EntityDamageEvent e) {
    }

    protected void onKill(int level, Player killer, EntityDeathEvent e) {
    }

    public static boolean hasEnchant(ItemStack i, Enchantment enchant) {
        if(i.getItemMeta() != null && i.getItemMeta().getEnchants() != null && !i.getItemMeta().getEnchants().isEmpty())
            for (Map.Entry<Enchantment, Integer> enchants : i.getItemMeta().getEnchants().entrySet())
                if (enchants.getKey().equals(enchant)) return true;

        return false;
    }

    public static String getDescription(BaseEnchant ench) {
       if (!ench.getClass().isAnnotationPresent(EnchDescription.class))
           return null;
       return ench.getClass().getAnnotation(EnchDescription.class).value();
    }

    public static List<Enchantment> getConflicting(BaseEnchant ench) {
        if (!(ench instanceof IConflicting)) return null;
        IConflicting conflicting = (IConflicting) ench;
        if (conflicting.enchants().size() < 1) return null;
        return conflicting.enchants();
    }

    public static Map<BaseEnchant, Integer> getEnchants(ItemStack i) {
        Map<BaseEnchant, Integer> map = new HashMap<>();
        Map<Enchantment, Integer> vanilla;
        if (i == null) return map;
        ItemMeta meta = i.getItemMeta();
        if (meta == null) return map;
        if (meta instanceof EnchantmentStorageMeta)
            vanilla = ((EnchantmentStorageMeta)meta).getStoredEnchants();
        else
            vanilla = meta.getEnchants();
        for (Map.Entry<Enchantment, Integer> e : vanilla.entrySet()) {
            Enchantment ench = e.getKey();
            int level = e.getValue();
            if (ench instanceof BaseEnchant)
                map.put((BaseEnchant) ench, level);
        }
        return map;
    }

    public static String applyEnchantName(Enchantment ench, int level) {
        if (!(ench instanceof BaseEnchant)) return null;
        BaseEnchant enchant = (BaseEnchant) ench;
        String prefix = enchant.getRarity().getPrefix();
        if (level == 1 && enchant.getMaxLevel() == 1)
            return color(prefix + enchant.getName() + "&r");
        if (level > 10 || level < 1)
            return color(prefix + enchant.getName() + " enchantment.level." + level + "&r");
        return color(prefix + enchant.getName() + " " + NUMERALS[level - 1] + "&r");
    }

    public static void updateLore(ItemStack i) {
        ItemMeta meta = i.getItemMeta();
        if (meta == null)
            return;
        Map<Enchantment, Integer> enchants;
        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) meta;
            enchants = meta2.getStoredEnchants();
        }
        else
            enchants = meta.getEnchants();
        List<String> lore = new ArrayList<>();
        meta.setLore(lore);
        i.setItemMeta(meta);
        int line = 0;
        for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
            if (!(e.getKey() instanceof BaseEnchant))
                continue;
            BaseEnchant ench = (BaseEnchant) e.getKey();
            String enchlore;
            String prefix = ench.getRarity().getPrefix();
            if (e.getValue() == 1 && ench.getMaxLevel() == 1)
                enchlore = color(prefix + ench.getName() + "&r");
            else if (e.getValue() > 10 || e.getValue() <= 0)
                enchlore = color(prefix + ench.getName() + " enchantment.level." + e.getValue() + "&r");
            else
                enchlore = color(prefix + ench.getName() + " " + NUMERALS[e.getValue() - 1] + "&r");
            lore.add(line, enchlore);
            line++;
            if (i.getType() == Material.ENCHANTED_BOOK) {
                String desc = getDescription(ench);
                lore.add(line, desc != null ? color("&7&o" + desc) : color("&7&oThis enchant has no description."));
                line++;
                List<String> names = new ArrayList<>();
                Arrays.stream(ench.getTargets()).collect(Collectors.toList()).forEach(t -> names.add(t.getName()));
                lore.add(color("&eApplies to: &c" + String.join(", ", names)));
                line++;
                if (ench instanceof ICooldown) {
                    lore.add(color("&eCooldown: &c" + ((ICooldown) ench).cooldown(e.getValue())) + " seconds");
                    line++;
                }
            }
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
    }

    public static boolean isConflicting(Enchantment ench1, Enchantment ench2) {
        if (ench1.conflictsWith(ench2)) return true;
        if (!(ench1 instanceof BaseEnchant)) return false;
        BaseEnchant base = (BaseEnchant) ench1;
        if (ench2 instanceof BaseEnchant)
            if (!Target.matches(base, ((BaseEnchant) ench2).getTargets()))
                return true;
        List<Enchantment> enchants = getConflicting(base);
        if (enchants == null)
            return false;
        return enchants.stream().anyMatch(e -> e == ench2);
    }

    private static boolean itemConflicts(ItemStack item, Enchantment ench) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return false;
        if (!(ench instanceof BaseEnchant))
            return meta.getEnchants().keySet().stream().anyMatch(ench::conflictsWith);
        List<Enchantment> enchants = getConflicting((BaseEnchant) ench);
        if (enchants == null)
            return false;
        int i = 0, limit = ((IConflicting) ench).limit();
        Stream<Enchantment> s = meta.getEnchants().keySet().stream();
        if (limit <= 1 && s.anyMatch(e -> isConflicting(ench, e)))
            return true;
        else
            i += s.filter(e -> isConflicting(ench, e)).count();

        if (enchants.contains(ench)) {
            if (limit <= 1)
                return true;
            else
                i++;
        }
        return i >= limit;
    }

    public static boolean canEnchant(ItemStack item, BaseEnchant ench, int lvl) {
        if (!Target.matches(item, ench.targets)) return false;
        if (lvl < ench.getStartLevel())
            lvl = ench.getStartLevel();
        if (lvl > ench.getMaxLevel())
            lvl = ench.getMaxLevel();
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return false;
        if (itemConflicts(item, ench))
            return false;
        int lvlHas = meta.getEnchantLevel(ench);
        return lvlHas < lvl;
    }

    public static boolean applyEnchant(ItemStack item, Enchantment ench, int level) {
        removeEnchant(item, ench, level);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return false;
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        if (ench instanceof BaseEnchant) {
            String name = applyEnchantName(ench, level);
            lore.add(0, name);
        }
        if (meta instanceof EnchantmentStorageMeta)
            ((EnchantmentStorageMeta)meta).addStoredEnchant(ench, level, true);
        else
            meta.addEnchant(ench, level, true);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return true;
    }

    public static ItemStack generateEnchantBook(ItemStack item, Enchantment ench, int level) {
        ItemStack i = item;
        if (i == null)
            i = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) i.getItemMeta();
        meta.addStoredEnchant(ench, level, true);
        i.setItemMeta(meta);
        updateLore(i);
        return i;
    }

    private static void removeEnchant(ItemStack item, Enchantment ench, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasEnchant(ench)) return;
        List<String> lore = meta.getLore();
        if (ench instanceof BaseEnchant) {
            BaseEnchant e = (BaseEnchant) ench;
            if (lore != null) {
                String oldlore;
                String prefix = e.getRarity().getPrefix();
                if (level == 1 && ench.getMaxLevel() == 1)
                    oldlore = color(prefix + ench.getKey().getKey() + "&r");
                else if (level > 10 || level <= 0)
                    oldlore = color(prefix + ench.getKey().getKey() + " enchantment.level." + level + "&r");
                else
                    oldlore = color(prefix + ench.getKey().getKey() + " " + NUMERALS[level - 1] + "&r");
                lore.remove(oldlore);
                meta.setLore(lore);
            }
        }
        meta.removeEnchant(ench);
        item.setItemMeta(meta);
    }


}
