package ru.dark32.chat.chanels;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ichanels.ETypeChanel;
import ru.dark32.chat.ichanels.IChanel;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * @author Andrew
 * 
 */
public class ChanelRegister {
	/**
	 * иницилизируем каналы
	 */
	public static int				intex	= 0;
	public static List<IChanel>		listChat;
	public static List<Character>	signes;
	public static List<Character>	prefixes;
	public static int				defaultChanel;

	public static void init() {
		listChat = new ArrayList<IChanel>();
		signes = new ArrayList<Character>();
		prefixes = new ArrayList<Character>();
		ConfigurationSection chtatList = Main.config.getConfigurationSection("Chat");
		if (chtatList == null) {
			Bukkit.getConsoleSender().sendMessage("Ошибка. Каналы чата не найдены в конфиге");
			return;
		}
		Set<String> list = chtatList.getKeys(false);
		for (String name : list) {
			String _type = "Chat." + name + ".type";
			String _enable = "Chat." + name + ".enable";
			String chanelType = Main.config.getString(_type, _type);
			boolean chanelEnable = Main.config.getBoolean(_enable, false);
			if (chanelType.equals("none") || !chanelEnable) continue;
			listChat.add(registrChanel(ETypeChanel.get(chanelType), name));
		}
	}

	private static IChanel registrChanel(ETypeChanel type, String name ) {
		IChanel chanel = type.setChanel(name);
		chanel.setType(type);
		signes.add(chanel.getSign());
		prefixes.add(chanel.getPrefix());
		if (Main.config.getBoolean("Chat." + name + ".default", false)) {
			defaultChanel = chanel.getIndex();
		}
		return chanel;
	}

	public static int getNextIndex() {
		return intex++;
	}

	public static int getIndex() {
		return intex;
	}

	public static IChanel getByIndex(int id ) {
		if (id > listChat.size()) id = defaultChanel;
		return listChat.get(id);
	}

	public static int getIndexBySign(char sign ) {
		return signes.indexOf(sign);
	}

	public static int getIndexByPrefix(char preffix ) {
		return prefixes.indexOf(preffix);
	}

	public static String colorize(String string ) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}


	public static String getPreffix(String name ) {
		if (!Util.usePEX) {
			return "";
		}
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);
		if (user == null) {
			return "";
		}

		return colorize(user.getPrefix());
	}

	public static String getSuffix(String name ) {
		if (!Util.usePEX) {
			return "";
		}
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);
		if (user == null) {
			return "";
		}
		return colorize(user.getSuffix());
	}
}
