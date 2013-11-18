package ru.dark32.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValueStorage {
	public static String			chanseLuck;
	public static String			chanseUnluck;
	public static String			chanseFormat;
	public static String			nei;
	public static String			noPerm;
	public static String			pmFormatTo;
	public static String			pmFormatFrom;
	public static String			chansrRollFormat;
	public static String			pmPlayeNotFound;
	public static String			muteMessage;
	public static List<String>		broadList;
	public static String			broadName;
	public static String			pmNoinputMsg;
	public static String			broadSpy;
	public static String			broadConsoleSpy;
	public static List<String>		helpBase;
	public static String			helpChangeChanel;
	public static List<String>		helpPrefix	= new ArrayList<String>();
	public static List<String>		helpChanelsSitch;
	public static List<String>		joinmsg;
	public static List<String>		muteMute;
	public static List<String>		muteHelp;
	public static String			helpMute;
	public static String			muteUnmute;
	public static String			muteUnknow;

	public static boolean			experemental;
	public static int				chanseDefaultRoll;
	public static int				PmSearchNickMode;
	public static int				chanseVaule;
	public static int				chanseMinRoll;

	protected final static Pattern	nickForMute	= Pattern.compile("%([\\d\\w_]+)\\s(.+)");

	public static Chanel			global;
	public static Chanel			world;
	public static Chanel			shout;
	public static Chanel			local;
	public static Chanel			whisper;
	public static Chanel			pm;
	public static Chanel            chance;
	public static Chanel 			broadcast;
	
	public static void init() {
		experemental = Main.config.getBoolean("experemental", false);
		
		global = new Chanel("Chat.Global.name", "Chat.Global.format",
							ChatMode.GLOBAL.getFirstChar(),ChatMode.GLOBAL.getSign());
		global.setId("Chat.Global.Id", 260);
		global.setSubId("Chat.Global.SubId", 0);
		global.setMaterial("Chat.Global.Ma");

		world = new Chanel("Chat.World.name", "Chat.World.format",
							ChatMode.WORLD.getFirstChar(),ChatMode.WORLD.getSign());
		world.setId("Chat.World.Id", 260);
		world.setSubId("Chat.World.SubId", 0);
		world.setMaterial("Chat.World.Ma");

		shout = new Chanel("Chat.Shout.name", "Chat.Shout.format",
							ChatMode.SHOUT.getFirstChar(),ChatMode.SHOUT.getSign());
		shout.setRange("Chat.Shout.range", 500d);

		local = new Chanel("Chat.Local.name", "Chat.Local.format",
							ChatMode.LOCAL.getFirstChar(),ChatMode.LOCAL.getSign());
		local.setRange("Chat.Local.range", 250d);

		whisper = new Chanel("Chat.Whisper.name", "Chat.Whisper.format",
							ChatMode.WHISPER.getFirstChar(),ChatMode.WHISPER.getSign());
		whisper.setRange("Chat.Whisper.range", 20d);

		pm = new Chanel("Chat.PM.name", "Chat.PM.formatTo",
						ChatMode.PM.getFirstChar(),ChatMode.PM.getSign());
		pmFormatTo = colorByString("Chat.PM.formatTo");
		pmFormatFrom = colorByString("Chat.PM.formatFrom");
		pmNoinputMsg = colorByString("Chat.PM.noinputmsg");
		pmPlayeNotFound = colorByString("Chat.PM.playeNotFound");
		PmSearchNickMode = Main.config.getInt("Chat.PM.PMSearchNickMode", 0);

	
		chance = new Chanel("Chat.Chance.name", "Chat.Chance.format",
							ChatMode.CHANCE.getFirstChar(),ChatMode.CHANCE.getSign());
		chanseDefaultRoll = Main.config.getInt("Chat.Chance.default", 5);
		chanseVaule = Main.config.getInt("Chat.Chance.vaule", 50);
		chanseMinRoll = Main.config.getInt("Chat.Chance.min", 5);
		chanseFormat = colorByString("Chat.Chance.format");
		chansrRollFormat = colorByString("Chat.Chance.formatroll");
		chanseLuck = colorByString("Chat.Chance.luck");
		chanseUnluck = colorByString("Chat.Chance.unluck");

		nei = colorByString("String.nei");
		noPerm = colorByString("String.noPerm");
		joinmsg = Main.config.getStringList("String.joinmsg");
		
		
		broadcast = new Chanel("Chat.Broad.name", "Chat.Broad.format",
				ChatMode.BROADCAST.getFirstChar(),ChatMode.BROADCAST.getSign());
		broadName = colorByString("Chat.Broad.name");
		broadSpy = colorByString("Chat.Broad.spy");
		broadConsoleSpy = colorByString("Chat.Broad.consoleSpy");
		broadList = Main.config.getStringList("Chat.Broad.list");

		muteMessage = colorByString("mute.message");
		muteUnmute = Main.config.getString("mute.unmute");
		muteUnknow = Main.config.getString("mute.unknow");
		muteMute = Main.config.getStringList("mue.mute");
		muteHelp = Main.config.getStringList("mue.help");
		
		helpMute = Main.config.getString("help.mute");
		helpChangeChanel = colorByString("help.changechanel");
		helpBase = Main.config.getStringList("help.base");
		List<String> _helpPrefix = Main.config.getStringList("help.prefix");
		for (String s : _helpPrefix) {
			helpPrefix.add(s.replace("$1", ChatMode.GLOBAL.getFirstLetter())
					.replace("$2", ChatMode.WORLD.getFirstLetter())
					.replace("$3", ChatMode.SHOUT.getFirstLetter())
					.replace("$4", ChatMode.LOCAL.getFirstLetter())
					.replace("$5", ChatMode.WHISPER.getFirstLetter())
					.replace("$6", ChatMode.PM.getFirstLetter())
					.replace("$7", String.valueOf(chanseMinRoll)));
		}
		_helpPrefix.clear();
		helpChanelsSitch = Main.config.getStringList("help.chanelswitch");
		
	}

	private static String colorByString(String key ) {
		return ChatListener.tCC(Main.config.getString(key, key));
	}
}
