package ru.dark32.chat;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.dark32.chat.chanels.ChanelRegister;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Util {

	public static HashMap<String, Integer>	modes;
	public static HashMap<String, Integer>	modes2;
	
	static boolean							usePB	= false;
	public static boolean							usePEX	= false;

	public static int getChatMode(String player ) {
		if (!modes.containsKey(player)) {
			return ValueStorage.local.getIndex();
		} else {
			return modes.get(player);
		}
	}
	
	public static int getModeIndex(String name ) {
		if (!modes2.containsKey(name)) {
			return ChanelRegister.defaultChanel;
		} else {
			return modes2.get(name);
		}
	}
	public static Player getPlayerSoft(final String name ) {
		if (name.equals("")) {
			return null;
		}
		Player[] players = Bukkit.getServer().getOnlinePlayers();
		Player found = null;
		String lowerName = name.toLowerCase();
		int delta = Integer.MAX_VALUE;
		for (Player player : players) {
			if (player.getName().toLowerCase().indexOf(lowerName) != -1) {
				int curDelta = player.getName().length() - lowerName.length();
				if (curDelta < delta) {
					found = player;
					delta = curDelta;
				}
				if (curDelta == 0) {
					break;
				}
			}
		}
		return found;
	}

	public static boolean hasPermission(CommandSender player, String permission ) {
		if (!(player instanceof Player )){
			return true;
		}else if (usePEX) {
			return PermissionsEx.getUser((Player)player).has(permission);
		} else if (usePB) {
			return player.hasPermission(permission);
		} else {
			return player.isOp();
		}
	}

	public static void init(Main main ) {
		modes = new HashMap<String, Integer>();
		modes2 = new HashMap<String, Integer>();
	}

	public static boolean isInteger(String string ) {
		if (string == null || string.length() == 0) {
			return false;
		}
		int i = 0;
		int len = string.length();
		if (string.charAt(0) == '-') {
			if (len == 1) {
				return false;
			}
			i = 1;
		}
		char c;
		for (; i < len; i++) {
			c = string.charAt(i);
			if (!(c >= '0' && c <= '9')) {
				return false;
			}
		}
		return true;
	}

	public static void setChatMode(String player, int cm ) {
		if (modes.containsKey(player)) {
			modes.remove(player);
		}
		modes.put(player, cm);
	}

}
