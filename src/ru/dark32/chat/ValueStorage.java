package ru.dark32.chat;

import java.util.List;

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

	public static void init() {
		experemental = Main.config.getBoolean("experemental", false);

		nei = ValueStorage.colorByString("String.nei");
		noPerm = ValueStorage.colorByString("String.noPerm");
		joinmsg = Main.localeConfig.getStringList("String.joinmsg");
		muteMessage = ValueStorage.colorByString("mute.message");
		muteHelp = Main.localeConfig.getStringList("mute.help");
		deafHelp = Main.localeConfig.getStringList("deaf.help");
		helpBase = Main.localeConfig.getStringList("help.base");
		helpHelp = Main.localeConfig.getStringList("help.help");
		helpChannel = Main.localeConfig.getStringList("help.channel");
	}

	private static String colorByString(final String key ) {
		return ChanelRegister.colorUTF8(Main.config.getString(key, key), 3);
	}
}
