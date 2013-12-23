package ru.dark32.chat.chanels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ichanels.ETypeChanel;
import ru.dark32.chat.ichanels.IChanel;
import ru.dark32.chat.ichanels.IItemChanel;
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
	public static int			intex	= 0;
	public static List<IChanel>	listChat;
	public static int			defaultChanel;

	public static void init() {
		listChat = new ArrayList<IChanel>();
		final ConfigurationSection chtatList = Main.chatConfig.getConfigurationSection("Chat");
		if (chtatList == null) {
			Bukkit.getConsoleSender().sendMessage("Ошибка. Каналы чата не найдены в конфиге");
			return;
		}
		final Set<String> list = chtatList.getKeys(false);
		for (final String name : list) {
			final String _type = "Chat." + name + ".type";
			final String _enable = "Chat." + name + ".enable";
			final String chanelType = Main.chatConfig.getString(_type, _type);
			final boolean chanelEnable = Main.chatConfig.getBoolean(_enable, false);
			if (chanelType.equals("none") || !chanelEnable) {
				continue;
			}
			listChat.add(ChanelRegister.registrChanel(ETypeChanel.get(chanelType), name));
		}
	}

	private static IChanel registrChanel(final ETypeChanel type, final String name ) {
		final IChanel chanel = type.setChanel(name);
		chanel.setType(type);
		if (Main.chatConfig.getBoolean("Chat." + name + ".default", false)) {
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
		if (id > listChat.size()) {
			id = defaultChanel;
		}
		return listChat.get(id);
	}

	public static int getIndexBySign(final char sign ) {
		for (final IChanel chanel : listChat) {
			if (chanel.getSign() == sign) {
				return chanel.getIndex();
			}
		}
		return -1;
	}

	public static int getIndexByItem(final ItemStack item ) {
		for (final IChanel chanel : listChat) {
			if ((chanel.getType() == ETypeChanel.ITEM || chanel.getType() == ETypeChanel.RANGE_ITEM || chanel.getType() == ETypeChanel.REQUISITE)
					&& ((IItemChanel) chanel).equalItem(item) && !((IItemChanel) chanel).isRequestPprefix()) {
				return chanel.getIndex();
			}
		}
		return -1;
	}

	public static int getIndexByPrefix(final Player sender, final char preffix ) {
		for (final IChanel chanel : listChat) {
			if ((chanel.getPrefix() == preffix)
					&& (!chanel.isNeedPerm() || Util.hasPermission(sender, "mcnw.spy") || Util.hasPermission(sender,
							Main.BASE_PERM + "." + chanel.getInnerName() + ".say"))) {
				return chanel.getIndex();
			}
		}
		return -1;
	}

	public static String colorize(final String string ) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static String getPreffix(final String name ) {
		if (!Util.usePEX) {
			return "";
		}
		final PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);
		if (user == null) {
			return "";
		}

		return ChanelRegister.colorize(user.getPrefix());
	}

	public static String getSuffix(final String name ) {
		if (!Util.usePEX) {
			return "";
		}
		final PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);
		if (user == null) {
			return "";
		}
		return ChanelRegister.colorize(user.getSuffix());
	}

	public static int getChanels() {
		return listChat.size();
	}
}
