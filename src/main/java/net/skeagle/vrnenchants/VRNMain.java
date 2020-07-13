package net.skeagle.vrnenchants;

import net.skeagle.vrnenchants.commands.Enchant;
import net.skeagle.vrnenchants.commands.EnchantBook;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.enchant.EnchantListener;
import net.skeagle.vrnenchants.enchant.enchantments.EnchGills;
import net.skeagle.vrnenchants.listener.AnvilRepairListener;
import net.skeagle.vrnenchants.listener.FishingListener;
import net.skeagle.vrnenchants.listener.ProjectileTracker;
import org.mineacademy.fo.plugin.SimplePlugin;

public class VRNMain extends SimplePlugin {

    private static VRNMain plugin;

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
        //enchants extended listeners
        registerEvents(new EnchGills());
    }

    @Override
    protected void onPluginStop() {
    }

    public static VRNMain getInstance() {
        return plugin;
    }
}
