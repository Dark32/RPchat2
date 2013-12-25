package ru.dark32.chat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.dark32.chat.chanels.ChanelRegister;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Util {
	public static Map<String, Integer>	modes2;
	static boolean						usePB;
	public static boolean				usePEX;
	private static String				luck;
	private static String				unluck;
	private static int					chance;

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
		luck = ChanelRegister.colorize(Main.localeConfig.getString("String.chance.luck", "(luck)"));
		unluck = ChanelRegister.colorize(Main.localeConfig.getString("String.chance.unluck", "(unluck)"));
		chance = Main.config.getInt("chance", 50);
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

	final private static Pattern	rollPatern	= Pattern.compile("\\*(.+?)\\*");
	final private static Random		rand		= new Random();

	public static String randomRoll(String message ) {
		Matcher mt = rollPatern.matcher(message);
		ChatColor.getLastColors(message);
		while (mt.find()) {
			message = message.replaceFirst("\\*(.+?)\\*", "$1 " + (rand.nextInt(100) > chance ? luck : unluck));
		}
		message = message.replaceAll("\\*(.+?)\\Z", "$1 " + (rand.nextInt(100) > chance ? luck : unluck));
		return message;
	}

	public static String parseUTF8(String instr ) {
		char[] in = instr.toCharArray();
		int len = in.length;
		int off = 0;
		char[] convtBuf = new char[len];
		char aChar;
		char[] out = convtBuf;
		int outLen = 0;
		int end = off + len;
		while (off < end) {
			aChar = in[off++];
			if (aChar == '\\') {
				aChar = in[off++];
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = in[off++];
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								Bukkit.getConsoleSender().sendMessage("Malformed \\uxxxx encoding.");
								// throw new
								// IllegalArgumentException("Malformed \\uxxxx encoding.");
						}
					}
					out[outLen++] = (char) value;
				} else {
					if (aChar == 't') aChar = '\t';
					else if (aChar == 'r') aChar = '\r';
					else if (aChar == 'n') aChar = '\n';
					else if (aChar == 'f') aChar = '\f';
					out[outLen++] = aChar;
				}
			} else {
				out[outLen++] = aChar;
			}
		}
		return new String(out, 0, outLen);
	}
}
