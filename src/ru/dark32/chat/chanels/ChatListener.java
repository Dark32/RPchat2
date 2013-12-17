package ru.dark32.chat.chanels;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ValueStorage;
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
		final int prefixChanel = ChanelRegister.getIndexByPrefix(prefix);
		// ИД канала по вещи в руках, -1 нет канала по вещи в руках
		final int itemChanel = ChanelRegister.getIndexByItem(sender.getItemInHand());
		// ИД активного канала, не иницилизируем по умолчанию
		int indexChanel = Util.getModeIndex(sender.getName());
		// Сообщение длинее одного знака
		if (message.length() > 1) {
			if (itemChanel != -1) {
				// меняем канаал на канал по вещи
				indexChanel = itemChanel;
			}
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
		if (!chanel.isNeedPerm() || !Util.hasPermission(sender, Main.BASE_PERM + "." + chanel.getInnerName())) {
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
		event.getRecipients().addAll(chanel.getRecipients(sender));
		// отправка пре сообщения
		chanel.preSend(sender, message, event.getRecipients().size());
		// System.out.println(event.getRecipients());
		// получаем и обрабатываем формат
		format = chanel.format(sender, chanel.getFormat());
		// устанавливаем формат
		event.setFormat(format);
		// устанавливаем сообщение
		event.setMessage(message);
	}

	private Boolean hasMute(final Player player, final int indexChanel ) {
		if (Main.getBanStorage().isMuted(player.getName(), indexChanel)) {
			player.sendMessage(ValueStorage.muteMessage.replace("$1",
					String.valueOf(Main.getBanStorage().getTimeMute(player.getName(), indexChanel))));

			return true;
		}
		return false;
	}
}
