package ru.dark32.chat.chanels;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ValueStorage;
import ru.dark32.chat.ichanels.IChanceChanel;
import ru.dark32.chat.ichanels.IChanel;
import ru.dark32.chat.ichanels.IItemChanel;
import ru.dark32.chat.ichanels.IPersonalMessagesChanel;

/**
 * @author Andrew
 * 
 */
public class ChatListener implements Listener {
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event ) {
		// тот кто отправил сообщение
		Player sender = event.getPlayer();
		// сообщение
		String message = event.getMessage();
		// формат
		String format = "";
		// префикс, первый символ
		char prefix = message.charAt(0);
		// ИД канала по префиксы, -1 - нет канала по префиксы
		int prefixChanel = ChanelRegister.getIndexByPrefix(prefix);
		// ИД канала по вещи в руках, -1 нет канала по вещи в руках
		int itemChanel = ChanelRegister.getIndexByItem(sender.getItemInHand());
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
		IChanel chanel = ChanelRegister.getByIndex(indexChanel);
		// может ли сендер вообще говорить в этот канал?
		if (hasMute(sender, indexChanel)) {
			event.setCancelled(true);
			return;
		}
		// есть ли права говорить в этот чат
		if (!Util.hasPermission(sender, "mcnw." + chanel.getInnerName())) {
			sender.sendMessage(ValueStorage.noPerm.replace("$1", chanel.getName()));
			event.setCancelled(true);
			return;
		}
		// Фильтруем каналы
		switch (chanel.getType()) {
		// Базовый
			case BASE: {
				break;
			}
			case RANGE: {
				break;
			}
			case RANGE_ITEM:
			case REQUISITE:
			case ITEM: {
				// может ли говорить без вещи
				if (!Util.hasPermission(sender, "mcnw." + chanel.getInnerName() + ".no_item")) {
					// если вещь в руках совпала
					if (((IItemChanel) chanel).equalItem(sender.getItemInHand())) {
						// теряем 1 вещь
						((IItemChanel) chanel).loseItem(sender);
					} else { // иначе
						// глаголим, что вещи нет
						sender.sendMessage(ValueStorage.nei);
						event.setCancelled(true);
						return;
					}
				}
				break;
			}
			case CHANCE: {
				break;
			}
			case PM: {
				// отправляем сообщение цели
				((IPersonalMessagesChanel) chanel).sendMessage(sender, message);
				break;
			}
			default:
				break;
		}
		// обрабатываем сообение перед отправкой
		message = chanel.preformat(message);
		// чистим список получателей
		event.getRecipients().clear();
		// добавляем получателей согласно типу чата
		event.getRecipients().addAll(chanel.getRecipients(sender));
		// System.out.println(event.getRecipients());
		// получаем и обрабатываем формат
		format = chanel.format(sender, chanel.getFormat());
		// устанавливаем формат
		event.setFormat(format);
		// устанавливаем сообщение
		event.setMessage(message);
	}

	private Boolean hasMute(Player player, int indexChanel ) {
		if (Main.getBanStorage().isMuted(player.getName(), indexChanel)) {
			player.sendMessage(ValueStorage.muteMessage.replace("$1", ""
					+ Main.getBanStorage().getTimeMute(player.getName(), indexChanel)));

			return true;
		}
		return false;
	}
}
