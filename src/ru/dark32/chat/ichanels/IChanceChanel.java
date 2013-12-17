package ru.dark32.chat.ichanels;

public interface IChanceChanel extends IRangeChanel {
	/**
	 * @param format
	 *            установить формат броска кубика
	 */
	void setFormatRoll(String format );

	/**
	 * формат
	 * 
	 * @param luck
	 *            удача
	 * @param unluck
	 *            неудача
	 */
	void setLuckUnLuck(String luck, String unluck );

	/**
	 * шанс удачи и минимальный кубик
	 * 
	 * @param chance
	 *            шанс
	 * @param min
	 *            кубик
	 */
	void setChance(int chance, int min );

	/**
	 * @return формат броска кубика
	 */
	String getFormatRoll();

	/**
	 * @return удача
	 */
	String getLuck();

	/**
	 * @return неудача
	 */
	String getUnLuck();

	/**
	 * @return минимальныый кубик
	 */
	int getMinRoll();

	/**
	 * @return шанс
	 */
	int getChance();

	String getFormatLuck();

	void setFormatLuck(String format );

}
