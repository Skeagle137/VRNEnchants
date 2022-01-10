package net.skeagle.vrnenchants.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

import static net.skeagle.vrncommands.BukkitUtils.color;

public class VRNUtil {

    public final static String NOPERM = color("&cYou do not have permission to do this.");

    public static void say(CommandSender cs, String... message) {
        if (cs == null) return;
        for (String msg : message)
            cs.sendMessage(color(msg));
    }

    public static void sayNoPrefix(CommandSender cs, String... message) {
        if (cs == null) return;
        for (String msg : message)
            cs.sendMessage(color(msg));
    }

    public static void sayActionBar(Player p, String msg) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(color(msg)));
    }

    public static int rng(int min, int max) {
        Random r = new Random();
        return r.ints(min, max + 1).findFirst().getAsInt();
    }
}
