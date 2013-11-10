package ru.dark32.chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Material;

public class ValueStorage {
	public static String			luck;
	public static String			unluck;
	public static String			roll;
	public static String			nei;
	public static String			noPerm;
	public static String			globalChat;
	public static String			worldChat;
	public static String			globalChatFormat;
	public static String			worldChatFormat;
	public static String			shoutChatFormat;
	public static String			whisperingChatFormat;
	public static String			localChatFormat;
	public static String			pmChatFormat;
	public static String			pmChatFormat2;
	public static String			rnd;
	public static String			playeNotFound;
	public static String			mute;
	public static List<String>		broadcastList;
	public static String			broadcast;
	public static String			noinputmsg;
	public static String			trybroadcastspy;
	public static String			broadcastspy;
	public static List<String>		baseHelp;
	public static String			changechanel;
	public static String			shoutChat;
	public static String			localChat;
	public static String			whisperingChat;
	public static List<String>		helpPrefix		= new ArrayList<String>();
	public static List<String>		chanelswitch;
	public static List<String>		joinmsg;
	public static List<String>		muteMute;
	public static List<String>		muteHelp2;
	public static String			muteHelp;
	public static String			muteUnmute;
	public static String			unknow;

	public static double			rangeLocal;
	public static double			RangeWhispering;
	public static double			RangeShout;
	@Deprecated
	public static int				globalId;
	public static int				globalSubId;
	@Deprecated
	public static int				worldId;
	public static int				worldSubId;
	/* experemental--> */
	public static Material			globalMa;
	public static Material			worldMa;
	public static boolean			experemental	= false;
	/* <--experemental */
	public static int				randrolldef;
	public static int				PMSearchNickMode;
	public static int				defchanse;
	public static int				minroll;

	protected final static Pattern	nickForMute		= Pattern.compile("%([\\d\\w_]+)\\s(.+)");

	public static void init() {
		rangeLocal = Main.config.getDouble("Range.main", 250d);
		RangeWhispering = Main.config.getDouble("Range.whispering", 10d);
		RangeShout = Main.config.getDouble("Range.shout", 500d);
		globalId = Main.config.getInt("Id.globalId", 260);
		globalSubId = Main.config.getInt("Id.globalSubId", 0);
		worldId = Main.config.getInt("Id.worldId", 264);
		/* experemental--> */
		globalMa = Material.getMaterial(Main.config.getString("Id.globalMa"));
		worldMa = Material.getMaterial(Main.config.getString("Id.worldMa", "APPLE"));
		experemental = Main.config.getBoolean("Prefix", experemental);
		/* <--experemental */
		worldSubId = Main.config.getInt("Id.worldSubId", 0);
		randrolldef = Main.config.getInt("randrolldef", 5);
		defchanse = Main.config.getInt("defchanse", 50);
		minroll = Main.config.getInt("minroll", 5);
		PMSearchNickMode = Main.config.getInt("PMSearchNickMode", 0);

		luck = ChatListener.tCC(Main.config.getString("String.luck"));
		unluck = ChatListener.tCC(Main.config.getString("String.unluck"));
		roll = ChatListener.tCC(Main.config.getString("String.roll"));
		rnd = ChatListener.tCC(Main.config.getString("String.rnd"));
		nei = ChatListener.tCC(Main.config.getString("String.nei"));
		noPerm = ChatListener.tCC(Main.config.getString("String.noPerm"));
		globalChat = ChatListener.tCC(Main.config.getString("String.globalChat"));
		worldChat = ChatListener.tCC(Main.config.getString("String.worldChat"));
		globalChatFormat = ChatListener.tCC(Main.config.getString("String.globalChatFormat"));
		worldChatFormat = ChatListener.tCC(Main.config.getString("String.worldChatFormat"));
		shoutChatFormat = ChatListener.tCC(Main.config.getString("String.shoutChatFormat"));
		whisperingChatFormat = ChatListener.tCC(Main.config
				.getString("String.whisperingChatFormat"));
		localChatFormat = ChatListener.tCC(Main.config.getString("String.localChatFormat"));
		pmChatFormat = ChatListener.tCC(Main.config.getString("String.localChatFormat"));
		pmChatFormat2 = ChatListener.tCC(Main.config.getString("String.pmChatFormat2"));
		playeNotFound = ChatListener.tCC(Main.config.getString("String.playeNotFound"));
		mute = ChatListener.tCC(Main.config.getString("String.mute"));
		broadcast = ChatListener.tCC(Main.config.getString("String.broadcast"));
		noinputmsg = ChatListener.tCC(Main.config.getString("String.noinputmsg"));
		trybroadcastspy = ChatListener.tCC(Main.config.getString("String.trybroadcastspy"));
		broadcastspy = ChatListener.tCC(Main.config.getString("String.broadcastspy"));
		changechanel = ChatListener.tCC(Main.config.getString("help.changechanel"));
		shoutChat = ChatListener.tCC(Main.config.getString("String.shoutChat"));
		localChat = ChatListener.tCC(Main.config.getString("String.localChat"));
		whisperingChat = ChatListener.tCC(Main.config.getString("String.whisperingChat"));
		muteHelp = Main.config.getString("mute.help");
		muteUnmute = Main.config.getString("mute.unmute");
		unknow = Main.config.getString("mute.unknow");

		broadcastList = Main.config.getStringList("String.broadcastList");
		baseHelp = Main.config.getStringList("help.base");
		List<String> _helpPrefix = Main.config.getStringList("help.prefix");
		for (String s : _helpPrefix) {
			helpPrefix.add(s.replace("$1", ChatMode.GLOBAL.getFirstLetter())
					.replace("$2", ChatMode.WORLD.getFirstLetter())
					.replace("$3", ChatMode.SHOUT.getFirstLetter())
					.replace("$4", ChatMode.LOCAL.getFirstLetter())
					.replace("$5", ChatMode.WHISPER.getFirstLetter())
					.replace("$6", ChatMode.PM.getFirstLetter())
					.replace("$7", String.valueOf(minroll)));
		}
		_helpPrefix.clear();
		chanelswitch = Main.config.getStringList("help.chanelswitch");
		joinmsg = Main.config.getStringList("joinmsg");
		muteMute = Main.config.getStringList("mue.mute");
		muteHelp2 = Main.config.getStringList("mue.help2");
	}
}
