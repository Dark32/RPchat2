package ru.dark32.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Material;

public class ValueStorage {
	public static class Global{
		public static String format;
		public static String name;
		@Deprecated
		public static int				Id;
		public static int				SubId;
		public static Material			Ma;
	}
	public static class World{
		public static String format;
		public static String name;
		@Deprecated
		public static int				Id;
		public static int				SubId;
		public static Material			Ma;
	}
	public static class Shout{
		public static String format;
		public static String name;
		public static double	 range;
	}
	public static class Local{
		public static String format;
		public static String name;
		public static double	 range;
	}
	public static class Whisper{
		public static String format;
		public static String name;
		public static double	 range;
	}
	public static String			luck;
	public static String			unluck;
	public static String			roll;
	public static String			nei;
	public static String			noPerm;
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
	public static List<String>		helpPrefix		= new ArrayList<String>();
	public static List<String>		chanelswitch;
	public static List<String>		joinmsg;
	public static List<String>		muteMute;
	public static List<String>		muteHelp2;
	public static String			muteHelp;
	public static String			muteUnmute;
	public static String			unknow;

	public static boolean			experemental	= false;
	/* <--experemental */
	public static int				randrolldef;
	public static int				PMSearchNickMode;
	public static int				defchanse;
	public static int				minroll;

	protected final static Pattern	nickForMute		= Pattern.compile("%([\\d\\w_]+)\\s(.+)");

	public static void init() {
		
		Global.Id = Main.config.getInt("Chat.Global.Id", 260);
		Global.SubId = Main.config.getInt("Chat.Global.SubId", 0);
		Global.Ma = Material.getMaterial(Main.config.getString("Chat.Global.Ma"));
		Global.name = ChatListener.tCC(Main.config.getString("Chat.Global.name"));
		Global.format = ChatListener.tCC(Main.config.getString("Chat.Global.format"));
		
		World.Id = Main.config.getInt("Chat.World.Id", 260);
		World.SubId = Main.config.getInt("Chat.World.SubId", 0);
		World.Ma = Material.getMaterial(Main.config.getString("Chat.World.Ma"));
		World.name = ChatListener.tCC(Main.config.getString("Chat.World.name"));
		World.format = ChatListener.tCC(Main.config.getString("Chat.World.format"));
		
		Shout.name = ChatListener.tCC(Main.config.getString("Chat.Shout.name"));
		Shout.format = ChatListener.tCC(Main.config.getString("Chat.Shout.format"));
		Shout.range = Main.config.getDouble("Chat.Shout.range", 500d);
		
		Local.name = ChatListener.tCC(Main.config.getString("Chat.Local.name"));
		Local.format = ChatListener.tCC(Main.config.getString("Chat.Local.format"));
		Local.range = Main.config.getDouble("Chat.Local.range", 250d);
		
		Whisper.name = ChatListener.tCC(Main.config.getString("Chat.Whisper.name"));
		Whisper.format = ChatListener.tCC(Main.config.getString("Chat.Whisper.format"));
		Whisper.range = Main.config.getDouble("Chat.Whisper.range", 20d);
		
		experemental = Main.config.getBoolean("experemental", experemental);
	
		randrolldef = Main.config.getInt("roll.def", 5);
		defchanse = Main.config.getInt("roll.defchanse", 50);
		minroll = Main.config.getInt("roll.min", 5);
		
		PMSearchNickMode = Main.config.getInt("PMSearchNickMode", 0);

		luck = ChatListener.tCC(Main.config.getString("String.luck"));
		unluck = ChatListener.tCC(Main.config.getString("String.unluck"));
		roll = ChatListener.tCC(Main.config.getString("String.roll"));
		rnd = ChatListener.tCC(Main.config.getString("String.rnd"));
		nei = ChatListener.tCC(Main.config.getString("String.nei"));
		noPerm = ChatListener.tCC(Main.config.getString("String.noPerm"));
		
		pmChatFormat = ChatListener.tCC(Main.config.getString("String.localChatFormat"));
		pmChatFormat2 = ChatListener.tCC(Main.config.getString("String.pmChatFormat2"));
		playeNotFound = ChatListener.tCC(Main.config.getString("String.playeNotFound"));
		mute = ChatListener.tCC(Main.config.getString("String.mute"));
		broadcast = ChatListener.tCC(Main.config.getString("String.broadcast"));
		noinputmsg = ChatListener.tCC(Main.config.getString("String.noinputmsg"));
		trybroadcastspy = ChatListener.tCC(Main.config.getString("String.trybroadcastspy"));
		broadcastspy = ChatListener.tCC(Main.config.getString("String.broadcastspy"));
		changechanel = ChatListener.tCC(Main.config.getString("help.changechanel"));
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
