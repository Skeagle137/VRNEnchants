package net.skeagle.vrnenchants.commands;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.enchant.enchantments.EnchSpeedy;
import net.skeagle.vrnenchants.util.VRNUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrnenchants.util.VRNUtil.say;

public class Enchant extends SimpleCommand {

    public Enchant() {
        super("enchant|ench");
        setDescription("Enchants the item held in your main hand.");
        setPermission("vrn.enchant");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        final ItemStack i = getPlayer().getInventory().getItemInMainHand();
        if (i.getType() == Material.AIR) {
            say(getPlayer(), "&cYou must be holding an item to enchant.");
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
        BaseEnchant.applyEnchant(i, enchant, level);
        BaseEnchant.updateLore(i);


    }

    private Enchantment checkArgs() {
        for (final Enchantment enchants : Enchantment.values())
            if (args[0].equalsIgnoreCase(enchants.getKey().toString().replaceAll("minecraft:", "").replaceAll("vrnenchants:","")))
                return enchants;
        return null;
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
}