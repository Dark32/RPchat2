package ru.dark32.chat.ichanels;

import java.util.List;

import org.bukkit.entity.Player;

/**
 * @author Andrew Базовый интерфейс канала
 */
public interface IChanel {

	/**
	 * @return формат канала
	 */
	public String getFormat();

	/**
	 * @return индекс канала
	 */
	public int getIndex();

	/**
	 * @return имя канала
	 */
	public String getName();

	/**
	 * @return префикс канала
	 */
	public char getPrefix();

	/**
	 * @return подпись канала
	 */
	public char getSign();

	/**
	 * @param key
	 *            фомат канала
	 */
	public void setFormat(String key );

	/**
	 * @param i
	 *            индекс канала на кайний случай
	 */
	public void setIndex(int i );

	/**
	 * @param key
	 *            имя канала
	 */
	public void setName(String key );

	/**
	 * @param key
	 *            префикс канала
	 */
	public void setPrefix(String key );

	/**
	 * @param sign
	 *            подпись канала
	 */
	public void setSign(char sign );

	/**
	 * @return включен ли канал
	 */
	public boolean isEnable();

	/**
	 * @param enbl
	 *            управление включённостью канала
	 */
	public void setEnable(boolean enbl );

	/**
	 * @param sender
	 *            отправитель
	 * @return слушатели
	 */
	public List<Player> getRecipients(Player sender );

	/**
	 * @return ограничен ли чат одним миром
	 */
	public boolean isWorldChat();

	/**
	 * @param isWorld
	 *            ограничен ли чат одним миром
	 */
	public void setWorldChat(boolean isWorld );

	/**
	 * @param type
	 *            тип чата <br>
	 *            base - базовый<br>
	 *            range - ограничен по радиусу<br>
	 *            item - требует вещь<br>
	 *            pm - личные сообщение<br>
	 *            range-item - требует вещь, ограничен по радиусу<br>
	 *            none - не канал
	 */
	public void setType(ETypeChanel type );

	/**
	 * @return тип чата <br>
	 *         base - базовый<br>
	 *         range - ограничен по радиусу<br>
	 *         item - требует вещь<br>
	 *         pm - личные сообщение<br>
	 *         range-item - требует вещь, ограничен по радиусу<br>
	 *         none - не канал
	 */
	public ETypeChanel getType();

	/**
	 * @return внутреннее имя
	 */
	public String getInnerName();

	/**
	 * @param name
	 *            внутреннее имя
	 */
	public void setInnerName(String name );

	/**
	 * форматировать сообщение
	 * 
	 * @param p
	 *            - игрок
	 * @param msg
	 *            - сообщение
	 * @return - отформатированное сообщение
	 */
	public String format(Player p, String msg );

	/**
	 * @param tables
	 *            Табаемо
	 */
	public void setTabes(boolean tables );

	/**
	 * @return набаемо ли?
	 */
	public boolean isTabes();

	/**
	 * обработка сообщения, не формата
	 * 
	 * @param sender
	 *            отсылающий сообщение
	 * @param message
	 *            сообщение
	 * @return обработанное сообщение
	 */

	public String preformatMessage(Player sender, String message );

	/**
	 * можно ли отправить
	 * 
	 * @param sender
	 *            Отправитель
	 * @param message
	 *            сообщение
	 * @return истина, если можно отправить
	 */
	boolean canSend(Player sender, String message );

	/**
	 * событие до отправки сообщения
	 * 
	 * @param sender
	 *            Отправитель
	 * @param message
	 *            сообщение
	 * @param recipient
	 *            число услышащих сообщение
	 */
	void preSend(Player sender, String message, int recipient );

	/**
	 * установка сообщения о числе услышавших
	 * 
	 * @param listenerMessage
	 *            сообщение если услышали
	 * @param noListenerMessage
	 *            сообщение если не услышали
	 * @param enable
	 *            выводить ли
	 */
	void setListenerMessage(String listenerMessage, String noListenerMessage, boolean enable );

	/**
	 * @param count
	 *            сколько услышат
	 * 
	 * @return сообщения о числе услышавших
	 */
	String getListenerMessage(int count );

	/**
	 * @return выводить ли сообщения о числе услышавших
	 */
	boolean isListenerMessage();
}
