package net.skeagle.vrnenchants;

import net.skeagle.vrnenchants.commands.Enchant;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.enchant.EnchantListener;
import net.skeagle.vrnenchants.enchant.enchantments.EnchGills;
import net.skeagle.vrnenchants.enchant.enchantments.EnchSpeedy;
import net.skeagle.vrnenchants.listener.ProjectileTracker;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.mineacademy.fo.plugin.SimplePlugin;

import static net.skeagle.vrnenchants.enchant.VRNEnchants.registerEnchant;

public class VRNMain extends SimplePlugin {

    private static VRNMain plugin;

    @Override
    protected void onPluginStart() {
        plugin = this;
        //command
        registerCommand(new Enchant());
        //enchants
        VRNEnchants.registerOnStart();
        //listeners
        registerEvents(new EnchantListener());
        registerEvents(new ProjectileTracker());
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
