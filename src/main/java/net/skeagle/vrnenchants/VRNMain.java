package net.skeagle.vrnenchants;

import net.skeagle.vrnenchants.commands.Enchant;
import net.skeagle.vrnenchants.commands.EnchantBook;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.enchant.EnchantListener;
import net.skeagle.vrnenchants.enchant.enchantments.EnchGliding;
import net.skeagle.vrnenchants.enchant.enchantments.EnchTeleportResistance;
import net.skeagle.vrnenchants.enchant.enchantments.EnchThunderStrike;
import net.skeagle.vrnenchants.enchant.extended.DispenserListener;
import net.skeagle.vrnenchants.enchant.extended.armorequip.ArmorListener;
import net.skeagle.vrnenchants.listener.*;
import net.skeagle.vrnenchants.enchant.extended.TaskArmorEnchant;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.plugin.SimplePlugin;

public class VRNMain extends SimplePlugin {

    private static VRNMain plugin;
    private TaskArmorEnchant armor;

    @Override
    protected void onPluginStart() {
        plugin = this;
        //command
        registerCommand(new Enchant());
        registerCommand(new EnchantBook());
        //enchants
        VRNEnchants.registerOnStart();
        //listeners
        Bukkit.getPluginManager().registerEvents(new EnchantListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProjectileTracker(), this);
        Bukkit.getPluginManager().registerEvents(new AnvilRepairListener(), this);
        Bukkit.getPluginManager().registerEvents(new FishingListener(), this);
        Bukkit.getPluginManager().registerEvents(new MobKillListener(), this);
        //Bukkit.getPluginManager().registerEvents(new EnchantmentTableListener(), this);
        //armor listeners
        Bukkit.getPluginManager().registerEvents(new DispenserListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArmorListener(), this);
        Bukkit.getPluginManager().registerEvents(new TaskArmorEnchant(), this);
        //enchants extended listeners
        Bukkit.getPluginManager().registerEvents(new EnchGliding(), this);
        Bukkit.getPluginManager().registerEvents(new EnchTeleportResistance(), this);
        Bukkit.getPluginManager().registerEvents(new EnchThunderStrike(), this);
        //tasks
        armor = new TaskArmorEnchant();
        armor.runTaskTimerAsynchronously(this, 0, 5L);
    }

    private void stopTasks(final BukkitRunnable task) {
        if (task != null) {
            try {
                task.cancel();
            } catch (final IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onPluginStop() {
        stopTasks(armor);
    }

    public static VRNMain getInstance() {
        return plugin;
    }
}
