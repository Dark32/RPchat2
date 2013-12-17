/**
 * 
 */
package ru.dark32.chat.ichanels;

import org.bukkit.entity.Player;

/**
 * @author Andrew
 * 
 */
public interface IPersonalMessagesChanel extends IChanel {
	/**
	 * @param raw
	 *            сырая строка
	 * @return есть ли имя
	 */
	boolean hasNameTarget(String raw );

	/**
	 * @param raw
	 *            сырая строка
	 * @return имя цели
	 */
	String getNameTarget(String raw );

	/**
	 * @param raw
	 *            сырая строка
	 * @return если есть сообщение, то индекс начала сообщения, иначе -1
	 */
	int hasMessage(String raw );

	/**
	 * @param raw
	 *            сырая строка
	 * @param ind
	 *            индекс начала сообщения
	 * @return сообщение
	 */
	String getMessage(String raw, int ind );

	/**
	 * @param name
	 *            имя цели
	 * @return цель, либо ничто, если цель не найдена
	 */
	Player getTargetByName(String name );

	/**
	 * @param target
	 *            цель или ничто
	 * @return истина если цель не ничто
	 */
	boolean hasTarget(Player target );

	Player getTarget(String targetName );

	/**
	 * @param sender
	 *            отправитель
	 * @param raw
	 *            сырая строка
	 */
	void sendMessage(Player sender, String raw );

	/**
	 * отправить сообщение наблюдателям
	 * 
	 * @param sender
	 *            отправитель
	 * @param msg
	 *            сообщение
	 */
	void sendSpyMessage(Player sender, Player target, String msg );

	/**
	 * отклик, вывести себе сообщение
	 * 
	 * @param sender
	 *            - отправитель
	 * @param msg
	 *            - сообщение
	 */
	void responseSendMessage(Player sender, String msg );

	/**
	 * Формат сообщения для получаталя
	 * 
	 * @param formatTo
	 *            формат
	 */
	void setFormatTo(String formatTo );

	/**
	 * Форматировать сообщение цели
	 * 
	 * @param sender
	 *            отправитель
	 * @param target
	 *            цель
	 * @param msg
	 *            сообщение
	 * @return отформатированное сообщение
	 */
	String formatTo(Player sender, Player target, String msg );

	/**
	 * @param formatFrom
	 *            формат сообщения ответа
	 */
	void setFormatFrom(String formatFrom );

	/**
	 * Форматировать сообщение отправителя
	 * 
	 * @param sender
	 *            отправитель
	 * @param target
	 *            цель
	 * @param msg
	 *            сообщение
	 * @return отформатированное сообщение
	 **/
	String formatFrom(Player sender, Player target, String msg );

	/**
	 * @param formatSpy
	 *            формат прослушки
	 */
	void setFormatSpy(String formatSpy );

	/**
	 * форматирование сообщения прослушки
	 * 
	 * @param sender
	 *            отправитель
	 * @param target
	 *            цель
	 * @param msg
	 *            сообщение
	 * @return отформатированное сообщение
	 */
	String formatSpy(Player sender, Player target, String msg );

	int getPmSearchNickMode();

	void setPmSearchNickMode(int pmSearchNickMode );

}
