package ru.dark32.chat.ichanels;

public interface IChanceChanel extends IRangeChanel {
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

}
