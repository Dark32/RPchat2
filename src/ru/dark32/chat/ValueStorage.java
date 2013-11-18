package ru.dark32.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValueStorage {
	public static String			nei;
	public static String			noPerm;
	public static String			muteMessage;
	public static List<String>		broadList;
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
	public static Chanel			chance;
	public static Chanel			broadcast;

	public static void init() {
		experemental = Main.config.getBoolean("experemental", false);

		global = new Chanel("Chat.Global.name", "Chat.Global.format", "Chat.Global.prefix", 'g');
		global.setId("Chat.Global.Id", 260);
		global.setSubId("Chat.Global.SubId", 0);
		global.setMaterial("Chat.Global.Ma");

		world = new Chanel("Chat.World.name", "Chat.World.format", "Chat.Global.prefix", 'w');
		world.setId("Chat.World.Id", 260);
		world.setSubId("Chat.World.SubId", 0);
		world.setMaterial("Chat.World.Ma");

		shout = new Chanel("Chat.Shout.name", "Chat.Shout.format", "Chat.Shout.prefix", 's');
		shout.setRange("Chat.Shout.range", 500d);

		local = new Chanel("Chat.Local.name", "Chat.Local.format", "Chat.Local.prefix", 'l');
		local.setRange("Chat.Local.range", 250d);

		whisper = new Chanel("Chat.Whisper.name", "Chat.Whisper.format", "Chat.Whisper.prefix", 'v');
		whisper.setRange("Chat.Whisper.range", 20d);

		pm = new Chanel("Chat.PM.name", "Chat.PM.formatTo", "Chat.PM.prefix", 'p');
		pm.set("formatFrom", colorByString("Chat.PM.formatFrom"));
		pm.set("NoinputMsg", colorByString("Chat.PM.noinputmsg"));
		pm.set("PlayeNotFound", colorByString("Chat.PM.playeNotFound"));
		PmSearchNickMode = Main.config.getInt("Chat.PM.PMSearchNickMode", 0);

		chance = new Chanel("Chat.Chance.name", "Chat.Chance.format", "Chat.Chance.prefix", 'c');
		chance.set("RollFormat", "Chat.Chance.formatroll");
		chance.set("Luck", "Chat.Chance.luck");
		chance.set("Unluck", "Chat.Chance.unluck");
		chanseDefaultRoll = Main.config.getInt("Chat.Chance.default", 5);
		chanseVaule = Main.config.getInt("Chat.Chance.vaule", 50);
		chanseMinRoll = Main.config.getInt("Chat.Chance.min", 5);

		nei = colorByString("String.nei");
		noPerm = colorByString("String.noPerm");
		joinmsg = Main.config.getStringList("String.joinmsg");

		broadcast = new Chanel("Chat.Broad.name", "Chat.Broad.format", "Chat.Broad.prefix", 'b');
		broadcast.set("Spy", colorByString("Chat.Broad.spy"));
		broadcast.set("ConsoleSpy", colorByString("Chat.Broad.consoleSpy"));
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
			helpPrefix.add(s.replace("$1", global.getFirstLetter())
					.replace("$2", world.getFirstLetter())
					.replace("$3", shout.getFirstLetter())
					.replace("$4", local.getFirstLetter())
					.replace("$5", whisper.getFirstLetter())
					.replace("$6", pm.getFirstLetter())
					.replace("$7", String.valueOf(chanseMinRoll)));
		}
		_helpPrefix.clear();
		helpChanelsSitch = Main.config.getStringList("help.chanelswitch");

	}

	private static String colorByString(String key ) {
		return ChatListener.tCC(Main.config.getString(key, key));
	}
}
