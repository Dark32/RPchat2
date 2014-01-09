package ru.dark32.chat.ichanels;

import java.util.Set;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;

/**
 * @author Andrew Базовый интерфейс канала
 */
public interface IChanel {

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
	 * @return nickname selecter
	 */
	String getColorize();

	/**
	 * @return формат канала
	 */
	String getFormat();

	/**
	 * @return индекс канала
	 */
	int getIndex();

	/**
	 * @return внутреннее имя
	 */
	String getInnerName();

	/**
	 * @param count
	 *            сколько услышат
	 * 
	 * @return сообщения о числе услышавших
	 */
	String getListenerMessage(int count );

	/**
	 * @return имя канала
	 */
	String getName();

	/**
	 * @return инструмент пимка
	 */
	Instrument getPimkInstrument();

	/**
	 * @return нота пимка
	 */
	Note getPimkNote();

	/**
	 * @return префикс канала
	 */
	char getPrefix();

	/**
	 * @param sender
	 *            отправитель
	 * @return слушатели
	 */
	Set<Player> getRecipients(Player sender );

	/**
	 * @return подпись канала
	 */
	char getSign();

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
	 * @return включен ли канал
	 */
	boolean isEnable();

	/**
	 * @return выводить ли сообщения о числе услышавших 0 - выключено -1 в
	 *         сообщение 1 - отдельно
	 */
	int isListenerMessage();

	/**
	 * @return нужны ли базовые права
	 */
	boolean isNeedPerm();

	/**
	 * виден ли канал всегда или только когда ты в нём
	 * 
	 * @return истина - виден, ложь - не виден
	 */
	boolean isOverAll();

	/**
	 * @return включен ли пимк
	 */
	boolean isPimk();

	/**
	 * @return набаемо ли?
	 */
	boolean isTabes();

	/**
	 * @return ограничен ли чат одним миром
	 */
	boolean isWorldChat();

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
	 * @return клановый ли чат
	 */
	boolean isClan();

	/**
	 * @param sender
	 *            отправитель
	 * @return слушатели шпионы
	 */
	Set<Player> getSpyRecipients(Player sender );

	/**
	 * @return союзный ли чат
	 */
	boolean isAlly();
}
