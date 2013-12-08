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
	public boolean hasNameTarget(String raw );

	/**
	 * @param raw
	 *            сырая строка
	 * @return имя цели
	 */
	public String getNameTarget(String raw );

	/**
	 * @param raw
	 *            сырая строка
	 * @return если есть сообщение, то индекс начала сообщения, иначе -1
	 */
	public int hasMessage(String raw );

	/**
	 * @param raw
	 *            сырая строка
	 * @param ind
	 *            индекс начала сообщения
	 * @return сообщение
	 */
	public String getMessage(String raw, int ind );

	/**
	 * @param name
	 *            имя цели
	 * @return цель, либо ничто, если цель не найдена
	 */
	public Player getTargetByName(String name );

	/**
	 * @param target
	 *            цель или ничто
	 * @return истина если цель не ничто
	 */
	public boolean hasTarget(Player target ); 

	public Player getTarget(String targetName );

	/**
	 * @param sender
	 *            отправитель
	 * @param raw
	 *            сырая строка
	 */
	public void sendMessage(Player sender, String raw );

	/**
	 * отправить сообщение наблюдателям
	 * 
	 * @param sender
	 *            отправитель
	 * @param msg
	 *            сообщение
	 */
	public void sendSpyMessage(Player sender, Player target,String msg );

	/**
	 * отклик, вывести себе сообщение
	 * 
	 * @param sender
	 *            - отправитель
	 * @param msg
	 *            - сообщение
	 */
	public void responseSendMessage(Player sender, String msg );

	/**
	 * Формат сообщения для получаталя
	 * 
	 * @param formatTo
	 *            формат
	 */
	public void setFormatTo(String formatTo );

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
	public String formatTo(Player sender, Player target, String msg );

	/**
	 * @param formatFrom
	 *            формат сообщения ответа
	 */
	public void setFormatFrom(String formatFrom );

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
	public String formatFrom(Player sender, Player target, String msg );

	/**
	 * @param formatSpy
	 *            формат прослушки
	 */
	public void setFormatSpy(String formatSpy );

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
	public String formatSpy(Player sender, Player target, String msg );

	int getPmSearchNickMode();

	void setPmSearchNickMode(int pmSearchNickMode );

}
