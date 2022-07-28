package net.skeagle.vrnenchants.enchant;

import net.md_5.bungee.api.ChatColor;
import net.skeagle.vrncommands.misc.FormatUtils;
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

import java.util.*;

import static net.skeagle.vrncommands.BukkitUtils.color;
import static net.skeagle.vrnenchants.enchant.EnchantRegistry.registerEnchant;
import static net.skeagle.vrnenchants.util.VRNUtil.*;

public class BaseEnchant extends Enchantment {

    private static final String[] NUMERALS = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };
    private final String name;
    private final int maxlevel;
    private final Target[] targets;
    private Rarity rarity;
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
    public boolean conflictsWith(Enchantment enchant) {
        if (this instanceof IConflicting conflicting && !conflicting.enchants().isEmpty()) {
            return conflicting.enchants().stream().anyMatch(en -> en == enchant);
        }
        return false;
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
        return !this.itemConflicts(item);
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
        if(i.getItemMeta() != null && !i.getItemMeta().getEnchants().isEmpty())
            for (Map.Entry<Enchantment, Integer> enchants : i.getItemMeta().getEnchants().entrySet())
                if (enchants.getKey().equals(enchant)) return true;

        return false;
    }

    private String getDescription() {
       EnchDescription desc = this.getClass().getAnnotation(EnchDescription.class);
       return desc == null ? null : desc.value();
    }

    private boolean itemConflicts(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return false;
        Set<Enchantment> itemEnchants = (meta instanceof EnchantmentStorageMeta m ? m.getStoredEnchants() : meta.getEnchants()).keySet();
        return itemEnchants.stream().anyMatch(this::conflictsWith);
    }

    private String getFormattedEntry(int level) {
        ChatColor color = this.getRarity().getColor();
        if (level == 1 && this.getMaxLevel() == 1)
            return color(color + this.getName() + "&r");
        if (level > 10 || level < 1)
            return color(color + this.getName() + " enchantment.level." + level + "&r");
        return color(color + this.getName() + " " + NUMERALS[level - 1] + "&r");
    }

    public static Map<BaseEnchant, Integer> getEnchants(ItemStack i) {
        Map<BaseEnchant, Integer> map = new HashMap<>();
        Map<Enchantment, Integer> vanilla;
        if (i == null) return map;
        ItemMeta meta = i.getItemMeta();
        if (meta == null) return map;
        if (meta instanceof EnchantmentStorageMeta m)
            vanilla = m.getStoredEnchants();
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

    public static void updateLore(ItemStack i) {
        ItemMeta meta = i.getItemMeta();
        if (meta == null)
            return;
        Map<Enchantment, Integer> enchants;
        if (meta instanceof EnchantmentStorageMeta m) {
            enchants = m.getStoredEnchants();
        }
        else
            enchants = meta.getEnchants();
        List<String> lore = new ArrayList<>();
        meta.setLore(lore);
        i.setItemMeta(meta);
        int line = 0;
        for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
            if (!(e.getKey() instanceof BaseEnchant ench))
                continue;
            String entry = ench.getFormattedEntry(e.getValue());
            lore.add(line, entry);
            line++;
            if (i.getType() == Material.ENCHANTED_BOOK) {
                int wrap = 1;
                String desc = ench.getDescription();
                if (desc != null) {
                    List<String> descLines = FormatUtils.lineWrap(desc, 60);
                    descLines.forEach(l -> lore.add(color("&7&o" + l)));
                    wrap = descLines.size();
                }
                else {
                    lore.add(color("&7&oThis enchant has no description."));
                }
                line += wrap;
                List<String> names = new ArrayList<>();
                Arrays.stream(ench.getTargets()).toList().forEach(t -> names.add(t.getName()));
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

    public static boolean applyEnchant(ItemStack item, Enchantment ench, int level) {
        removeEnchant(item, ench, level);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return false;
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        if (ench instanceof BaseEnchant en) {
            String name = en.getFormattedEntry(level);
            lore.add(0, name);
        }
        if (meta instanceof EnchantmentStorageMeta m)
            m.addStoredEnchant(ench, level, true);
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
        if (ench instanceof BaseEnchant en) {
            if (lore != null) {
                lore.remove(en.getFormattedEntry(level));
                meta.setLore(lore);
            }
        }
        meta.removeEnchant(ench);
        item.setItemMeta(meta);
    }


}
