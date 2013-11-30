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
	 * @param raw  сырая строка
	 * @param _ind индекс начала сообщения
	 * @return сообщение
	 */
	public String getMessage(String raw, int _ind );

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

	public void responseSendMessage(Player sender );

	public void sendSpyMessage(Player sender );

}
