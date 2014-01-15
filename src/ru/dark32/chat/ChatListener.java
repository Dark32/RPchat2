package ru.dark32.chat;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ru.dark32.chat.chanels.BaseChanel;
import ru.dark32.chat.ichanels.IChanel;

/**
 * @author Andrew
 * 
 */
public class ChatListener implements Listener {
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent event ) {
		// тот кто отправил сообщение
		final Player sender = event.getPlayer();
		// сообщение
		String message = event.getMessage();
		// формат
		String format = "";
		// префикс, первый символ
		final char prefix = message.charAt(0);
		// ИД канала по префиксы, -1 - нет канала по префиксы
		final int prefixChanel = ChanelRegister.getIndexByPrefix(sender, prefix);
		// ИД канала по вещи в руках, -1 нет канала по вещи в руках
		final int itemChanel = ChanelRegister.getIndexByItem(sender.getItemInHand());
		// ИД активного канала, не иницилизируем по умолчанию
		int indexChanel = Util.getModeIndex(sender.getName());
		// Сообщение длинее одного знака
		if (itemChanel != -1) {
			// меняем канаал на канал по вещи
			indexChanel = itemChanel;
		}
		if (message.length() > 1) {
			// ИД канала по префексу есть
			if (prefixChanel != -1) {
				// меняем канаал на канал по префексу
				indexChanel = prefixChanel;
				// убираем префикс
				message = message.substring(1).trim();
			}
		}
		// получаем канал
		final IChanel chanel = ChanelRegister.getByIndex(indexChanel);
		// может ли сендер вообще говорить в этот канал?
		if (hasMute(sender, indexChanel)) {
			event.setCancelled(true);
			return;
		}
		// есть ли права говорить в этот чат
		if (chanel.isNeedPerm() && !Util.hasPermission(sender, Main.BASE_PERM + "." + chanel.getInnerName() + ".say")) {
			sender.sendMessage(ValueStorage.noPerm.replace("$1", chanel.getName()));
			event.setCancelled(true);
			return;
		}
		// соблюдены ли иные условия отправки
		if (!chanel.canSend(sender, message)) {
			event.setCancelled(true);
			return;
		}
		// обрабатываем сообение перед отправкой
		message = chanel.preformatMessage(sender, message);
		// чистим список получателей
		event.getRecipients().clear();
		// добавляем получателей согласно типу чата
		Set<Player> recipient = chanel.getRecipients(sender);
		// отправка пре сообщения
		chanel.preSend(sender, message, recipient);
		// добавляем получателей согласно договору о промышленном шпонаже
		recipient.addAll(chanel.getSpyRecipients(sender));
		event.getRecipients().addAll(recipient);
		// получаем и обрабатываем формат
		format = chanel.format(sender, chanel.getFormat());
		// устанавливаем формат
		event.setFormat(format);
		// устанавливаем сообщение
		event.setMessage(message);
	}

	private Boolean hasMute(final Player player, final int indexChanel ) {
		if (Main.getBanStorage().isMuted(player.getName(), indexChanel)) {
			String msg = ValueStorage.muteMessage;
			long time = Main.getBanStorage().getTimeMute(player.getName(), indexChanel);
			msg = Mute.unParseTime(msg, time);
			msg = Util.suffixLatter(msg);
			player.sendMessage(msg);
			return true;
		}
		return false;
	}
}
