package ru.dark32.chat.ichanels;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Andrew Интерфейс канала с требованием вещи
 */
public interface IItemChanel extends IChanel {
	/**
	 * @param id
	 *            Ид вещи
	 */
	@Deprecated
	public void setItemId(int id );

	/**
	 * @return ид вещи
	 */
	@Deprecated
	public int getItemId();

	/**
	 * @return материал вещи
	 */
	public Material getItemMaterial();

	/**
	 * @param ma
	 *            материал вещи
	 */
	public void setItemMaterial(Material ma );

	/**
	 * @return метадата вещи
	 */
	public int getItemSubId();

	/**
	 * @param sub
	 *            метадата вещи
	 */
	public void setItemSubId(int sub );

	/**
	 * @param player
	 */
	public void loseItem(Player player );

	/**
	 * @param item
	 *            вещь в руках
	 * @return истина, если совпало с вещью канала
	 */
	public boolean equalItem(ItemStack item );

	/**
	 * @return обязателен ли префикс (если нет, то достаточно вещи в руках если
	 *         да - то нужно и вещь в руках и префикс)
	 */
	public boolean isRequestPprefix();

	/**
	 * @param needобязателен
	 *            ли префикс (если нет, то достаточно вещи в руках если да - то
	 *            нужно и вещь в руках и префикс)
	 */
	public void setRequestPprefix(boolean need );

	/**
	 * установить
	 * 
	 * @param amount
	 *            вещей тратится
	 */
	public void setItemAmount(int amount );

	/**
	 * получить
	 * 
	 * @return вещей тратится
	 */
	public int getItemAmount();
}