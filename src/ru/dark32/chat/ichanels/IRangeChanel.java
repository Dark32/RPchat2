package ru.dark32.chat.ichanels;

/**
 * @author Andrew
 *  интерфейс канала с ограничением по расстоянию
 */
public interface IRangeChanel extends IChanel {
	/**
	 * @param range расстояние
	 */
	void setRange(int range );

	/**
	 * @return расстояние
	 */
	int getRange();
}
