package ru.dark32.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Material;

public class ValueStorage {	
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

	public static boolean			experemental;
	public static int				randrolldef;
	public static int				PMSearchNickMode;
	public static int				defchanse;
	public static int				minroll;

	protected final static Pattern	nickForMute		= Pattern.compile("%([\\d\\w_]+)\\s(.+)");
	
	public static Chanel global;
	public static Chanel world;
	public static Chanel shout;
	public static Chanel local;
	public static Chanel whisper;
	
	public static void init() {
		
		global = new Chanel("Chat.Global.name","Chat.Global.format");
		global.setId("Chat.Global.Id", 260);
		global.setSubId("Chat.Global.SubId", 0);
		global.setMaterial("Chat.Global.Ma");
		
		world = new Chanel("Chat.World.name","Chat.World.format");
		world.setId("Chat.World.Id", 260);
		world.setSubId("Chat.World.SubId", 0);
		world.setMaterial("Chat.World.Ma");
	
		shout =  new Chanel("Chat.Shout.name","Chat.Shout.format");
		shout.setRange("Chat.Shout.range", 500d);
		
		local =  new Chanel("Chat.Local.name","Chat.Local.format");
		local.setRange("Chat.Local.range", 250d);
		
		whisper =  new Chanel("Chat.Whisper.name","Chat.Whisper.format");
		whisper.setRange("Chat.Whisper.range", 20d);
		
		experemental = Main.config.getBoolean("experemental", false);
	
		randrolldef = Main.config.getInt("roll.def", 5);
		defchanse   = Main.config.getInt("roll.defchanse", 50);
		minroll     = Main.config.getInt("roll.min", 5);
		
		roll   = getByKey("String.roll");
		rnd    = getByKey("String.rnd");
		luck   = getByKey("String.luck");
		unluck = getByKey("String.unluck");
		
		nei    = getByKey("String.nei");
		noPerm = getByKey("String.noPerm");
		
		pmChatFormat    = getByKey("Chat.PM.formatTo");
		pmChatFormat2   = getByKey("Chat.PM.formatFrom");
		noinputmsg      = getByKey("Chat.PM.noinputmsg");
		playeNotFound   = getByKey("Chat.PM.playeNotFound");
		PMSearchNickMode = Main.config.getInt("Chat.PM.PMSearchNickMode", 0);
		
		broadcast       = getByKey("Chat.Broad.name");
		trybroadcastspy = getByKey("Chat.Broad.spy");
		broadcastspy    = getByKey("Chat.Broad.consoleSpy");
		broadcastList = Main.config.getStringList("Chat.Broad.list");
		
		mute            = getByKey("String.mute");
		muteHelp = Main.config.getString("mute.help");
		muteUnmute = Main.config.getString("mute.unmute");
		unknow = Main.config.getString("mute.unknow");
		muteMute = Main.config.getStringList("mue.mute");
		muteHelp2 = Main.config.getStringList("mue.help2");
		
		changechanel = getByKey("help.changechanel");
		baseHelp = Main.config.getStringList("help.base");
		List<String> _helpPrefix = Main.config.getStringList("help.prefix");
		for (String s : _helpPrefix) {
			helpPrefix.add(s
					.replace("$1", ChatMode.GLOBAL.getFirstLetter())
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

	}
	private static String getByKey(String key){
		return ChatListener.tCC(Main.config.getString(key,key));
	}
}
