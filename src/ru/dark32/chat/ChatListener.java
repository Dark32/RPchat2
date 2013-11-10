package ru.dark32.chat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatListener implements Listener {
	private Random	rand	= new Random();
	private Main	plugin;

	public ChatListener(Main pluging ){
		this.plugin = pluging;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event ) {
		Player player = event.getPlayer();// получаем игрока, вызвавшего событие
		String message = ValueStorage.localChatFormat;
		String chatMessage = event.getMessage();// сообщение
		char firstChar = chatMessage.charAt(0);
		char secondChar = chatMessage.length() > 1 ? chatMessage.charAt(1) : 0;
		char thirdChar = chatMessage.length() > 2 ? chatMessage.charAt(2) : 0;
		ItemStack inHand = player.getItemInHand();// вещь в руках
		double range = ValueStorage.rangeLocal;// локальный чат, радиус по
												// умолчанию
		int mode = Util.getChatMode(player.getName());
		boolean isMoreThenOne = chatMessage.length() > 1;
		boolean isGlobalChatItemInHand = inHand != null
				&& inHand.getDurability() == ValueStorage.globalSubId;
		boolean isWorldChatItemInHand = inHand != null
				&& inHand.getDurability() == ValueStorage.worldSubId;

		if (ValueStorage.experemental) {
			isGlobalChatItemInHand &= inHand != null && inHand.getType() == ValueStorage.globalMa;
			isWorldChatItemInHand &= inHand != null && inHand.getType() == ValueStorage.worldMa;
		} else {
			isGlobalChatItemInHand &= inHand != null && inHand.getTypeId() == ValueStorage.globalId;
			isWorldChatItemInHand &= inHand != null && inHand.getTypeId() == ValueStorage.worldId;
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
			} else if (firstChar == '%') {
				mode = -1;
				chatMessage = chatMessage.substring(1).trim();
			}
		}

		if (mode == ChatMode.GLOBAL.getModeId()) {// Глобальный
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			} else if (Util.hasPermission(player, "mcnw.global")) {
				message = ValueStorage.globalChatFormat;
				if (!Util.hasPermission(player, "mcnw.global.no_item")) {
					if (isGlobalChatItemInHand) {
						loseitem(player);
					} else {
						player.sendMessage(ValueStorage.nei);
						event.setCancelled(true);
						return;
					}
				}
			} else {
				player.sendMessage(ValueStorage.noPerm.replace("$1", ValueStorage.globalChat));
				event.setCancelled(true);
				return;
			}
		} else if (mode == ChatMode.WORLD.getModeId()) {// Мировой
			if (hasMute(player, mode)) {
				event.setCancelled(true);
			}
			if (Util.hasPermission(player, "mcnw.world")) {
				message = ValueStorage.worldChatFormat;
				if (!Util.hasPermission(player, "mcnw.world.no_item")) {
					if (isWorldChatItemInHand) {
						loseitem(player);
					} else {
						player.sendMessage(ValueStorage.nei);
						event.setCancelled(true);
						return;
					}
				}
			} else {
				player.sendMessage(ValueStorage.noPerm.replace("$1", ValueStorage.worldChat));
				event.setCancelled(true);
				return;
			}
		} else if (mode == ChatMode.SHOUT.getModeId()) {// Крик
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = ValueStorage.RangeShout;
			message = ValueStorage.shoutChatFormat;
		} else if (mode == ChatMode.LOCAL.getModeId()) {// локаль
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = ValueStorage.rangeLocal;
			message = ValueStorage.localChatFormat;
		} else if (mode == ChatMode.WHISPER.getModeId()) {// шепот
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = ValueStorage.RangeWhispering;
			message = ValueStorage.whisperingChatFormat;
		} else if (mode == ChatMode.PM.getModeId()) {// PM
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			int _ind = chatMessage.indexOf(" ");
			if (_ind == -1) {
				player.sendMessage(ValueStorage.noinputmsg);
				event.setCancelled(true);
				return;
			}
			String pmNick = chatMessage.substring(0, _ind);
			String _msg = chatMessage.substring(_ind + 1, chatMessage.length());
			Player recipient = getPlayerByName(pmNick);
			if (recipient == null) {
				player.sendMessage(ValueStorage.playeNotFound.replace("$1", pmNick));
				event.setCancelled(true);
				return;
			}
			String pmMSG1 = ValueStorage.pmChatFormat
					.replace("%sf", ChatListener.getSuffix(player.getName()))
					.replace("%pf", ChatListener.getPreffix(player.getName()))
					.replace("%p", player.getName()).replace("%r", recipient.getName())
					.replace(" %msg", _msg);
			String pmMSG2 = ValueStorage.pmChatFormat2
					.replace("%sf", ChatListener.getSuffix(player.getName()))
					.replace("%pf", ChatListener.getPreffix(player.getName()))
					.replace("%p", player.getName()).replace("%r", recipient.getName())
					.replace(" %msg", _msg);
			String pmCnsoleSpy = ChatListener.tCC("&7spy@" + player.getName() + "->"
					+ recipient.getName() + ": " + _msg);
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
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = ValueStorage.rangeLocal;
			int iChance;
			if (Util.isInteger(chatMessage)) {
				iChance = chatMessage.length() < 5 ? Integer.parseInt(chatMessage) : 9999;
				iChance = iChance > ValueStorage.minroll ? iChance : ValueStorage.randrolldef;
				int iRoll = rand.nextInt(iChance) + 1;
				message = ValueStorage.rnd.replace("$1", String.valueOf(iRoll)).replace(
						"$2",
							String.valueOf(iChance));

			} else {
				chatMessage = ChatListener.tCC("&5" + chatMessage);
				int chance = rand.nextInt(100);
				message = ChatListener.tCC(ValueStorage.roll.replace(
						"$1",
							(chance > ValueStorage.defchanse) ? ValueStorage.luck
									: ValueStorage.unluck));
			}

		} else if (mode == ChatMode.BROADCAST.getModeId()) {// Броадкаст
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			if (Util.hasPermission(player, "mcnw.broadcast")) {
				message = "%2$s";
				Bukkit.getConsoleSender().sendMessage(
						ValueStorage.broadcastspy.replace("%p", player.getName()));
			} else {
				player.sendMessage(ValueStorage.noPerm.replace("$1", ValueStorage.broadcast));
				Bukkit.getConsoleSender().sendMessage(
						ValueStorage.trybroadcastspy.replace("%p", player.getName()));
				event.setCancelled(true);
				return;
			}
		} else if (mode == -1) {
			int _ind = chatMessage.indexOf(" ");
			if (_ind == -1) {
				player.sendMessage(ValueStorage.noinputmsg);
				event.setCancelled(true);
				return;
			}
			String pmNick = chatMessage.substring(0, _ind);
			String _msg = chatMessage.substring(_ind + 1, chatMessage.length());
			Main.getBanStorage().mute(pmNick, _msg, player);
			event.setCancelled(true);
			return;

		}
		if (firstChar == '?' && chatMessage.length() == 1) {
			ChatListener.getHelp(player);
			event.setCancelled(true);
			return;
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
			chatMessage = ChatListener.tCC(chatMessage);
		}
		message = message.replace("%sf", ChatListener.getSuffix(player.getName()))
				.replace("%pf", ChatListener.getPreffix(player.getName())).replace("%p", "%1$s")
				.replace("%msg", "%2$s");
		event.setFormat(message);
		event.setMessage(chatMessage);
	}

	// теряем вещь
	private void loseitem(Player player ) {
		ItemStack inHand = player.getItemInHand();
		int AmoutHand = inHand.getAmount() - 1;
		ItemStack inHandrem = null;
		if (ValueStorage.experemental) {
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
				recipient.sendMessage(ChatListener.tCC("&7spy@" + sender.getName() + "->"
						+ target.getName() + ": " + msg));
			}
		}
	}

	static String tCC(String string ) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static String getPreffix(String name ) {
		if (!Util.usePEX) {
			return "";
		}
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);
		if (user == null) {
			return "";
		}

		return user.getPrefix();
	}

	public static String getSuffix(String name ) {
		if (!Util.usePEX) {
			return "";
		}
		PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);
		if (user == null) {
			return "";
		}
		return user.getSuffix();
	}

	private Boolean hasMute(Player player, int i ) {
		if (Main.getBanStorage().isMuted(player.getName(), i)) {
			player.sendMessage(ValueStorage.mute.replace("$1", ""
					+ Main.getBanStorage().getTimeMute(player.getName(), i)));
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
		msg.add("&6Link: http://goo.gl/wRJecu");
		msg.addAll(ValueStorage.baseHelp);
		msg.add("&b=============================================");
		for (String s : msg) {
			player.sendMessage(ChatListener.tCC(s));
		}
	}

	private void getAnotherHelp(Player player, char thirdChar ) {
		List<String> msg = new ArrayList<String>();
		msg.add("&b=============================================");
		switch (thirdChar) {
			case ('g'): {
				Util.setChatMode(player.getName(), ChatMode.GLOBAL.getModeId());
				msg.add(ValueStorage.changechanel.replace("$1", ValueStorage.globalChat));
				break;
			}
			case ('w'): {
				Util.setChatMode(player.getName(), ChatMode.WORLD.getModeId());
				msg.add(ValueStorage.changechanel.replace("$1", ValueStorage.worldChat));
				break;
			}
			case ('s'): {
				Util.setChatMode(player.getName(), ChatMode.SHOUT.getModeId());
				msg.add(ValueStorage.changechanel.replace("$1", ValueStorage.shoutChat));
				break;
			}
			case ('l'): {
				Util.setChatMode(player.getName(), ChatMode.LOCAL.getModeId());
				msg.add(ValueStorage.changechanel.replace("$1", ValueStorage.localChat));
				break;
			}
			case ('v'): {
				Util.setChatMode(player.getName(), ChatMode.WHISPER.getModeId());
				msg.add(ValueStorage.changechanel.replace("$1", ValueStorage.whisperingChat));
				break;
			}
			case '.': {
				msg.addAll(ValueStorage.helpPrefix);
				break;
			}
			case '?': {
				msg.addAll(ValueStorage.chanelswitch);
				if (Util.hasPermission(player, "mcnw.mute.help")) {
					msg.add(ValueStorage.muteHelp);
				}
				break;
			}
			case 'm': {
				if (Util.hasPermission(player, "mcnw.mute.mute")) {
					msg.addAll(ValueStorage.muteMute);
				}
				if (Util.hasPermission(player, "mcnw.mute.unmute")) {
					msg.add(ValueStorage.muteUnmute);
				}
				if (Util.hasPermission(player, "mcnw.mute.help")) {
					msg.addAll(ValueStorage.muteHelp2);
				}
				break;
			}
			default: {
				msg.add(ValueStorage.unknow  + thirdChar);
				break;
			}
		}
		msg.add("&b=============================================");
		for (String s : msg) {
			player.sendMessage(ChatListener.tCC(s));
		}
	}

	private Player getPlayerByName(String name ) {
		switch (ValueStorage.PMSearchNickMode) {
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

}
