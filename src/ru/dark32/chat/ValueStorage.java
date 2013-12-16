package ru.dark32.chat;

import java.util.List;
import ru.dark32.chat.chanels.ChanelRegister;

public class ValueStorage {
	public static String		nei;
	public static String		noPerm;
	public static String		muteMessage;
	public static List<String>	helpBase;
	public static List<String>	helpHelp;
	public static List<String>	helpChannel;
	public static List<String>	joinmsg;
	public static List<String>	muteHelp;
	public static List<String>	deafHelp;

	public static boolean		experemental;
	public static boolean		lister;

	public static void init() {
		experemental = Main.config.getBoolean("experemental", false);

		nei = colorByString("String.nei");
		noPerm = colorByString("String.noPerm");
		joinmsg = Main.config.getStringList("String.joinmsg");
		muteMessage = colorByString("mute.message");
		muteHelp = Main.config.getStringList("mute.help");
		deafHelp = Main.config.getStringList("deaf.help");
		lister = Main.config.getBoolean("liteners", true);
		helpBase = Main.config.getStringList("help.base");
		helpHelp = Main.config.getStringList("help.help");
		helpChannel = Main.config.getStringList("help.channel");
	}

	private static String colorByString(final String key ) {
		return ChanelRegister.colorize(Main.config.getString(key, key));
	}
}
