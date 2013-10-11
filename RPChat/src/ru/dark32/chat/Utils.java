package ru.dark32.chat;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Utils {

    public static HashMap<Player, Integer> modes;
    static boolean usePEX = false;
    static boolean usePB = false;
    
    public static boolean hasPermission(Player player, String permission) {
        if (usePEX) {
            return PermissionsEx.getUser(player).has(permission);
        } else if (usePB) {
            return player.hasPermission(permission);
        } else {
            return player.isOp();
        }
    }

    public static void init() {
        modes = new HashMap<Player, Integer>();
    }

    public static void setChatMode(Player player, int cm) {
        if (modes.containsKey(player)) {
            modes.remove(player);
        }
        modes.put(player, cm);
    }

    public static int getChatMode(Player player) {
        if (!modes.containsKey(player)) {
            return ChatMode.LOCAL.getModeId();
        } else {
            return modes.get(player);
        }
    }
}
