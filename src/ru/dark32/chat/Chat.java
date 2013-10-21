package ru.dark32.chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Chat implements Listener {
	// локальный чат
	private double					RangeLocal;
	// шёпот
	private double					RangeWhispering;
	// крик
	private double					RangeShout;
	// Id вещи для вещания глобально
	@Deprecated
	private int						globalId;
	// метадата вещи для вещания глобально
	private int						globalSubId;
	// Id вещи для вещания мирово
	@Deprecated
	private int						worldId;
	// метадата вещи для вещания мирово
	private int						worldSubId;
	/* experemental--> */
	// Материал вещи для вещания глобально
	Material						globalMa;
	// Материал вещи для вещания мирово
	Material						worldMa;
	boolean							experemental	= false;
	/* <--experemental */
	private int						randrolldef;
	private int						PMSearchNickMode;
	private int						defchanse;
	private static int				minroll;
	protected final static Pattern	nickForMute		= Pattern.compile("%([\\d\\w_]+)\\s(.+)");
	private Random					rand			= new Random();
	private Main					plugin;

	public Chat(FileConfiguration config, Main pluging ){
		// радиус обычного чата
		this.RangeLocal = config.getDouble("Range.main", 250d);
		// радиус шёпота
		this.RangeWhispering = config.getDouble("Range.whispering", 10d);
		// радиус крика
		this.RangeShout = config.getDouble("Range.shout", 500d);
		// вещь для вечания в глобальный чат
		this.globalId = config.getInt("Id.globalId", 260);
		// метадата
		this.globalSubId = config.getInt("Id.globalSubId", 0);
		// вещь для вещания в мировой чат
		this.worldId = config.getInt("Id.worldId", 264);
		/* experemental--> */
		// вещь для вещания в глобальный чат, эксперементально
		this.globalMa = Material.getMaterial(config.getString("Id.globalMa"));
		// вещь для вещания в мировой чат, эксперементально
		this.worldMa = Material.getMaterial(config.getString("Id.worldMa", "APPLE"));
		// Эксперементальный режим
		this.experemental = config.getBoolean("Prefix", this.experemental);
		/* <--experemental */
		this.worldSubId = config.getInt("Id.worldSubId", 0);// методата
		// бросок кубика
		this.randrolldef = config.getInt("randrolldef", 5);
		this.defchanse = config.getInt("defchanse", 50);// шанс
		// минимальный бросок
		this.minroll = config.getInt("minroll", 5);
		this.PMSearchNickMode = config.getInt("PMSearchNickMode", 0);
		STR.init(config);
		this.plugin = pluging;
	}

	private static class STR {
		private static String		luck;
		private static String		unluck;
		private static String		roll;
		private static String		nei;
		private static String		noPerm;
		private static String		globalChat;
		private static String		worldChat;
		private static String		globalChatFormat;
		private static String		worldChatFormat;
		private static String		shoutChatFormat;
		private static String		whisperingChatFormat;
		private static String		localChatFormat;
		private static String		pmChatFormat;
		private static String		pmChatFormat2;
		private static String		rnd;
		private static String		playeNotFound;
		private static String		mute;
		private static List<String>	broadcastList;
		private static String		broadcast;
		private static String		noinputmsg;
		private static String		trybroadcastspy;
		private static String		broadcastspy;
		private static List<String>	baseHelp;
		private static String		changechanel;
		private static String		shoutChat;
		private static String		localChat;
		private static String		whisperingChat;
		private static List<String>	helpPrefix	= new ArrayList<String>();
		private static List<String>	chanelswitch;

		private static void init(MemorySection config ) {
			luck = Chat.tCC(config.getString("String.luck"));
			unluck = Chat.tCC(config.getString("String.unluck"));
			roll = Chat.tCC(config.getString("String.roll"));
			rnd = Chat.tCC(config.getString("String.rnd"));
			nei = Chat.tCC(config.getString("String.nei"));
			noPerm = Chat.tCC(config.getString("String.noPerm"));
			globalChat = Chat.tCC(config.getString("String.globalChat"));
			worldChat = Chat.tCC(config.getString("String.worldChat"));
			globalChatFormat = Chat.tCC(config.getString("String.globalChatFormat"));
			worldChatFormat = Chat.tCC(config.getString("String.worldChatFormat"));
			shoutChatFormat = Chat.tCC(config.getString("String.shoutChatFormat"));
			whisperingChatFormat = Chat.tCC(config.getString("String.whisperingChatFormat"));
			localChatFormat = Chat.tCC(config.getString("String.localChatFormat"));
			pmChatFormat = Chat.tCC(config.getString("String.localChatFormat"));
			pmChatFormat2 = Chat.tCC(config.getString("String.pmChatFormat2"));
			playeNotFound = Chat.tCC(config.getString("String.playeNotFound"));
			mute = Chat.tCC(config.getString("String.mute"));
			broadcast = Chat.tCC(config.getString("String.broadcast"));
			noinputmsg = Chat.tCC(config.getString("String.noinputmsg"));
			trybroadcastspy = Chat.tCC(config.getString("String.trybroadcastspy"));
			broadcastspy = Chat.tCC(config.getString("String.broadcastspy"));
			changechanel = Chat.tCC(config.getString("help.changechanel"));
			shoutChat = Chat.tCC(config.getString("String.shoutChat"));
			localChat = Chat.tCC(config.getString("String.localChat"));
			whisperingChat = Chat.tCC(config.getString("String.whisperingChat"));

			broadcastList = config.getStringList("String.broadcastList");
			baseHelp = config.getStringList("help.baseHelp");
			List<String> _helpPrefix = config.getStringList("help.prefix");
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
			chanelswitch = config.getStringList("help.chanelswitch");
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event ) {
		Player player = event.getPlayer();// получаем игрока, вызвавшего событие
		String message = STR.localChatFormat;
		String chatMessage = event.getMessage();// сообщение
		char firstChar = chatMessage.charAt(0);
		char secondChar = chatMessage.length() > 1 ? chatMessage.charAt(1) : 0;
		char thirdChar = chatMessage.length() > 2 ? chatMessage.charAt(2) : 0;
		ItemStack inHand = player.getItemInHand();// вещь в руках
		double range = RangeLocal;// локальный чат, радиус по умолчанию
		int mode = Util.getChatMode(player.getName());
		boolean isMoreThenOne = chatMessage.length() > 1;
		boolean isGlobalChatItemInHand = inHand != null && inHand.getDurability() == globalSubId;
		boolean isWorldChatItemInHand = inHand != null && inHand.getDurability() == worldSubId;

		if (experemental) {
			isGlobalChatItemInHand &= inHand != null && inHand.getType() == globalMa;
			isWorldChatItemInHand &= inHand != null && inHand.getType() == worldMa;
		} else {
			isGlobalChatItemInHand &= inHand != null && inHand.getTypeId() == globalId;
			isWorldChatItemInHand &= inHand != null && inHand.getTypeId() == worldId;
		}
		if (isMoreThenOne) {
			if (firstChar == ChatMode.GLOBAL.getFirstChar()) {
				mode = ChatMode.GLOBAL.getModeId();
				chatMessage = chatMessage.substring(1).trim();
			} else if (firstChar == ChatMode.WORLD.getFirstChar()) {
				mode = ChatMode.WORLD.getModeId();
				chatMessage = chatMessage.substring(1).trim();
			} else if (firstChar == ChatMode.SHOUT.getFirstChar()) {
				mode = ChatMode.SHOUT.getModeId();
				chatMessage = chatMessage.substring(1).trim();
			} else if (firstChar == ChatMode.LOCAL.getFirstChar()) {
				mode = ChatMode.LOCAL.getModeId();
				chatMessage = chatMessage.substring(1).trim();
			} else if (firstChar == ChatMode.WHISPER.getFirstChar()) {
				mode = ChatMode.WHISPER.getModeId();
				chatMessage = chatMessage.substring(1).trim();
			} else if (firstChar == ChatMode.PM.getFirstChar()) {
				mode = ChatMode.PM.getModeId();
				chatMessage = chatMessage.substring(1).trim();
			} else if (firstChar == ChatMode.CHANCE.getFirstChar()) {
				mode = ChatMode.CHANCE.getModeId();
				chatMessage = chatMessage.substring(1).trim();
			} else if (firstChar == ChatMode.BROADCAST.getFirstChar()) {
				mode = ChatMode.BROADCAST.getModeId();
				chatMessage = chatMessage.substring(1).trim();
			}
		}

		if (mode == ChatMode.GLOBAL.getModeId()) {// Глобальный
			if (ifMute(player, mode)) {
				event.setCancelled(true);
				return;
			} else if (Util.hasPermission(player, "mcnw.global")) {
				message = STR.globalChatFormat;
				if (!Util.hasPermission(player, "mcnw.global.no_item")) {
					if (isGlobalChatItemInHand) {
						loseitem(player);
					} else {
						player.sendMessage(STR.nei);
						event.setCancelled(true);
						return;
					}
				}
			} else {
				player.sendMessage(STR.noPerm.replace("$1", STR.globalChat));
				event.setCancelled(true);
				return;
			}
		} else if (mode == ChatMode.WORLD.getModeId()) {// Мировой
			if (ifMute(player, mode)) {
				event.setCancelled(true);
			}
			if (Util.hasPermission(player, "mcnw.world")) {
				message = STR.worldChatFormat;
				if (!Util.hasPermission(player, "mcnw.world.no_item")) {
					if (isWorldChatItemInHand) {
						loseitem(player);
					} else {
						player.sendMessage(STR.nei);
						event.setCancelled(true);
						return;
					}
				}
			} else {
				player.sendMessage(STR.noPerm.replace("$1", STR.worldChat));
				event.setCancelled(true);
				return;
			}
		} else if (mode == ChatMode.SHOUT.getModeId()) {// Крик
			if (ifMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = RangeShout;
			message = STR.shoutChatFormat;
		} else if (mode == ChatMode.LOCAL.getModeId()) {// локаль
			if (ifMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = RangeLocal;
			message = STR.localChatFormat;
		} else if (mode == ChatMode.WHISPER.getModeId()) {// шепот
			if (ifMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = RangeWhispering;
			message = STR.whisperingChatFormat;
		} else if (mode == ChatMode.PM.getModeId()) {// PM
			if (ifMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			int _ind = chatMessage.indexOf(" ");
			if (_ind == -1) {
				player.sendMessage(STR.noinputmsg);
				event.setCancelled(true);
				return;
			}
			String pmNick = chatMessage.substring(0, _ind);
			String _msg = chatMessage.substring(_ind + 1, chatMessage.length());
			Player recipient = getPlayerByName(pmNick);
			if (recipient == null) {
				player.sendMessage(STR.playeNotFound.replace("$1", pmNick));
				event.setCancelled(true);
				return;
			}
			String pmMSG1 = STR.pmChatFormat.replace("%sf", suffix(player))
					.replace("%pf", preffix(player)).replace("%p", player.getName())
					.replace("%r", recipient.getName()).replace(" %msg", _msg);
			String pmMSG2 = STR.pmChatFormat2.replace("%sf", suffix(player))
					.replace("%pf", preffix(player)).replace("%p", player.getName())
					.replace("%r", recipient.getName()).replace(" %msg", _msg);
			String pmCnsoleSpy = Chat.tCC("&7spy@" + player.getName() + "->" + recipient.getName()
					+ ": " + _msg);
			if (!recipient.equals(player)) {
				recipient.sendMessage(pmMSG1);
			}
			player.sendMessage(pmMSG2);
			if (!Util.hasPermission(player, "mcnw.nospy")) {
				getPMRecipientsSpy(player, recipient, _msg);
			}
			Bukkit.getConsoleSender().sendMessage(pmCnsoleSpy);
			event.setCancelled(true);
			return;
		} else if (mode == ChatMode.CHANCE.getModeId()) {
			// действия с вероятностью
			if (ifMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = RangeLocal;
			int iChance;
			if (Util.isInteger(chatMessage)) {
				iChance = chatMessage.length() < 5 ? Integer.parseInt(chatMessage) : 9999;
				iChance = iChance > minroll ? iChance : randrolldef;
				int iRoll = rand.nextInt(iChance) + 1;
				message = STR.rnd.replace("$1", String.valueOf(iRoll)).replace(
						"$2",
							String.valueOf(iChance));

			} else {
				chatMessage = Chat.tCC("&5" + chatMessage);
				int chance = rand.nextInt(100);
				message = Chat.tCC(STR.roll.replace("$1", (chance > defchanse) ? STR.luck
						: STR.unluck));
			}

		} else if (mode == ChatMode.BROADCAST.getModeId()) {// Броадкаст
			if (ifMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			if (Util.hasPermission(player, "mcnw.broadcast")) {
				message = "%2$s";
				Bukkit.getConsoleSender().sendMessage(
						STR.broadcastspy.replace("%p", player.getName()));
			} else {
				player.sendMessage(STR.noPerm.replace("$1", STR.broadcast));
				Bukkit.getConsoleSender().sendMessage(
						STR.trybroadcastspy.replace("%p", player.getName()));
				event.setCancelled(true);
				return;
			}
		}

		if (firstChar == '?' && chatMessage.length() == 1) {
			Chat.getHelp(player);
			event.setCancelled(true);
			return;
		}
		// молчанка
		if (firstChar == '%' && isMoreThenOne) {
			Matcher m = nickForMute.matcher(chatMessage);
			if (m.find()) {
				Main.getBanStorage().mute(m.group(1), m.group(2), player);
				event.setCancelled(true);
				return;
			}
		}
		if (firstChar == '?' && secondChar == '/' && chatMessage.length() == 3) {
			getAnotherHelp(player, thirdChar);
			event.setCancelled(true);
			return;
		}
		if (mode == ChatMode.SHOUT.getModeId() || mode == ChatMode.WHISPER.getModeId()
				|| mode == ChatMode.LOCAL.getModeId()) {
			event.getRecipients().clear();
			event.getRecipients().addAll(this.getLocalRecipients(player, range));
		} else if (mode == ChatMode.WORLD.getModeId()) {
			event.getRecipients().clear();
			event.getRecipients().addAll(this.getWorldRecipients(player, message));
		} else if (mode == ChatMode.BROADCAST.getModeId()) {
			chatMessage = Chat.tCC(chatMessage);
		}
		message = message.replace("%sf", suffix(player)).replace("%pf", preffix(player))
				.replace("%p", "%1$s").replace("%msg", "%2$s");
		event.setFormat(message);
		event.setMessage(chatMessage);
	}

	// теряем вещь
	private void loseitem(Player player ) {
		ItemStack inHand = player.getItemInHand();
		int AmoutHand = inHand.getAmount() - 1;
		ItemStack inHandrem = null;
		if (experemental) {
			inHandrem = new ItemStack(inHand.getType(), inHand.getAmount() - 1);
		} else {
			inHandrem = new ItemStack(inHand.getTypeId(), inHand.getAmount() - 1);
		}
		if (AmoutHand <= 0) {
			inHandrem = null;
		}
		player.setItemInHand(inHandrem);
		return;
	}

	// Лакальный чат. Список кому отправлять
	protected List<Player> getLocalRecipients(Player sender, double range ) {
		Location playerLocation = sender.getLocation();
		List<Player> recipients = new LinkedList<Player>();
		double squaredDistance = Math.pow(range, 2);
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if (recipient.getWorld().equals(sender.getWorld())
					&& playerLocation.distanceSquared(recipient.getLocation()) < squaredDistance) {
				recipients.add(recipient);
			} else if (Util.hasPermission(recipient, "mcnw.spy")) {
				recipients.add(recipient);
			} else {
				continue;
			}
		}
		return recipients;
	}

	// Мировой чат. Список тех кому отправлять
	protected List<Player> getWorldRecipients(Player sender, String message ) {
		List<Player> recipients = new LinkedList<Player>();
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if (!recipient.getWorld().equals(sender.getWorld())) {
				continue;
			}
			recipients.add(recipient);
		}
		return recipients;
	}

	// Прослушка ЛС
	protected void getPMRecipientsSpy(Player sender, Player target, String msg ) {
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if (Util.hasPermission(recipient, "mcnw.pmspy") && !recipient.equals(target)) {
				recipient.sendMessage(Chat.tCC("&7spy@" + sender.getName() + "->"
						+ target.getName() + ": " + msg));
			}
		}
	}

	private static String tCC(String string ) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	private String preffix(Player p ) {
		if (!Util.usePEX) {
			return "";
		}
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(p);
		if (user == null) {
			return "";
		}

		return user.getPrefix();
	}

	private String suffix(Player p ) {
		if (!Util.usePEX) {
			return "";
		}
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(p);
		if (user == null) {
			return "";
		}
		return user.getSuffix();
	}

	private Boolean ifMute(Player player, int i ) {
		if (Main.getBanStorage().isMuted(player.getName(), i)) {
			player.sendMessage(STR.mute.replace(
					"$1",
						"" + Main.getBanStorage().getTimeMute(player.getName(), i)));
			return true;
		}
		return false;
	}

	public static void getHelp(CommandSender player ) {
		List<String> msg = new ArrayList<String>();
		msg.add("&b=============================================");
		msg.add("&6" + Main.version);
		msg.add("&6Autors: ufatos, dark32");
		msg.add("&6License: CC-BY-NC-ND");
		msg.add("&6Linck: http://bit.ly/12Q8z4q");
		msg.addAll(STR.baseHelp);
		msg.add("&b=============================================");
		for (String s : msg) {
			player.sendMessage(Chat.tCC(s));
		}
	}

	private void getAnotherHelp(Player player, char thirdChar ) {
		List<String> msg = new ArrayList<String>();
		msg.add("&b=============================================");
		switch (thirdChar) {
			case ('g'): {
				Util.setChatMode(player.getName(), ChatMode.GLOBAL.getModeId());
				msg.add(STR.changechanel.replace("$1", STR.globalChat));
				break;
			}
			case ('w'): {
				Util.setChatMode(player.getName(), ChatMode.WORLD.getModeId());
				msg.add(STR.changechanel.replace("$1", STR.worldChat));
				break;
			}
			case ('s'): {
				Util.setChatMode(player.getName(), ChatMode.SHOUT.getModeId());
				msg.add(STR.changechanel.replace("$1", STR.shoutChat));
				break;
			}
			case ('l'): {
				Util.setChatMode(player.getName(), ChatMode.LOCAL.getModeId());
				msg.add(STR.changechanel.replace("$1", STR.localChat));
				break;
			}
			case ('v'): {
				Util.setChatMode(player.getName(), ChatMode.WHISPER.getModeId());
				msg.add(STR.changechanel.replace("$1", STR.whisperingChat));
				break;
			}
			case '.': {
				msg.addAll(STR.helpPrefix);
				break;
			}
			case '?': {
				msg.addAll(STR.chanelswitch);
				if (Util.hasPermission(player, "mcnw.mute.help")) {
					msg.add("&6?/m - для вызоа справки по молчанке");
				}
				break;
			}
			case 'm': {
				if (Util.hasPermission(player, "mcnw.mute.mute")) {
					msg.add("&b%<nick> <chanel=g|w|s|v|l|p|a> <time=sec> <reason> &6- для молчанки");
					msg.add("&bg &6- Глобалный  &bw&6 - мировой");
					msg.add("&bs &6- крик       &bv&6 - шёпот");
					msg.add("&bl &6- локальный  &bp&6 - личные сообщения");
					msg.add("&ba &6- все");
				}
				msg.add("&b%nick s[ee] &6- для просмотра молчанки");
				if (Util.hasPermission(player, "mcnw.mute.unmute")) {
					msg.add("&6 время равное 0 - для принудительносго снятия молчанки");
				}
				if (Util.hasPermission(player, "mcnw.mute.help")) {
					msg.add("&6?/m - для вызоа справки по молчанке");
				}
				break;
			}
			default: {
				msg.add("&bНеизвестнй параметр " + thirdChar);
				break;
			}
		}
		msg.add("&b=============================================");
		for (String s : msg) {
			player.sendMessage(Chat.tCC(s));
		}
	}

	private Player getPlayerByName(String name ) {
		switch (PMSearchNickMode) {
			case -1:
				return Bukkit.getServer().getPlayerExact(name);
			case 0:
				return Bukkit.getServer().getPlayer(name);
			case 1:
				return Util.getPlayerSoft(name);
			default:
				return Bukkit.getServer().getPlayer(name);
		}
	}

	@EventHandler
	public void tabComplete(PlayerChatTabCompleteEvent e ) {
		e.getTabCompletions().clear();
		String chatMessage = e.getChatMessage();
		Collection<String> completions = e.getTabCompletions();
		char firstChar = chatMessage.charAt(0);
		if (firstChar == ChatMode.PM.getFirstChar()) {
			String _nick = chatMessage.length() > 1 ? chatMessage.substring(1) : "";
			String _name = "";
			for (Player player : Bukkit.getOnlinePlayers()) {
				_name = player.getName();
				if ((_nick.length() > 0 && _name.startsWith(_nick)) || _nick.isEmpty()) {
					completions.add(ChatMode.PM.getFirstLetter() + _name);
				}
			}
		} else if (firstChar == '%') {
			String _nick = chatMessage.length() > 1 ? chatMessage.substring(1) : "";
			String _name = "";
			for (Player player : Bukkit.getOnlinePlayers()) {
				_name = player.getName();
				if ((_nick.length() > 0 && _name.startsWith(_nick)) || _nick.isEmpty()) {
					completions.add('%' + _name);
				}
			}
		} else if (firstChar == ChatMode.BROADCAST.getFirstChar()) {
			for (String s : STR.broadcastList) {
				String broad = chatMessage.length() > 1 ? chatMessage.substring(1) : "";
				if ((broad.length() > 0 && s.startsWith(broad)) || broad.isEmpty()) {
					completions.add(ChatMode.BROADCAST.getFirstLetter() + s);
				}
			}
		} else {
			return;
		}
	}
}
