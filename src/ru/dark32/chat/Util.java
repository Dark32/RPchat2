package ru.dark32.chat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.dark32.chat.chanels.ChanelRegister;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Util {
	public static Map<String, Integer>	modes2;

	static boolean						usePB;
	public static boolean				usePEX;

	public static int getModeIndex(final String name ) {
		if (!modes2.containsKey(name)) {
			return ChanelRegister.defaultChanel;
		} else {
			return modes2.get(name);
		}
	}

	public static Player getPlayerSoft(final String name ) {
		if (name.isEmpty()) {
			return null;
		}
		final Player[] players = Bukkit.getServer().getOnlinePlayers();
		Player found = null;
		final String lowerName = name.toLowerCase(Locale.US);
		int delta = Integer.MAX_VALUE;
		for (final Player player : players) {
			if (player.getName().toLowerCase().indexOf(lowerName) != -1) {
				final int curDelta = player.getName().length() - lowerName.length();
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

	public static boolean hasPermission(final CommandSender player, final String permission ) {
		if (!(player instanceof Player)) {
			return true;
		} else if (usePEX) {
			return PermissionsEx.getUser((Player) player).has(permission);
		} else if (usePB) {
			return player.hasPermission(permission);
		} else {
			return player.isOp();
		}
	}

	public static void init(final Main main ) {
		modes2 = new HashMap<String, Integer>();
	}

	public static boolean isInteger(final String string ) {
		if (string == null || string.length() == 0) {
			return false;
		}
		int i = 0;
		final int len = string.length();
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
		if (modes2.containsKey(player)) {
			modes2.remove(player);
		}
		modes2.put(player, cm);
	}

	static Pattern				rollPatern	= Pattern.compile("\\*(.+?)\\*");
	final private static Random	rand		= new Random();

	public static String randomRoll(String message ) {
		Matcher mt = rollPatern.matcher(message);
		while (mt.find())
			message = message.replaceFirst("\\*(.+?)\\*", "$1" + (rand.nextInt(100) > 50 ? "(удачно)" : "(не удачно)"));
		message = message.replaceAll("\\*(.+?)\\Z", "$1" + (rand.nextInt(100) > 50 ? "(удачно)" : "(не удачно)"));
		return message;
	}

}
