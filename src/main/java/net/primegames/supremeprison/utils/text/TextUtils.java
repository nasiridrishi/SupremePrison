package net.primegames.supremeprison.utils.text;

import net.primegames.supremeprison.utils.compat.MinecraftVersion;
import net.primegames.supremeprison.utils.player.PlayerUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    private TextUtils() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static String applyColor(String message) {
        if (MinecraftVersion.atLeast(MinecraftVersion.V.v1_16)) {
            Matcher matcher = HEX_PATTERN.matcher(message);
            while (matcher.find()) {
                final ChatColor hexColor = ChatColor.of(matcher.group().substring(1));
                final String before = message.substring(0, matcher.start());
                final String after = message.substring(matcher.end());
                message = before + hexColor + after;
                matcher = HEX_PATTERN.matcher(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> applyColor(List<String> list) {
        List<String> returnVal = new ArrayList<>(list.size());
        list.forEach(s -> returnVal.add(applyColor(s)));
        return returnVal;
    }

    public static void sendMessage(Player player, String message) {
        PlayerUtils.sendMessage(player, PlaceholderAPI.setPlaceholders(player, applyColor(message)));
    }
}
