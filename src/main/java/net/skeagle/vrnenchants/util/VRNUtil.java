package net.skeagle.vrnenchants.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.skeagle.vrnenchants.VRNEnchants;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.logging.Level;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

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

    public static void log(String... message) {
        log(Level.INFO, message);
    }

    public static void log(Level level, String... message) {
        for (String s : message)
            VRNEnchants.getInstance().getLogger().log(level, s);
    }

    public static String color(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (s.length() - i > 8) {
                String temp = s.substring(i, i + 8);
                if (temp.startsWith("&#")) {
                    char[] chars = temp.replaceFirst("&#", "").toCharArray();
                    StringBuilder rgbColor = new StringBuilder();
                    rgbColor.append("&x");
                    for (char c : chars)
                        rgbColor.append("&").append(c);
                    s = s.replaceAll(temp, rgbColor.toString());
                }
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String[] color(String... i) {
        for (String uncolored : i)
            translateAlternateColorCodes('&', uncolored);
        return i;
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
