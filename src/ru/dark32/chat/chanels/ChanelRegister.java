package ru.dark32.chat.chanels;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import ru.dark32.chat.Main;
import ru.dark32.chat.ichanels.ETypeChanel;
import ru.dark32.chat.ichanels.IChanel;
import ru.dark32.chat.ichanels.IItemChanel;
import ru.dark32.chat.ichanels.IRangeChanel;

/**
 * @author Andrew
 * 
 */
public class ChanelRegister {
	/**
	 * иницилизируем каналы
	 */
	public static int				intex	= 0;
	private static List<IChanel>	listChat;

	public static void init() {
		listChat = new LinkedList<IChanel>();
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
			if (chanelType.equals("none") || chanelEnable) {
				continue;
			}
			listChat.add(registrChanel(ETypeChanel.get(_type), name));
		}
	}

	private static IChanel registrChanel(ETypeChanel type, String name ) {
		IChanel chanel = type.setChanel(name);
		return chanel;
	}
	public static int getIndex() {
		return intex++;
	}
}
