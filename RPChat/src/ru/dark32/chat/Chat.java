package ru.dark32.chat;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

@SuppressWarnings("deprecation")
public class Chat implements Listener {

	private double RangeMain; // локальный чат
	private double RangeWhispering; // шёпот
	private double RangeShout; // крик
	private int globalId; // Id вещи для вещания глобально
	private int globalSubId;// метадата вещи для вещания глобально
	private int worldId;// Id вещи для вещания мирово
	private int worldSubId;// метадата вещи для вещания мирово
	private boolean DeathMessage;// скрыть ли сообщения о смерти
	protected static Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-F])");
	protected static Pattern chatMagicPattern = Pattern.compile("(?i)&([K])");
	protected static Pattern chatBoldPattern = Pattern.compile("(?i)&([L])");
	protected static Pattern chatStrikethroughPattern = Pattern.compile("(?i)&([M])");
	protected static Pattern chatUnderlinePattern = Pattern.compile("(?i)&([N])");
	protected static Pattern chatItalicPattern = Pattern.compile("(?i)&([O])");
	protected static Pattern chatResetPattern = Pattern.compile("(?i)&([R])");
	protected static Pattern nick = Pattern.compile("[@2]([\\d\\w_]+)\\s(.+)");// Извлекаем ник
	protected static Pattern _space = Pattern.compile("^\\s+");// пробел?

	public Chat(FileConfiguration config) {
		this.RangeMain = config.getDouble("Range.main", this.RangeMain);// радиус обычного чата
		this.RangeWhispering = config.getDouble("Range.whispering", this.RangeWhispering);// радиус шёпота
		this.RangeShout = config.getDouble("Range.shout", this.RangeShout);// радиус крика
		this.globalId = config.getInt("Id.globalId", this.globalId);//
		this.globalSubId = config.getInt("Id.globalSubId", this.globalSubId);//
		this.worldId = config.getInt("Id.worldId", this.worldId);//
		this.worldSubId = config.getInt("Id.worldSubId", this.worldSubId);//
		this.DeathMessage = config.getBoolean("disableDeathMessage", this.DeathMessage);//
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		updateDisplayName(event.getPlayer());
		event.setJoinMessage(null);
	}

	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = "%1$s: %2$s";
		String chatMessage = event.getMessage();
		int ranged = 1; // Тип чата
		/*
		 * 3 - глобальный
		 * 2 - мировой
		 * 1 - флаг дистанции
		 * 
		 * @ - ЛС
		 * $ - глобальный чат
		 * > - мировой чат
		 * ! - крик
		 * # - шепот
		 */
		ItemStack inHand = player.getItemInHand();
		double range = RangeMain;// локальный чат
		// Глобальный
		if ((chatMessage.startsWith("$") || chatMessage.startsWith(";")) && chatMessage.length() > 1) {
			
			if (main.hasPermission(player, "mcnw.global")) {
				message = "%1$s всем: " + ChatColor.YELLOW + "%2$s";
				range = RangeWhispering;
				if (main.hasPermission(player, "mcnw.global.no_item")) {
					ranged = 3; // флаг глобально, межмирового чата
				} else if (inHand != null && inHand.getTypeId() == globalId && inHand.getDurability() == globalSubId) {
					ranged = 3; // флаг глобально, межмирового чата
					loseitem(player);
					// message = "%1$s всем: " + ChatColor.YELLOW + "%2$s";
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
		if ((chatMessage.startsWith(">") || chatMessage.startsWith(".")) && chatMessage.length() > 1) {
			if (main.hasPermission(player, "mcnw.world")) {
				message = "%1$s в мир: " + ChatColor.GOLD + "%2$s";
				range = RangeWhispering;
				if (main.hasPermission(player, "mcnw.world.no_item")) {
					ranged = 2;// флаг мирового чата
				} else if (inHand != null && inHand.getTypeId() == worldId && inHand.getDurability() == worldSubId) {
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
		if (chatMessage.startsWith("!") && chatMessage.length() > 1) {
			ranged = 1; // флаг дистанции
			range = RangeShout;// 1000;
			message = "%1$s кричит: " + ChatColor.RED + "%2$s";

		}
		// Шепот
		if ((chatMessage.startsWith("#") || chatMessage.startsWith("№")) && chatMessage.length() > 1) {
			ranged = 1;// флаг дистанции
			range = RangeWhispering;// 10;
			message = ChatColor.GRAY + "%1$s шепчет: %2$s";
		}
		// Личка
		if ((chatMessage.startsWith("@") || chatMessage.startsWith("2")) && chatMessage.length() > 1) {
			ranged = -1;// флаг особого чата
			Matcher m = nick.matcher(chatMessage);
			if (m.find()) {
				if (Bukkit.getServer().getPlayer(m.group(1)) != null) {
					Player recipient = Bukkit.getServer().getPlayer(m.group(1));
					if (!recipient.equals(player))
						recipient.sendMessage(ChatColor.GRAY + "@" + player.getDisplayName() + ": "
						        + ChatColor.DARK_PURPLE + m.group(2));
					player.sendMessage(ChatColor.GRAY + "@" + player.getDisplayName() + "->"
					        + recipient.getDisplayName() + ": " + ChatColor.DARK_PURPLE + m.group(2));
					if (!main.hasPermission(player, "mcnw.nospy"))
						getPMRecipientsSpy(player, recipient, m.group(2));

					Bukkit.getConsoleSender().sendMessage(
					        ChatColor.GRAY + "spy@" + player.getDisplayName() + "->" + recipient.getDisplayName()
					                + ": " + m.group(2));
				} else {
					player.sendMessage("Игрока " + m.group(1) + " нет в сети");
				}
			} else {
				player.sendMessage(ChatColor.YELLOW + "Нужно писать так @<имя> сообщение");
			}
			event.setCancelled(true);
		}
		if (ranged > 0 && chatMessage.length() > 1) {
			if (range != RangeMain) {
				Matcher space = _space.matcher(chatMessage.substring(1));
				chatMessage = space.replaceFirst("");
			}
		}
		if (chatMessage.startsWith("?") && chatMessage.length() == 1) {
			player.sendMessage(ChatColor.YELLOW + "RPchat. Сделано ufatos'ом, доделано dark32'ом ");
			player.sendMessage(ChatColor.YELLOW + "$ или ; для глобального чата");
			player.sendMessage(ChatColor.YELLOW + "> или . для мирового чата");
			player.sendMessage(ChatColor.YELLOW + "! для крика");
			player.sendMessage(ChatColor.YELLOW + "# или № для крика");
			player.sendMessage(ChatColor.YELLOW + "2<имя> сообщени или @<имя> сообщени для личного чата");
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
	private void loseitem(Player player) {
		ItemStack inHand = player.getItemInHand();
		int AmoutHand = inHand.getAmount() - 1;
		ItemStack inHandrem = new ItemStack(inHand.getTypeId(), inHand.getAmount() - 1);
		if (AmoutHand <= 0) {
			inHandrem = null;
		}
		player.setItemInHand(inHandrem);
		return;
	}

	/* убираем сообщение о смерти */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		if (DeathMessage) {
			if (event instanceof PlayerDeathEvent) {
				PlayerDeathEvent deathEvent = (PlayerDeathEvent) event;
				deathEvent.setDeathMessage(null);
			}
		}
	}

	// Лакальный чат
	protected List<Player> getLocalRecipients(Player sender, double range) {
		Location playerLocation = sender.getLocation();
		List<Player> recipients = new LinkedList<Player>();
		double squaredDistance = Math.pow(range, 2);
		// sender.sendMessage("567");
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if (recipient.getWorld().equals(sender.getWorld())
			        && playerLocation.distanceSquared(recipient.getLocation()) < squaredDistance) {
				recipients.add(recipient);
			} else if (main.hasPermission(recipient, "mcnw.spy")) {
				recipients.add(recipient);
			} else {
				continue;
			}
		}
		return recipients;
	}

	// Список тех кому отправлять(мир)
	protected List<Player> getWorldRecipients(Player sender, String message) {
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
	protected void getPMRecipientsSpy(Player sender, Player target, String msg) {
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if (main.hasPermission(recipient, "mcnw.pmspy") && !recipient.equals(target)) {
				recipient.sendMessage(ChatColor.GRAY + "spy@" + sender.getDisplayName() + "->"
				        + target.getDisplayName() + ": " + msg);
			}
		}
	}

	// ChatManager Permissions Prefix своровал код, чтобы не парится
	protected static String translateColorCodes(String string) {
		if (string == null) {
			return "";
		}
		String newstring = string;
		newstring = chatColorPattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatMagicPattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatBoldPattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatStrikethroughPattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatUnderlinePattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatItalicPattern.matcher(newstring).replaceAll("\u00A7$1");
		newstring = chatResetPattern.matcher(newstring).replaceAll("\u00A7$1");
		return newstring;
	}

	static void updateDisplayName(Player player) {
		if (!main.usePEX)
			return;
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
		if (user == null) {
			return;
		}
		player.setDisplayName(Chat.translateColorCodes(user.getPrefix() + player.getName()));
	}
}
