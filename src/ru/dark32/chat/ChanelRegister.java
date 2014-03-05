package ru.dark32.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ru.dark32.chat.ichanels.ETypeChanel;
import ru.dark32.chat.ichanels.IChanel;
import ru.dark32.chat.ichanels.IItemChanel;

/**
 * @author Andrew
 * 
 */
public class ChanelRegister {
	/**
	 * иницилизируем каналы
	 */
	private static int			index	= 0;
	public static List<IChanel>	listChat;
	private static int			defaultChanel;
	public static boolean		switchByPrefix;

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
		switchByPrefix = Main.config.getBoolean("switchByPrefix", false);
	}

	public static int getDefaultChanel(Player player ) {
		for (IChanel chat : listChat) {
			if (chat.isDefaultForGrop(Main.getPermissionsHandler().getGroup(player))) {
				return chat.getIndex();
			}
		}
		return defaultChanel;
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
		return index++;
	}

	public static int getIndex() {
		return index;
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
	public static int getIndexByInnerName(final String sign ) {
		for (final IChanel chanel : listChat) {
			if (chanel.getInnerName().equalsIgnoreCase(sign)) {
				return chanel.getIndex();
			}
		}
		return -1;
	}
	
	public static int getIndexBySignOrByInnerName(final CommandSender sender, final String par2){
		if (par2.length() != 1) {
			return ChanelRegister.getIndexByInnerName(par2);
		} else {
			return  ChanelRegister.getIndexBySign(par2.charAt(0));
		}
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
			if (chanel.getPrefix() != '\u0000'
					&& (chanel.getPrefix() == preffix)
					&& (!chanel.isNeedPerm() || Main.getPermissionsHandler().hasPermission(sender, "mcnw.spy") || Main
							.getPermissionsHandler().hasPermission(sender,
									Main.BASE_PERM + "." + chanel.getInnerName() + ".say"))) {
				return chanel.getIndex();
			}
		}
		return -1;
	}

	/**
	 * форматируем строку
	 * 
	 * @param string
	 *            исходная строка
	 * @param flag
	 *            0x01 - цвет, 0x02- UTF8
	 * @return обработанная строка
	 */
	public static String colorUTF8(String string, final int flag ) {
		if ((flag & 0x01) == 0x01) {
			string = ChatColor.translateAlternateColorCodes('&', string);
		}
		if ((flag & 0x02) == 0x02) {
			string = Util.parseUTF8(string);
		}
		return string;

	}

	public static int getChanels() {
		return listChat.size();
	}
}
