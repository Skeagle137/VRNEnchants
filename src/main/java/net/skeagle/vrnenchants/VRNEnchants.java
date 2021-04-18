package net.skeagle.vrnenchants;

import net.skeagle.vrnenchants.commands.Enchant;
import net.skeagle.vrnenchants.commands.EnchantBook;
import net.skeagle.vrnenchants.enchant.EnchantListener;
import net.skeagle.vrnenchants.enchant.EnchantRegistry;
import net.skeagle.vrnenchants.enchant.armor.DispenserListener;
import net.skeagle.vrnenchants.enchant.armor.TaskArmorEnchant;
import net.skeagle.vrnenchants.enchant.armor.armorequip.ArmorListener;
import net.skeagle.vrnenchants.enchant.enchantments.EnchGliding;
import net.skeagle.vrnenchants.enchant.enchantments.EnchTeleportResistance;
import net.skeagle.vrnenchants.listener.AnvilRepairListener;
import net.skeagle.vrnenchants.listener.FishingListener;
import net.skeagle.vrnenchants.listener.MobKillListener;
import net.skeagle.vrnenchants.listener.ProjectileTracker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

import static net.skeagle.vrnenchants.enchant.EnchantRegistry.unregisterEnchant;

public class VRNEnchants extends JavaPlugin {

    private static VRNEnchants plugin;
    private TaskArmorEnchant armor;

    @Override
    public void onEnable() {
        plugin = this;
        //command
        this.getCommand("enchant").setExecutor(new Enchant());
        this.getCommand("enchant").setTabCompleter(new Enchant());
        this.getCommand("enchantbook").setExecutor(new EnchantBook());
        this.getCommand("enchantbook").setTabCompleter(new EnchantBook());
        //enchants
        EnchantRegistry.registerOnStart();
        //listeners
        Bukkit.getPluginManager().registerEvents(new EnchantListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProjectileTracker(), this);
        Bukkit.getPluginManager().registerEvents(new AnvilRepairListener(), this);
        Bukkit.getPluginManager().registerEvents(new FishingListener(), this);
        Bukkit.getPluginManager().registerEvents(new MobKillListener(), this);
        //armor listeners
        Bukkit.getPluginManager().registerEvents(new DispenserListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArmorListener(), this);
        Bukkit.getPluginManager().registerEvents(new TaskArmorEnchant(), this);
        //enchants extended listeners
        Bukkit.getPluginManager().registerEvents(new EnchGliding(), this);
        Bukkit.getPluginManager().registerEvents(new EnchTeleportResistance(), this);
        //tasks
        armor = new TaskArmorEnchant();
        armor.runTaskTimerAsynchronously(this, 1L, 1L);
    }

    private void stopTasks(BukkitRunnable task) {
        if (task != null) {
            try {
                task.cancel();
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        stopTasks(armor);
        Arrays.stream(EnchantRegistry.VRN.values()).forEach(vrn -> unregisterEnchant(vrn.getEnch()));
    }

    public static VRNEnchants getInstance() {
        return plugin;
    }
}
