package ru.dark32.chat.ichanels;

/**
 * @author Andrew
 *  интерфейс канала с ограничением по расстоянию
 */
public interface IRangeChanel extends IChanel {
	/**
	 * @param range расстояние
	 */
	public void setRange(int range );

	/**
	 * @return расстояние
	 */
	public int getRange();
}
