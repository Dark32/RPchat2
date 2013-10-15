package ru.dark32.chat;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Chat implements Listener {
	// локальный чат
	private double				RangeMain;
	// шёпот
	private double				RangeWhispering;
	// крик
	private double				RangeShout;
	// Id вещи для вещания глобально
	@Deprecated
	private int					globalId;
	// метадата вещи для вещания глобально
	private int					globalSubId;
	// Id вещи для вещания мирово
	@Deprecated
	private int					worldId;
	// метадата вещи для вещания мирово
	private int					worldSubId;
	/* experemental--> */
	// Материал вещи для вещания глобально
	Material					globalMa;
	// Материал вещи для вещания мирово
	Material					worldMa;
	boolean						experemental	= false;
	/* <--experemental */
	// Бросок по умолчанию = 100;
	private int					randrolldef;
	// выводить ли префиксы всюду
	private boolean				Displayname;
	// шанс
	private int					defchanse;
	private int					minroll;
	protected static Pattern	nickForPM		= Pattern.compile("@(\\D[\\d\\w_]+)\\s(.+)");
	protected static Pattern	nickForMute		= Pattern.compile("%(\\D[\\d\\w_]+)\\s(.+)");
	// protected static Pattern _space = Pattern.compile("^\\s+"); // пробел?
	protected static Pattern	_number			= Pattern.compile("^\\*(\\d)$");				// число?
	private Random				rand			= new Random();

	private Main				plugin;

	public Chat(FileConfiguration config, Main pluging ){
		// радиус обычного чата
		this.RangeMain = config.getDouble("Range.main", this.RangeMain);
		// радиус шёпота
		this.RangeWhispering = config.getDouble("Range.whispering", this.RangeWhispering);
		// радиус крика
		this.RangeShout = config.getDouble("Range.shout", this.RangeShout);
		// вещь для вечания в глобальный чат
		this.globalId = config.getInt("Id.globalId", this.globalId);
		// метадата
		this.globalSubId = config.getInt("Id.globalSubId", this.globalSubId);
		// вещь для вещания в мировой чат
		this.worldId = config.getInt("Id.worldId", this.worldId);
		/* experemental--> */
		// вещь для вещания в глобальный чат, эксперементально
		this.globalMa = Material.getMaterial(config.getString("Id.globalMa"));
		// вещь для вещания в мировой чат, эксперементально
		this.worldMa = Material.getMaterial(config.getString("Id.worldMa"));
		// Эксперементальный режим
		this.experemental = config.getBoolean("Prefix", this.experemental);
		/* <--experemental */
		this.worldSubId = config.getInt("Id.worldSubId", this.worldSubId);// методата
		// бросок кубика
		this.randrolldef = config.getInt("randrolldef", this.randrolldef);
		this.Displayname = config.getBoolean("Prefix", this.Displayname);// префиксы
		this.defchanse = config.getInt("defchanse", this.defchanse);// шанс
		// минимальный бросок
		this.minroll = config.getInt("minroll", this.minroll);
		STR.init(config);
		this.plugin = pluging;
	}

	private static class STR {
		private static String	luck;
		private static String	unluck;
		private static String	roll;
		private static String	nei;
		private static String	noPerm;
		private static String	globalChat;
		private static String	worldChat;
		private static String	globalChatFormat;
		private static String	worldChatFormat;
		private static String	shoutChatFormat;
		private static String	whisperingChatFormat;
		private static String	localChatFormat;
		private static String	pmChatFormat;
		private static String	pmChatFormat2;
		private static String	rnd;
		private static String	playeNotFound;

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
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event ) {
		Util.setChatMode(event.getPlayer(), 0);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event ) {
		Player player = event.getPlayer();// получаем игрока, вызвавшего событие
		String message = STR.localChatFormat.replace("%sf", suffix(player))
				.replace("%pf", preffix(player)).replace("%p", "%1$s").replace("%msg", "%2$s");
		String chatMessage = event.getMessage();// сообщение
		int ranged = 1; // Тип чата
		/**
		 * =========================<br>
		 * 3 - глобальный<br>
		 * 2 - мировой<br>
		 * 1 - флаг дистанции<br>
		 * <br>
		 * @ - ЛС<br>
		 * $ - глобальный чат<br>
		 * > - мировой чат<br>
		 * ! - крик<br>
		 * # - шепот<br>
		 * ======================
		 **/
		ItemStack inHand = player.getItemInHand();// вещь в руках
		double range = RangeMain;// локальный чат, радиус по умолчанию
		int mode = Util.getChatMode(player); // уменьшаем число вызовов
		boolean isLocal = chatMessage.startsWith(ChatMode.LOCAL.getStartChar())
				&& chatMessage.length() > 1;
		boolean isGlobal = !isLocal && chatMessage.startsWith(ChatMode.GLOBAL.getStartChar())
				&& chatMessage.length() > 1;
		boolean isWorld = !isLocal && chatMessage.startsWith(ChatMode.WORLD.getStartChar())
				&& chatMessage.length() > 1;
		boolean isShout = !isLocal && chatMessage.startsWith(ChatMode.SHOUT.getStartChar())
				&& chatMessage.length() > 1;
		boolean isWhisper = !isLocal && chatMessage.startsWith(ChatMode.WHISPER.getStartChar())
				&& chatMessage.length() > 1;
		boolean isPm = chatMessage.startsWith(ChatMode.PM.getStartChar())
				&& chatMessage.length() > 1;
		boolean isAny = isGlobal || isWorld || isShout || isWhisper;
		boolean isGlobalMode = !isLocal && mode == ChatMode.GLOBAL.getModeId();
		boolean isWorldMode = !isLocal && !isGlobal && mode == ChatMode.WORLD.getModeId();
		boolean isShoutMode = !isLocal && !isGlobal && !isWorld
				&& mode == ChatMode.SHOUT.getModeId();
		boolean isWhisperMode = !isLocal && !isGlobal && !isWorld && !isShout
				&& mode == ChatMode.WHISPER.getModeId();
		boolean isLocalMode = mode == ChatMode.LOCAL.getModeId();
		boolean isGlobalChatItemInHantd = inHand != null && inHand.getTypeId() == globalId
				&& inHand.getDurability() == globalSubId;
		boolean isWorldChatItemInHantd = inHand != null && inHand.getTypeId() == worldId
				&& inHand.getDurability() == worldSubId;
		if (experemental) {
			isGlobalChatItemInHantd = inHand != null && inHand.getType() == globalMa
					&& inHand.getDurability() == globalSubId;
			isWorldChatItemInHantd = inHand != null && inHand.getType() == globalMa
					&& inHand.getDurability() == worldSubId;
		}
		// Глобальный
		if (isGlobal || isGlobalMode) {
			if (Util.hasPermission(player, "mcnw.global")) {
				message = STR.globalChatFormat.replace("%sf", suffix(player))
						.replace("%pf", preffix(player)).replace("%p", "%1$s")
						.replace("%msg", "%2$s");
				range = RangeWhispering;
				if (Util.hasPermission(player, "mcnw.global.no_item")) {
					ranged = 3; // флаг глобально, межмирового чата
				} else if (isGlobalChatItemInHantd) {
					ranged = 3; // флаг глобально, межмирового чата
					loseitem(player);
				} else {
					player.sendMessage(STR.nei);
					event.setCancelled(true);
				}
			} else {
				player.sendMessage(STR.noPerm.replace("$1", STR.globalChat));
				event.setCancelled(true);
			}
		}

		// Мировой
		if (isWorld || isWorldMode) {
			if (Util.hasPermission(player, "mcnw.world")) {
				message = STR.worldChatFormat.replace("%sf", suffix(player))
						.replace("%pf", preffix(player)).replace("%p", "%1$s")
						.replace("%msg", "%2$s");

				range = RangeWhispering;
				if (Util.hasPermission(player, "mcnw.world.no_item")) {
					ranged = 2;// флаг мирового чата
				} else if (isWorldChatItemInHantd) {
					ranged = 2;// флаг мирового чата
					loseitem(player);
				} else {
					player.sendMessage(STR.nei);
					event.setCancelled(true);
				}
			} else {
				player.sendMessage(STR.noPerm.replace("$1", STR.worldChat));
				event.setCancelled(true);
			}
		}
		// Крик
		if (isShout || isShoutMode) {
			ranged = 1; // флаг дистанции
			range = RangeShout;// 1000;
			message = STR.shoutChatFormat.replace("%sf", suffix(player))
					.replace("%pf", preffix(player)).replace("%p", "%1$s").replace("%msg", "%2$s");

		}
		// Шепот
		if (isWhisper || isWhisperMode) {
			ranged = 1;// флаг дистанции
			range = RangeWhispering;// 10;
			message = STR.whisperingChatFormat.replace("%sf", suffix(player))
					.replace("%pf", preffix(player)).replace("%p", "%1$s").replace("%msg", "%2$s");

		}
		// Личка
		if (isPm) {

			Matcher m = nickForPM.matcher(chatMessage);
			if (m.find()) {
				ranged = -1;// флаг особого чата
				if (Bukkit.getServer().getPlayer(m.group(1)) != null) {
					Player recipient = Bukkit.getServer().getPlayer(m.group(1));
					if (!recipient.equals(player)) {
						recipient.sendMessage(STR.pmChatFormat.replace("%sf", suffix(player))
								.replace("%pf", preffix(player)).replace("%p", player.getName())
								.replace("%r", recipient.getName()).replace(" %msg", m.group(2)));
					}
					player.sendMessage(STR.pmChatFormat2.replace("%sf", suffix(player))
							.replace("%pf", preffix(player)).replace("%p", player.getName())
							.replace("%r", recipient.getName()).replace(" %msg", m.group(2)));
					if (!Util.hasPermission(player, "mcnw.nospy")) {
						getPMRecipientsSpy(player, recipient, m.group(2));
					}
					Bukkit.getConsoleSender().sendMessage(
							Chat.tCC("&7spy@" + player.getName() + "->" + recipient.getName()
									+ ": " + m.group(2)));
				} else {
					player.sendMessage(STR.playeNotFound.replace("$1", m.group(2)));
				}
				event.setCancelled(true);
			}
		}

		// Действие с вероятностью
		if (chatMessage.startsWith("*") && chatMessage.length() > 1) {
			ranged = 1;
			range = RangeMain;
			Matcher m = _number.matcher(chatMessage);
			int i;
			if (m.find()) {
				String num = m.group(1);
				i = num.length() < 5 ? Integer.parseInt(num) : 9999;
				i = i > minroll ? i : randrolldef;
				int j = rand.nextInt(i) + 1;
				message = STR.rnd.replace("%sf", suffix(player)).replace("%pf", preffix(player))
						.replace("%p", player.getName()).replace("$1", String.valueOf(j))
						.replace("$2", String.valueOf(i));

			} else {
				chatMessage = Chat.tCC("&5" + chatMessage.substring(1));
				int chance = rand.nextInt(100);
				message = Chat.tCC(
						STR.roll.replace("%sf", suffix(player)).replace("%pf", preffix(player))
								.replace("%p", player.getName()).replace("%msg", chatMessage))
						.replace("$1", (chance > defchanse) ? STR.luck : STR.unluck);
			}

		}

		if (ranged > 0 && chatMessage.length() > 1 && (isAny) && (range != RangeMain)) {
			chatMessage = chatMessage.substring(1).trim();
		}

		if (isLocal) {
			chatMessage = chatMessage.substring(1).trim();
		}
		if (chatMessage.startsWith("?") && chatMessage.length() == 1) {
			player.sendMessage(Chat.tCC("&b============================================="));
			player.sendMessage(Chat.tCC("&6" + Main.version));
			player.sendMessage(Chat.tCC("&6 Авторы: ufatos, dark32"));
			player.sendMessage(Chat.tCC("&6 CC-BY-NC-ND"));
			player.sendMessage(Chat.tCC("&6 http://bit.ly/12Q8z4q"));
			player.sendMessage(Chat.tCC("&6 ?/. для справки режимов чата"));
			player.sendMessage(Chat.tCC("&6 ?/? для справки по переключению режимов чата"));
			player.sendMessage(Chat.tCC("&b ============================================="));
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/.") && chatMessage.length() == 3) {
			player.sendMessage(Chat.tCC("&b============================================="));
			player.sendMessage(Chat.tCC("&6" + ChatMode.GLOBAL.getStartChar()
					+ " для глобального чата"));
			player.sendMessage(Chat.tCC("&6" + ChatMode.WORLD.getStartChar() + " для мирового чата"));
			player.sendMessage(Chat.tCC("&6" + ChatMode.SHOUT.getStartChar() + " для крика"));
			player.sendMessage(Chat.tCC("&6" + ChatMode.WHISPER.getStartChar() + " для шепота"));
			player.sendMessage(Chat.tCC("&6" + ChatMode.PM.getStartChar()
					+ "<имя> сообщени для личного чата"));
			player.sendMessage(Chat
					.tCC("&6*<действие> для вероятного действия или *<целое число больше "
							+ minroll + "> для выброса случайного числа"));
			player.sendMessage(Chat.tCC("&b============================================="));
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/?") && chatMessage.length() == 3) {
			player.sendMessage(Chat.tCC("&b============================================="));
			player.sendMessage(Chat.tCC("&6?/g - для выбора глобального режима"));
			player.sendMessage(Chat.tCC("&6?/w - для выбора мирового мирового"));
			player.sendMessage(Chat.tCC("&6?/s - для выбора режима крика"));
			player.sendMessage(Chat.tCC("&6?/v - для выбора режима шепота"));
			player.sendMessage(Chat.tCC("&6?/l - для выбора локального режима"));
			player.sendMessage(Chat.tCC("&6?/? - для вызоа этой справки"));
			if (Util.hasPermission(player, "mcnw.mute.help")) {
				player.sendMessage(Chat.tCC("&6?/m - для вызоа справки по молчанке"));
			}
			player.sendMessage(Chat.tCC("&b============================================="));
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/m") && chatMessage.length() == 3) {
			player.sendMessage(Chat.tCC("&b============================================="));
			if (Util.hasPermission(player, "mcnw.mute.mute")) {
				player.sendMessage(Chat
						.tCC("&b%nick [chanel=g|w|s|v|l|p|a] [time=sec] &6- для молчанки"));
				player.sendMessage(Chat.tCC("&bg &6- Глобалный  &bw&6 - мировой"));
				player.sendMessage(Chat.tCC("&bs &6- крик       &bv&6 - шёпот"));
				player.sendMessage(Chat.tCC("&bl &6- локальный  &bp&6 - личные сообщения"));
				player.sendMessage(Chat.tCC("&ba &6- все"));
				if (Util.hasPermission(player, "mcnw.mute.unmute")) {
					player.sendMessage(Chat
							.tCC("&6 время равное 0 - для принудительносго снятия молчанки"));
				}
			}
			if (Util.hasPermission(player, "mcnw.mute.help")) {
				player.sendMessage(Chat.tCC("&6?/m - для вызоа справки по молчанке"));
			}
			player.sendMessage(Chat.tCC("&b============================================="));
			event.setCancelled(true);
		}
		// молчанка
		if (chatMessage.startsWith("%") && chatMessage.length() > 1) {
			Matcher m = nickForMute.matcher(chatMessage);
			if (m.find()) {
				plugin.getBanStorage().mute(m.group(1), m.group(2), player);
				event.setCancelled(true);
			}
		}
		// режимы чата
		if (chatMessage.startsWith("?/g") && chatMessage.length() == 3) {
			Util.setChatMode(player, ChatMode.GLOBAL.getModeId());
			player.sendMessage(Chat.tCC("&6Режим изменён на глобальный"));
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/w") && chatMessage.length() == 3) {
			Util.setChatMode(player, ChatMode.WORLD.getModeId());
			player.sendMessage(Chat.tCC("&6Режим изменён на мировой"));
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/s") && chatMessage.length() == 3) {
			Util.setChatMode(player, ChatMode.SHOUT.getModeId());
			player.sendMessage(Chat.tCC("&6 Режим изменён на крик"));
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/v") && chatMessage.length() == 3) {
			Util.setChatMode(player, ChatMode.WHISPER.getModeId());
			player.sendMessage(Chat.tCC("&6 Режим изменён на шёпот"));
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/l") && chatMessage.length() == 3) {
			Util.setChatMode(player, ChatMode.LOCAL.getModeId());
			player.sendMessage(Chat.tCC("&6 Режим изменён на локальный"));
			event.setCancelled(true);
		}
		switch (ranged) {
			case 0:
				player.sendMessage("Тебя слышат все!!!");/* Не используется */
			case 1: {// если стоит флаг дистанции
				event.getRecipients().clear();
				event.getRecipients().addAll(this.getLocalRecipients(player, range));
				break;
			}
			case 2: {// если стоит флаг внутримирового чата
				event.getRecipients().clear();
				event.getRecipients().addAll(this.getWorldRecipients(player, message));
				break;
			}
			case 3: {// если стоит флаг межмирового чата
				break;
			}
		}
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

}
