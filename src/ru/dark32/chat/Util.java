package ru.dark32.chat;

import java.util.HashMap;

import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Util {

	public static HashMap<String, Integer>	modes;

	static boolean							usePEX	= false;
	static boolean							usePB	= false;

	public static boolean hasPermission(Player player, String permission ) {
		if (usePEX) {
			return PermissionsEx.getUser(player).has(permission);
		} else if (usePB) {
			return player.hasPermission(permission);
		} else {
			return player.isOp();
		}
	}

	public static void init(Main main ) {
		modes = new HashMap<String, Integer>();
	}

	public static void setChatMode(String player, int cm ) {
		if (modes.containsKey(player)) {
			modes.remove(player);
		}
		modes.put(player, cm);
	}

	public static int getChatMode(String player ) {
		if (!modes.containsKey(player)) {
			return ChatMode.LOCAL.getModeId();
		} else {
			return modes.get(player);
		}
	}
}
