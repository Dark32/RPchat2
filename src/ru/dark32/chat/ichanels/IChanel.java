package ru.dark32.chat.ichanels;

import java.util.List;

import org.bukkit.entity.Player;

import ru.dark32.chat.ChatListener;
import ru.dark32.chat.Main;

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
	public boolean getEnable();

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
}
