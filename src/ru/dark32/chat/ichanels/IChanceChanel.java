package ru.dark32.chat.ichanels;

public interface IChanceChanel extends IRangeChanel {
	/**
	 * @param format
	 *            установить формат броска кубика
	 */
	public void setFormatRoll(String format );

	/**
	 * формат
	 * 
	 * @param luck
	 *            удача
	 * @param unluck
	 *            неудача
	 */
	public void setLuckUnLuck(String luck, String unluck );

	/**
	 * шанс удачи и минимальный кубик
	 * 
	 * @param chance
	 *            шанс
	 * @param min
	 *            кубик
	 */
	public void setChance(int chance, int min );

	/**
	 * @return формат броска кубика
	 */
	public String getFormatRoll();

	/**
	 * @return удача
	 */
	public String getLuck();

	/**
	 * @return неудача
	 */
	public String getUnLuck();

	/**
	 * @return минимальныый кубик
	 */
	public int getMinRoll();

	/**
	 * @return шанс
	 */
	public int getChance();

	public String getFormatLuck();

	public void setFormatLuck(String format );

}
