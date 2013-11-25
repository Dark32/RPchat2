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

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event ) {
		Player player = event.getPlayer();// получаем игрока, вызвавшего событие
		String message = ValueStorage.local.getFormat();
		String chatMessage = event.getMessage();// сообщение
		char firstChar = chatMessage.charAt(0);
		char secondChar = chatMessage.length() > 1 ? chatMessage.charAt(1) : 0;
		char thirdChar = chatMessage.length() > 2 ? chatMessage.charAt(2) : 0;
		ItemStack inHand = player.getItemInHand();// вещь в руках
		// локальный чат, радиус по умолчанию
		double range = ValueStorage.local.getRange();
		int mode = Util.getChatMode(player.getName());
		boolean isMoreThenOne = chatMessage.length() > 1;
		boolean isGlobalChatItemInHand = inHand != null
				&& inHand.getDurability() == ValueStorage.global.getSubId();
		boolean isWorldChatItemInHand = inHand != null
				&& inHand.getDurability() == ValueStorage.world.getSubId();

		if (ValueStorage.experemental) {
			isGlobalChatItemInHand &= inHand != null
					&& inHand.getType() == ValueStorage.global.getMaterial();
			isWorldChatItemInHand &= inHand != null
					&& inHand.getType() == ValueStorage.world.getMaterial();
		} else {
			isGlobalChatItemInHand &= inHand != null
					&& inHand.getTypeId() == ValueStorage.global.getId();
			isWorldChatItemInHand &= inHand != null
					&& inHand.getTypeId() == ValueStorage.world.getId();
		}
		if (isMoreThenOne) {
			int _mode = Chanel.getIndexByPrefix(firstChar);
			if (_mode != -2) {
				mode = _mode;
				chatMessage = chatMessage.substring(1).trim();
			}
		}

		if (mode == ValueStorage.global.getIndex()) {// Глобальный
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			} else if (Util.hasPermission(player, "mcnw.global")) {
				message = ValueStorage.global.getFormat();
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
				player.sendMessage(ValueStorage.noPerm.replace("$1", ValueStorage.global.getName()));
				event.setCancelled(true);
				return;
			}
		} else if (mode == ValueStorage.world.getIndex()) {// Мировой
			if (hasMute(player, mode)) {
				event.setCancelled(true);
			}
			if (Util.hasPermission(player, "mcnw.world")) {
				message = ValueStorage.world.getFormat();
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
				player.sendMessage(ValueStorage.noPerm.replace("$1", ValueStorage.world.getName()));
				event.setCancelled(true);
				return;
			}
		} else if (mode == ValueStorage.shout.getIndex()) {// Крик
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = ValueStorage.shout.getRange();
			message = ValueStorage.shout.getFormat();
		} else if (mode == ValueStorage.local.getIndex()) {// локаль
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = ValueStorage.local.getRange();
			message = ValueStorage.local.getFormat();
		} else if (mode == ValueStorage.whisper.getIndex()) {// шепот
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = ValueStorage.whisper.getRange();
			message = ValueStorage.whisper.getFormat();
		} else if (mode == ValueStorage.pm.getIndex()) {// pm
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			int _ind = chatMessage.indexOf(" ");
			if (_ind == -1) {
				player.sendMessage(ValueStorage.pm.get("NoinputMsg"));
				event.setCancelled(true);
				return;
			}
			String pmNick = chatMessage.substring(0, _ind);
			String _msg = chatMessage.substring(_ind + 1, chatMessage.length());
			Player recipient = getPlayerByName(pmNick);
			if (recipient == null) {
				player.sendMessage(ValueStorage.pm.get("PlayeNotFound").replace("$1", pmNick));
				event.setCancelled(true);
				return;
			}
			String pmMSG1 = ValueStorage.pm.getFormat()
					.replace("%sf", ChatListener.getSuffix(player.getName()))
					.replace("%pf", ChatListener.getPreffix(player.getName()))
					.replace("%p", player.getName()).replace("%r", recipient.getName())
					.replace(" %msg", _msg);
			String pmMSG2 = ValueStorage.pm.get("FormatFrom")
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
		} else if (mode == ValueStorage.chance.getIndex()) {
			// действия с вероятностью
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			range = ValueStorage.local.getRange();
			int iChance;
			if (Util.isInteger(chatMessage)) {
				iChance = chatMessage.length() < 5 ? Integer.parseInt(chatMessage) : 9999;
				iChance = iChance > ValueStorage.chanseMinRoll ? iChance
						: ValueStorage.chanseDefaultRoll;
				int iRoll = rand.nextInt(iChance) + 1;
				message = ValueStorage.chance.get("RollFormat")
						.replace("$1", String.valueOf(iRoll))
						.replace("$2", String.valueOf(iChance));

			} else {
				chatMessage = ChatListener.tCC("&5" + chatMessage);
				int chance = rand.nextInt(100);
				message = ChatListener.tCC(ValueStorage.chance.getFormat().replace(
						"$1",
						(chance > ValueStorage.chanseVaule) ? ValueStorage.chance.get("Luck")
								: ValueStorage.chance.get("Unluck")));
			}

		} else if (mode == ValueStorage.broadcast.getIndex()) {// Броадкаст
			if (hasMute(player, mode)) {
				event.setCancelled(true);
				return;
			}
			if (Util.hasPermission(player, "mcnw.broadcast")) {
				message = "%2$s";
				Bukkit.getConsoleSender().sendMessage(
						ValueStorage.broadcast.get("ConsoleSpy").replace("%p", player.getName()));
			} else {
				player.sendMessage(ValueStorage.noPerm.replace("$1",
						ValueStorage.broadcast.getName()));
				Bukkit.getConsoleSender().sendMessage(
						ValueStorage.broadcast.get("Spy").replace("%p", player.getName()));
				event.setCancelled(true);
				return;
			}
		} else if (mode == -1) {
			int _ind = chatMessage.indexOf(" ");
			if (_ind == -1) {
				player.sendMessage(ValueStorage.pm.get("NoinputMsg"));
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
		if (mode == ValueStorage.shout.getIndex() || mode == ValueStorage.whisper.getIndex()
				|| mode == ValueStorage.local.getIndex()) {
			event.getRecipients().clear();
			event.getRecipients().addAll(this.getLocalRecipients(player, range, mode));
		} else if (mode == ValueStorage.world.getIndex()) {
			event.getRecipients().clear();
			event.getRecipients().addAll(this.getWorldRecipients(player, message));
		} else if (mode == ValueStorage.broadcast.getIndex()) {
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
	protected List<Player> getLocalRecipients(Player sender, double range, int chanel ) {
		Location playerLocation = sender.getLocation();
		List<Player> recipients = new LinkedList<Player>();
		double squaredDistance = Math.pow(range, 2);
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if ( Main.getDeafStorage().isDeaf(sender.getName(), chanel)){
				continue;
			}else if (recipient.getWorld().equals(sender.getWorld())
					&& playerLocation.distanceSquared(recipient.getLocation()) < squaredDistance
					) {
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
			if ( Main.getDeafStorage().isDeaf(sender.getName(),ValueStorage.world.getIndex())){
				continue;
			}else if (!recipient.getWorld().equals(sender.getWorld()) 
					&& !Util.hasPermission(recipient, "mcnw.spy")) {
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
			player.sendMessage(ValueStorage.muteMessage.replace("$1", ""
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
		// msg.add("&6Link: http://goo.gl/wRJecu");
		msg.addAll(ValueStorage.helpBase);
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
				Util.setChatMode(player.getName(), ValueStorage.global.getIndex());
				msg.add(ValueStorage.helpChangeChanel.replace("$1", ValueStorage.global.getName()));
				break;
			}
			case ('w'): {
				Util.setChatMode(player.getName(), ValueStorage.world.getIndex());
				msg.add(ValueStorage.helpChangeChanel.replace("$1", ValueStorage.world.getName()));
				break;
			}
			case ('s'): {
				Util.setChatMode(player.getName(), ValueStorage.shout.getIndex());
				msg.add(ValueStorage.helpChangeChanel.replace("$1", ValueStorage.shout.getName()));
				break;
			}
			case ('l'): {
				Util.setChatMode(player.getName(), ValueStorage.local.getIndex());
				msg.add(ValueStorage.helpChangeChanel.replace("$1", ValueStorage.local.getName()));
				break;
			}
			case ('v'): {
				Util.setChatMode(player.getName(), ValueStorage.whisper.getIndex());
				msg.add(ValueStorage.helpChangeChanel.replace("$1", ValueStorage.whisper.getName()));
				break;
			}
			case '.': {
				msg.addAll(ValueStorage.helpPrefix);
				break;
			}
			case '?': {
				msg.addAll(ValueStorage.helpChanelsSitch);
				if (Util.hasPermission(player, "mcnw.mute.help")) {
					msg.add(ValueStorage.helpMute);
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
				if (Util.hasPermission(player, "mcnw.mute.see")) {
					msg.add(ValueStorage.muteSee);
				}
				if (Util.hasPermission(player, "mcnw.mute.see.self")) {
					msg.add(ValueStorage.muteSeeSelf);
				}
				if (Util.hasPermission(player, "mcnw.mute.help")) {
					msg.addAll(ValueStorage.muteHelp);
				}
				break;
			}
			default: {
				msg.add(ValueStorage.muteUnknow + thirdChar);
				break;
			}
		}
		msg.add("&b=============================================");
		for (String s : msg) {
			player.sendMessage(ChatListener.tCC(s));
		}
	}

	private Player getPlayerByName(String name ) {
		switch (ValueStorage.PmSearchNickMode) {
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
