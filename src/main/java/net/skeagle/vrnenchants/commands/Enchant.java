package net.skeagle.vrnenchants.commands;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.EnchantRegistry;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrnenchants.util.VRNUtil.say;

public class Enchant implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender cs, Command command, String s, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("Only players can use this command.");
            return true;
        }
        if (!cs.hasPermission("vrn.enchant")) {
            say(cs, VRNUtil.NOPERM);
            return true;
        }
        Player p = (Player) cs;
        ItemStack i = p.getInventory().getItemInMainHand();
        if (i.getType() == Material.AIR) {
            say(p, "&cYou must be holding an item to enchant.");
            return true;
        }
        if (args.length < 1) {
            say(p, "&cYou must provide an enchant name.");
            return true;
        }
        if (args[0].equalsIgnoreCase("all")) {
            int level = parseLevel(args, p);
            if (level < 1) {
                say(p, "&cLevel cannot be less than 1.");
                return true;
            }
            for (Enchantment ench : Enchantment.values())
                i.addUnsafeEnchantment(ench, level);
            for (EnchantRegistry.VRN entry : EnchantRegistry.VRN.values()) {
                BaseEnchant.applyEnchant(i, entry.getEnch(), level);
                BaseEnchant.updateLore(i);
            }
            return true;
        }
        Enchantment enchant = checkArgs(args);
        if (enchant == null) {
            say(p, "&cThat is not an available enchant.");
            return true;
        }
        int level = parseLevel(args, p);
        if (level < 1) {
            say(p, "&cLevel cannot be less than 1.");
            return true;
        }
        BaseEnchant.applyEnchant(i, enchant, level);
        BaseEnchant.updateLore(i);
        return true;
    }

    private int parseLevel(String[] args, Player p) {
        int level;
        if (args.length < 2)
            level = 1;
        else if (!findInt(args[1])) {
            say(p, "&cPlease specify a valid enchant level.");
            return 0;
        }
        else
            level = Integer.parseInt(args[1]);
        return level;
    }

    private Enchantment checkArgs(String[] args) {
        for (Enchantment enchants : Enchantment.values())
            if (args[0].equalsIgnoreCase(enchants.getKey().toString().replaceAll("minecraft:", "").replaceAll("vrnenchants:","")))
                return enchants;
        return null;
    }

    private boolean findInt(String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            ArrayList<String> names = new ArrayList<>();
            for (Enchantment enchants : Enchantment.values()) {
                names.add(enchants.getKey().toString().replaceAll("minecraft:", "").replaceAll("vrnenchants:", ""));
                names.add("all");
            }
            return names;
        }
        return new ArrayList<>();
    }
}