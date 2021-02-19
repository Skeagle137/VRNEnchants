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
        registerEvents(new EnchantListener());
        registerEvents(new ProjectileTracker());
        registerEvents(new AnvilRepairListener());
        registerEvents(new FishingListener());
        registerEvents(new MobKillListener());
        //registerEvents(new EnchantmentTableListener());
        //armor listeners
        registerEvents(new DispenserListener());
        registerEvents(new ArmorListener());
        registerEvents(new TaskArmorEnchant());
        //enchants extended listeners
        registerEvents(new EnchGliding());
        registerEvents(new EnchTeleportResistance());
        registerEvents(new EnchThunderStrike());
        //tasks
        armor = new TaskArmorEnchant();
        armor.runTaskTimerAsynchronously(this, 0, 5L);
    }

    @Override
    protected void onPluginReload() {
        cleanTasks();
    }

    private void cleanTasks() {
        stopTasks(armor);
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

    }

    public static VRNMain getInstance() {
        return plugin;
    }
}
