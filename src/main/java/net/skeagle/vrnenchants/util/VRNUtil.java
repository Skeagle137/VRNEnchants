package net.skeagle.vrnenchants.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class VRNUtil {

    public static String noperm = color("&cYou do not have permission to do this.");
    public static String noton = color("&cThat player is not online.");

    public static void say(final CommandSender cs, final String... message) {
        if (cs == null) return;
        for (final String msg : message) {
            cs.sendMessage(color(msg));
        }
    }

    public static void sayNoPrefix(final CommandSender cs, final String... message) {
        if (cs == null) return;
        for (final String msg : message) {
            cs.sendMessage(color(msg));
        }
    }

    public static void sayAndLog(final CommandSender cs, final String... message) {
        sayNoPrefix(cs, message);
        Common.logNoPrefix(color(message));
    }

    public static String color(final String i) {
        return translateAlternateColorCodes('&', i);
    }

    public static String[] color(final String... i) {
        for (final String uncolored : i) {
            translateAlternateColorCodes('&', uncolored);
        }
        return i;
    }

    public static void sayActionBar(final Player p, final String msg) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(color(msg)));
    }

    public static class LocationSerialization {
        public static String serialize(final Location loc) {
            if (loc == null || loc.getWorld() == null) {
                return null;
            }
            return loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch();
        }

        public static Location deserialize(final String s) {
            final String[] split = s.split(" ");
            if (split.length != 6) {
                return null;
            }
            return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        }
    }
}
