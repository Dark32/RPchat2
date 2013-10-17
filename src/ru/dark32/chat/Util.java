package ru.dark32.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Util {

	public static HashMap<String, Integer>	modes;

	static boolean							usePEX	= false;
	static boolean							usePB	= false;
	//static List<String> testStrings = new ArrayList<String>();
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
	/*	testStrings.add("qwert");
		testStrings.add("wqwert");
		testStrings.add("eqwert");
		testStrings.add("rqwert");
		testStrings.add("tqwert");
		testStrings.add("yqwert");
		testStrings.add("iuqwert");
		testStrings.add("iqwert");
		testStrings.add("oqwert");
		testStrings.add("ooqwert");
		testStrings.add("pqwert");
		testStrings.add("aqwert");
		testStrings.add("sqwert");
		testStrings.add("dqwert");
		testStrings.add("fqwert");
		testStrings.add("gqwert");
		testStrings.add("hqwert");
		testStrings.add("jqwert");
		testStrings.add("kjkqwert");*/
		
		
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

	public static Player getPlayerSoft(final String name ) {
		if (name.equals("")) {
			return null;
		}
		Player[] players = Bukkit.getServer().getOnlinePlayers();
		Player found = null;
		String lowerName = name.toLowerCase();
		int delta = Integer.MAX_VALUE;
		for (Player player : players) {
			if (player.getName().toLowerCase().indexOf(lowerName)!=-1) {
				int curDelta = player.getName().length() - lowerName.length();
				if (curDelta < delta) {
					found = player;
					delta = curDelta;
				}
				if (curDelta == 0) break;
			}
		}
		return found;
	}
	
}
