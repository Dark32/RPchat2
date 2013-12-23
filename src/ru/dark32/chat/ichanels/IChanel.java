package ru.dark32.chat.ichanels;

import java.util.List;
import java.util.Set;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;

/**
 * @author Andrew Базовый интерфейс канала
 */
public interface IChanel {

	/**
	 * @return формат канала
	 */
	String getFormat();

	/**
	 * @return индекс канала
	 */
	int getIndex();

	/**
	 * @return имя канала
	 */
	String getName();

	/**
	 * @return префикс канала
	 */
	char getPrefix();

	/**
	 * @return подпись канала
	 */
	char getSign();

	/**
	 * @param key
	 *            фомат канала
	 */
	void setFormat(String key );

	/**
	 * @param i
	 *            индекс канала на кайний случай
	 */
	void setIndex(int i );

	/**
	 * @param key
	 *            имя канала
	 */
	void setName(String key );

	/**
	 * @param key
	 *            префикс канала
	 */
	void setPrefix(String key );

	/**
	 * @param sign
	 *            подпись канала
	 */
	void setSign(char sign );

	/**
	 * @return включен ли канал
	 */
	boolean isEnable();

	/**
	 * @param enbl
	 *            управление включённостью канала
	 */
	void setEnable(boolean enbl );

	/**
	 * @param sender
	 *            отправитель
	 * @return слушатели
	 */
	List<Player> getRecipients(Player sender );

	/**
	 * @return ограничен ли чат одним миром
	 */
	boolean isWorldChat();

	/**
	 * @param isWorld
	 *            ограничен ли чат одним миром
	 */
	void setWorldChat(boolean isWorld );

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
	void setType(ETypeChanel type );

	/**
	 * @return тип чата <br>
	 *         base - базовый<br>
	 *         range - ограничен по радиусу<br>
	 *         item - требует вещь<br>
	 *         pm - личные сообщение<br>
	 *         range-item - требует вещь, ограничен по радиусу<br>
	 *         none - не канал
	 */
	ETypeChanel getType();

	/**
	 * @return внутреннее имя
	 */
	String getInnerName();

	/**
	 * @param name
	 *            внутреннее имя
	 */
	void setInnerName(String name );

	/**
	 * форматировать сообщение
	 * 
	 * @param p
	 *            - игрок
	 * @param msg
	 *            - сообщение
	 * @return - отформатированное сообщение
	 */
	String format(Player p, String msg );

	/**
	 * @param tables
	 *            Табаемо
	 */
	void setTabes(boolean tables );

	/**
	 * @return набаемо ли?
	 */
	boolean isTabes();

	/**
	 * обработка сообщения, не формата
	 * 
	 * @param sender
	 *            отсылающий сообщение
	 * @param message
	 *            сообщение
	 * @return обработанное сообщение
	 */

	String preformatMessage(Player sender, String message );

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
	 *            услышавшие сообщение
	 */
	void preSend(Player sender, String message, Set<Player> recipient );

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
	void setListenerMessage(String listenerMessage, String noListenerMessage, int type );

	/**
	 * @param count
	 *            сколько услышат
	 * 
	 * @return сообщения о числе услышавших
	 */
	String getListenerMessage(int count );

	/**
	 * @return выводить ли сообщения о числе услышавших
	 * 0 - выключено
	 * -1 в сообщение
	 * 1 - отдельно
	 */
	int isListenerMessage();

	/**
	 * нужен ли базовые права для канала
	 * 
	 * @param need
	 *            нужны или нет
	 */
	void setNeedPerm(boolean need );

	/**
	 * @return нужны ли базовые права
	 */
	boolean isNeedPerm();

	/**
	 * @param enable
	 *            включен ли
	 * @param instrument
	 *            инструмент
	 * @param note
	 *            нота
	 */
	void setPimk(boolean enable, Instrument instrument, Note note , String colorize);

	/**
	 * @return включен ли пимк
	 */
	boolean isPimk();

	/**
	 * @return инструмент пимка
	 */
	Instrument getPimkInstrument();

	/**
	 * @return нота пимка
	 */
	Note getPimkNote();
	
	/**
	 * @return nickname selecter
	 */
	String getColorize();
}
