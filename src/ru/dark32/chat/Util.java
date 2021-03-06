package ru.dark32.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.dark32.chat.ichanels.IChanel;
import ru.dark32.perm.PermissionsHandler;

public class Util {
	public static Map<String, Integer>	modes;
	private static String				luck;
	private static String				unluck;
	private static int					chance;

	public static int getModeIndex(final String name ) {
		if (!modes.containsKey(name)) {
			return ChanelRegister.getDefaultChanel(Bukkit.getPlayer(name));
		} else {
			return modes.get(name);
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

	public static void init(final Main main ) {
		modes = new HashMap<String, Integer>();
		luck = ChanelRegister.colorUTF8(Main.localeConfig.getString("String.chance.luck", "(luck)"), 3);
		unluck = ChanelRegister.colorUTF8(Main.localeConfig.getString("String.chance.unluck", "(unluck)"), 3);
		chance = Main.config.getInt("chance", 50);
	}

	public static boolean isInteger(final String string ) {
		if (string == null || string.isEmpty()) {
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

	public static boolean setChatMode(String player, int cm ) {
		final IChanel chanel = ChanelRegister.getByIndex(cm);
		final boolean hasPermission = Main.getPermissionsHandler().hasPermission(player,
				Main.BASE_PERM + "." + chanel.getInnerName() + ".say");
		if ((!chanel.isNeedPerm() || hasPermission)) {
			if (modes.containsKey(player)) {
				modes.remove(player);
			}
			modes.put(player, cm);
			return true;
		} else {
			return false;
		}

	}

	/*
	 * Не используется
	 */
	/*
	 * final private static Pattern rollPatern = Pattern.compile("\\*(.+?)\\*");
	 * final private static Random rand = new Random(); public static String
	 * randomRoll(String message ) { Matcher mt = rollPatern.matcher(message);
	 * ChatColor.getLastColors(message); while (mt.find()) { message =
	 * message.replaceFirst("\\*(.+?)\\*", "$1 " + (rand.nextInt(100) > chance ?
	 * luck : unluck)); } message = message.replaceAll("\\*(.+?)\\Z", "$1 " +
	 * (rand.nextInt(100) > chance ? luck : unluck)); return message; }
	 */

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

	final private static String		suffixParsePatern	= "\\$\\((.+?)\\|(.+?)\\|(.+?)\\|(\\d+?)\\)";
	final private static Pattern	suffixParser		= Pattern.compile(suffixParsePatern);

	public static String suffixLatter(String message ) {
		Matcher matches = suffixParser.matcher(message);
		while (matches.find()) {
			int num = Integer.valueOf(matches.group(4));
			String suf = "";
			int val = num % 100;
			if (val > 10 && val < 20) {
				suf = matches.group(3);
			} else {
				val = num % 10;
				if (val == 1) {
					suf = matches.group(1);
				} else if (val > 1 && val < 5) {
					suf = matches.group(2);
				} else {
					suf = matches.group(3);
				}
			}
			message = message.replaceFirst(suffixParsePatern, suf);
		}
		return message;
	}

	final public static void DEBUG(Object message, CommandSender sender ) {
		if (Main.DEBUG_MODE) {
			sender.sendMessage(message.toString());
		}
	}

	final public static void DEBUG(Object message ) {
		DEBUG(message, Bukkit.getConsoleSender());
	}

	public static String translateColorCodes(char altColorChar, String textToTranslate, char color ) {
		char[] b = textToTranslate.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == altColorChar && b[i + 1] == color) {
				b[i] = ChatColor.COLOR_CHAR;
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}
		return new String(b);
	}

	final static List<Character>	colors	= new ArrayList<Character>();
	static {
		for (ChatColor chatColor : ChatColor.values()) {
			colors.add(chatColor.getChar());
		}
	}

	final public static String coloreChat(Player player, String message, String innerName ) {
		for (char color : colors) {
			boolean perm_color = Main.getPermissionsHandler().hasPermission(player, Main.BASE_PERM + ".color." + color)
					|| Main.getPermissionsHandler().hasPermission(player,
							Main.BASE_PERM + "." + innerName + ".color." + color);
			if (message.contains("&" + color)) {
				if (perm_color) {
					message = translateColorCodes('&', message, color);
				}
			}
		}
		return message;
	}
}
