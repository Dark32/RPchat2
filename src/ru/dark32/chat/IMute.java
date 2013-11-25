package ru.dark32.chat;

import org.bukkit.command.CommandSender;

public interface IMute {
	/**
	 * Запрещено ли игроку писать в чате
	 * 
	 * @param playerName
	 *            ник игрока
	 * @param chanel
	 *            канал
	 * @return boolean
	 */
	public boolean isMuted(String playerName, int chanel );

	/**
	 * Запретить игроку писать в чат
	 * 
	 * @param playerName
	 *            ник игрока
	 * @param seconds
	 *            время в секундах
	 * 
	 */

	public void causeMute(String playerName, int[] seconds, String reason );

	/**
	 * Разрешить игроку писать в чат
	 * 
	 * @param playerName
	 *            ник игрока
	 * @param chanel
	 *            каналы
	 */
	public void unmute(String playerName, int[] chanel );

	/**
	 * Метод для безопасного отключения от хранилища при выключении плагина
	 */
	public void saveMute();

	public void mute(String name, String group2, CommandSender sender );

	public long getTimeMute(String playerName, int chanel );
}
