package net.skeagle.vrnenchants.commands;

import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.VRNEnchants;
import net.skeagle.vrnenchants.enchant.enchantments.EnchSpeedy;
import net.skeagle.vrnenchants.util.VRNUtil;
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
        if (args.length < 1) {
            say(getPlayer(), "&cYou must provide an enchant name.");
            return;
        }
        if (args[0].equalsIgnoreCase("all")) {
            for (final Enchantment ench : Enchantment.values()) {
                i.addUnsafeEnchantment(ench, (args.length < 2 ? 1 : findNumber(1, "&cPlease specify a valid enchant level.")));
            }
            return;
        }
        final Enchantment enchant = checkArgs();
        if (enchant == null) {
            say(getPlayer(), "That is not an available enchant.");
            return;
        }
        int level = findNumber(1, "&cPlease specify a valid enchant level.");
        i.addUnsafeEnchantment(enchant, (args.length < 2 ? 1 : level));
        BaseEnchant.applyLore(enchant, level, i);


    }

    private Enchantment checkArgs() {
        for (final Enchantment enchants : Enchantment.values()) {
            if (args[0].equalsIgnoreCase(enchants.getKey().toString().replaceAll("minecraft:", "").replaceAll("vrnenchants:",""))) {
                return enchants;
            }
        }
        return null;
    }

    @Override
    protected List<String> tabComplete() {
        switch (args.length) {
            case 1:
                final ArrayList<String> names = new ArrayList<>();
                for (final Enchantment enchants : Enchantment.values()) {
                    names.add(enchants.getKey().toString().replaceAll("minecraft:", "").replaceAll("vrnenchants:",""));
                }
                return completeLastWord(names);
            case 2:
                return completeLastWord(new ArrayList<>());
        }
        return new ArrayList<>();
    }
}