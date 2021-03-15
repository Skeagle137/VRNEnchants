package net.skeagle.vrnenchants.commands;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
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

public class EnchantBook implements CommandExecutor, TabCompleter {

    /*public EnchantBook() {
        super("enchantbook|enchbook");
        setDescription("Generates or adds custom enchants to a book.");
        setPermission("vrn.enchant");
        setPermissionMessage(VRNUtil.NOPERM);
    }

     */

    /*@Override
    protected void onCommand() {
        ItemStack i = getPlayer().getInventory().getItemInMainHand();
        if (i.getType() != Material.BOOK && i.getType() != Material.ENCHANTED_BOOK && i.getType() != Material.AIR) {
            say(getPlayer(), "&cYou must have nothing or a book in your hand.");
            return;
        }
        if (args.length < 1) {
            say(getPlayer(), "&cYou must provide an enchant name.");
            return;
        }
        if (args[0].equalsIgnoreCase("all")) {
            for (final Enchantment ench : Enchantment.values())
                i.addUnsafeEnchantment(ench, (args.length < 2 ? 1 : findNumber(1, "&cPlease specify a valid enchant level.")));
            for (final VRNEnchants.VRN entry : VRNEnchants.VRN.values()) {
                BaseEnchant.applyEnchant(i, entry.getEnch(), (args.length < 2 ? 1 : findNumber(1, "&cPlease specify a valid enchant level.")));
                BaseEnchant.updateLore(i);
            }
            return;
        }
        final Enchantment enchant = checkArgs();
        if (enchant == null) {
            say(getPlayer(), "That is not an available enchant.");
            return;
        }
        int level = (args.length < 2 ? 1 : findNumber(1, "&cPlease specify a valid enchant level."));
        ItemStack newbook = BaseEnchant.generateEnchantBook(enchant, level);
        getPlayer().getInventory().setItemInMainHand(newbook);
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1) {
            final ArrayList<String> names = new ArrayList<>();
            for (final Enchantment enchants : Enchantment.values()) {
                names.add(enchants.getKey().toString().replaceAll("minecraft:", "").replaceAll("vrnenchants:", ""));
                names.add("all");
            }
            return completeLastWord(names);
        }
        return new ArrayList<>();
    }

     */

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
        if (i.getType() != Material.BOOK && i.getType() != Material.ENCHANTED_BOOK && i.getType() != Material.AIR) {
            say(p, "&cYou must have nothing or a book in your hand.");
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
            for (final Enchantment ench : Enchantment.values())
                i.addUnsafeEnchantment(ench, level);
            for (final VRNEnchants.VRN entry : VRNEnchants.VRN.values()) {
                BaseEnchant.applyEnchant(i, entry.getEnch(), level);
                BaseEnchant.updateLore(i);
            }
            return true;
        }
        final Enchantment enchant = checkArgs(args);
        if (enchant == null) {
            say(p, "That is not an available enchant.");
            return true;
        }
        int level = parseLevel(args, p);
        if (level < 1) {
            say(p, "&cLevel cannot be less than 1.");
            return true;
        }
        ItemStack newbook = BaseEnchant.generateEnchantBook(enchant, level);
        p.getInventory().setItemInMainHand(newbook);
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
        for (final Enchantment enchants : Enchantment.values())
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
            final ArrayList<String> names = new ArrayList<>();
            for (final Enchantment enchants : Enchantment.values()) {
                names.add(enchants.getKey().toString().replaceAll("minecraft:", "").replaceAll("vrnenchants:", ""));
                names.add("all");
            }
            return names;
        }
        return new ArrayList<>();
    }
}
