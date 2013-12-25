package ru.dark32.chat.ichanels;

/**
 * @author Andrew интерфейс канала с ограничением по расстоянию
 */
public interface IRangeChanel extends IChanel {
	/**
	 * @return расстояние
	 */
	int getRange();
}
