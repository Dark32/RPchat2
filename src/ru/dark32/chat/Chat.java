package ru.dark32.chat;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;
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
	boolean						experemental				= false;
	/* <--experemental */
	// Бросок по умолчанию = 100;
	private int					randrolldef;
	// выводить ли префиксы всюду
	private boolean				Displayname;
	// шанс
	private int					defchanse;
	private int					minroll;
	protected static Pattern	chatColorPattern			= Pattern.compile("(?i)&([0-9A-F])");			// Цвет
	protected static Pattern	chatMagicPattern			= Pattern.compile("(?i)&([K])");				// магия
	protected static Pattern	chatBoldPattern				= Pattern.compile("(?i)&([L])");				// жирный
	protected static Pattern	chatStrikethroughPattern	= Pattern.compile("(?i)&([M])");				// зачёркнутый
	protected static Pattern	chatUnderlinePattern		= Pattern.compile("(?i)&([N])");				// подчёркнутый
	protected static Pattern	chatItalicPattern			= Pattern.compile("(?i)&([O])");				// косой
	protected static Pattern	chatResetPattern			= Pattern.compile("(?i)&([R])");				// сброс
	// извлекаем ник
	protected static Pattern	nickForPM					= Pattern
																	.compile("[@](\\D[\\d\\w_]+)\\s(.+)");
	protected static Pattern	nickForMute					= Pattern
																	.compile("%(\\D[\\d\\w_]+)\\s(.+)");
	protected static Pattern	_space						= Pattern.compile("^\\s+");					// пробел?
	protected static Pattern	_number						= Pattern.compile("^\\*(\\d+)$");				// число?
	private Random				rand						= new Random();
	private String				luck						= ChatColor.GREEN + "(удачно)"
																	+ ChatColor.LIGHT_PURPLE;
	private String				unluck						= ChatColor.RED + "(не удачно)"
																	+ ChatColor.LIGHT_PURPLE;
	private static Main			plugin;

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
		this.plugin = pluging;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event ) {
		updateDisplayName(event.getPlayer());// обновляем префиксы
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event ) {
		Player player = event.getPlayer();// получаем игрока, вызвавшего событие
		String message = "%1$s: %2$s";// формат сообщения
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
				message = "%1$s всем: " + ChatMode.GLOBAL.getColor() + "%2$s";
				range = RangeWhispering;
				if (Util.hasPermission(player, "mcnw.global.no_item")) {
					ranged = 3; // флаг глобально, межмирового чата
				} else if (isGlobalChatItemInHantd) {
					ranged = 3; // флаг глобально, межмирового чата
					loseitem(player);
				} else {
					player.sendMessage(ChatColor.GRAY + "У вас нет нужного предмета");
					event.setCancelled(true);
				}
			} else {
				player.sendMessage(ChatColor.GRAY + "У вас нет прав писать в глобальный чат");
				event.setCancelled(true);
			}
		}

		// Мировой
		if (isWorld || isWorldMode) {
			if (Util.hasPermission(player, "mcnw.world")) {
				message = "%1$s в мир: " + ChatMode.WORLD.getColor() + "%2$s";
				range = RangeWhispering;
				if (Util.hasPermission(player, "mcnw.world.no_item")) {
					ranged = 2;// флаг мирового чата
				} else if (isWorldChatItemInHantd) {
					ranged = 2;// флаг мирового чата
					loseitem(player);
				} else {
					player.sendMessage(ChatColor.GRAY + "У вас нет нужного предмета");
					event.setCancelled(true);
				}
			} else {
				player.sendMessage(ChatColor.GRAY + "У вас нет прав писать в мир");
				event.setCancelled(true);
			}
		}
		// Крик
		if (isShout || isShoutMode) {
			ranged = 1; // флаг дистанции
			range = RangeShout;// 1000;
			message = "%1$s кричит: " + ChatMode.SHOUT.getColor() + "%2$s";

		}
		// Шепот
		if (isWhisper || isWhisperMode) {
			ranged = 1;// флаг дистанции
			range = RangeWhispering;// 10;
			message = ChatMode.WHISPER.getColor() + "%1$s" + ChatMode.WHISPER.getColor()
					+ " шепчет: %2$s";
		}
		// Личка
		if (isPm) {

			Matcher m = nickForPM.matcher(chatMessage);
			if (m.find()) {
				ranged = -1;// флаг особого чата
				if (Bukkit.getServer().getPlayer(m.group(1)) != null) {
					Player recipient = Bukkit.getServer().getPlayer(m.group(1));
					if (!recipient.equals(player)) {
						recipient.sendMessage(ChatColor.GRAY + "@" + name(player) + ": "
								+ ChatMode.PM.getColor() + m.group(2));
					}
					player.sendMessage(ChatColor.GRAY + "@" + name(player) + "->" + name(recipient)
							+ ": " + ChatMode.PM.getColor() + m.group(2));
					if (!Util.hasPermission(player, "mcnw.nospy")) {
						getPMRecipientsSpy(player, recipient, m.group(2));
					}
					Bukkit.getConsoleSender().sendMessage(
							ChatColor.GRAY + "spy@" + name(player) + "->" + name(recipient) + ": "
									+ m.group(2));
				} else {
					player.sendMessage("Игрока " + m.group(1) + " нет в сети");
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
				i = Integer.parseInt(m.group(1));
				i = i > minroll ? i : randrolldef;
				int j = rand.nextInt(i) + 1;
				message = ChatColor.LIGHT_PURPLE + "*" + name(player) + ChatColor.LIGHT_PURPLE
						+ " выбрасывает " + j + " из " + i + "*";

			} else {
				chatMessage = ChatColor.LIGHT_PURPLE + (chatMessage.substring(1));
				int chance = rand.nextInt(100);
				message = ChatColor.LIGHT_PURPLE + "* " + name(player) + " " + chatMessage
						+ ((chance > defchanse) ? luck : unluck) + " *";
			}

		}

		if (ranged > 0 && chatMessage.length() > 1 && (isAny)) {
			if (range != RangeMain) {
				Matcher space = _space.matcher(chatMessage.substring(1));
				chatMessage = space.replaceFirst("");
			}
		}
		if (isLocal) {
			Matcher space = _space.matcher(chatMessage.substring(1));
			chatMessage = space.replaceFirst("");

		}
		if (chatMessage.startsWith("?") && chatMessage.length() == 1) {
			player.sendMessage(ChatColor.AQUA + "=============================================");
			player.sendMessage(ChatColor.GOLD + "RPchat v 0.9");
			player.sendMessage(ChatColor.GOLD + "Авторы: ufatos, dark32");
			player.sendMessage(ChatColor.GOLD + "http://bit.ly/12Q8z4q");
			player.sendMessage(ChatColor.GOLD + "?/. для справки режимов чата");
			player.sendMessage(ChatColor.GOLD + "?/? для справки по переключению режимов чата");
			player.sendMessage(ChatColor.AQUA + "=============================================");
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/.") && chatMessage.length() == 3) {
			player.sendMessage(ChatColor.AQUA + "=============================================");
			player.sendMessage(ChatColor.GOLD + ChatMode.GLOBAL.getStartChar()
					+ " для глобального чата");
			player.sendMessage(ChatColor.GOLD + ChatMode.WORLD.getStartChar()
					+ " для мирового чата");
			player.sendMessage(ChatColor.GOLD + ChatMode.SHOUT.getStartChar() + " для крика");
			player.sendMessage(ChatColor.GOLD + ChatMode.WHISPER.getStartChar() + " для шепота");
			player.sendMessage(ChatColor.GOLD + ChatMode.PM.getStartChar()
					+ "<имя> сообщени для личного чата");
			player.sendMessage(ChatColor.GOLD
					+ "*<действие> для вероятного действия или *<целое число больше " + minroll
					+ "> для выброса случайного числа");
			player.sendMessage(ChatColor.AQUA + "=============================================");
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/?") && chatMessage.length() == 3) {
			player.sendMessage(ChatColor.AQUA + "=============================================");
			player.sendMessage(ChatColor.GOLD + "?/g - для выбора глобального режима");
			player.sendMessage(ChatColor.GOLD + "?/w - для выбора мирового мирового");
			player.sendMessage(ChatColor.GOLD + "?/s - для выбора режима крика");
			player.sendMessage(ChatColor.GOLD + "?/v - для выбора режима шепота");
			player.sendMessage(ChatColor.GOLD + "?/l - для выбора локального режима");
			player.sendMessage(ChatColor.GOLD + "?/? - для вызоа этой справки");
			if (Util.hasPermission(player, "mcnw.mute.help"))
				player.sendMessage(ChatColor.GOLD + "?/m - для вызоа справки по молчанке");
			player.sendMessage(ChatColor.AQUA + "=============================================");
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/m") && chatMessage.length() == 3) {
			player.sendMessage(tCC("&b============================================="));
			if (Util.hasPermission(player, "mcnw.mute.mute")) {
				player.sendMessage(tCC("&b%nick [chanel=g|w|s|v|l|p|a] [time=sec] &6- для молчанки"));
				player.sendMessage(tCC("&bg &6- Глобалный  &bw&6 - мировой"));
				player.sendMessage(tCC("&bs &6- крик       &bv&6 - шёпот"));
				player.sendMessage(tCC("&bl &6- локальный  &bp&6 - личные сообщения"));
				player.sendMessage(tCC("&ba &6- все"));
				if (Util.hasPermission(player, "mcnw.mute.unmute")) {
					player.sendMessage(tCC("&6 время равное 0 - для принудительносго снятия молчанки"));
				}
			}
			if (Util.hasPermission(player, "mcnw.mute.help"))
				player.sendMessage(tCC("&6?/m - для вызоа справки по молчанке"));
			player.sendMessage(tCC("&b============================================="));
			event.setCancelled(true);
		}
		// молчанка
		if (chatMessage.startsWith("%") && chatMessage.length() > 1) {
			// player.sendMessage(ChatColor.GRAY + "@");
			Matcher m = nickForMute.matcher(chatMessage);
			if (m.find()) {
				// player.sendMessage(ChatColor.GRAY + "@@");
				// String recipient = m.group(1);
				plugin.getBanStorage().mute(m.group(1), m.group(2), player);
				// player.sendMessage(ChatColor.GRAY + "@" + recipient +
				// "теперь молчит");
				event.setCancelled(true);
			}
		}
		// режимы чата
		if (chatMessage.startsWith("?/g") && chatMessage.length() == 3) {
			Util.setChatMode(player, ChatMode.GLOBAL.getModeId());
			player.sendMessage(ChatColor.GOLD + "Режим изменён на глобальный");
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/w") && chatMessage.length() == 3) {
			Util.setChatMode(player, ChatMode.WORLD.getModeId());
			player.sendMessage(ChatColor.GOLD + "Режим изменён на мировой");
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/s") && chatMessage.length() == 3) {
			Util.setChatMode(player, ChatMode.SHOUT.getModeId());
			player.sendMessage(ChatColor.GOLD + "Режим изменён на крик");
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/v") && chatMessage.length() == 3) {
			Util.setChatMode(player, ChatMode.WHISPER.getModeId());
			player.sendMessage(ChatColor.GOLD + "Режим изменён на шёпот");
			event.setCancelled(true);
		}
		if (chatMessage.startsWith("?/l") && chatMessage.length() == 3) {
			Util.setChatMode(player, ChatMode.LOCAL.getModeId());
			player.sendMessage(ChatColor.GOLD + "Режим изменён на локальный");
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

	// вывод имени
	private String name(Player p ) {
		return Displayname ? p.getDisplayName() : p.getName();

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
				recipient.sendMessage(ChatColor.GRAY + "spy@" + name(sender) + "->" + name(target)
						+ ": " + msg);
			}
		}
	}

	protected static String tCC(String string ) {
		return ChatColor.translateAlternateColorCodes('&', string);
		/*
		 * if (string == null) { return ""; } String newstring = string;
		 * newstring =
		 * chatColorPattern.matcher(newstring).replaceAll("\u00A7$1"); newstring
		 * = chatMagicPattern.matcher(newstring).replaceAll("\u00A7$1");
		 * newstring =
		 * chatBoldPattern.matcher(newstring).replaceAll("\u00A7$1"); newstring
		 * = chatStrikethroughPattern.matcher(newstring).replaceAll("\u00A7$1");
		 * newstring =
		 * chatUnderlinePattern.matcher(newstring).replaceAll("\u00A7$1");
		 * newstring =
		 * chatItalicPattern.matcher(newstring).replaceAll("\u00A7$1");
		 * newstring =
		 * chatResetPattern.matcher(newstring).replaceAll("\u00A7$1"); return
		 * newstring;
		 */
	}

	static void updateDisplayName(Player player ) {
		if (!Util.usePEX) {
			return;
		}
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
		if (user == null) {
			return;
		}
		player.setDisplayName(Chat.tCC(user.getPrefix() + player.getName() + user.getSuffix()));
	}
}
