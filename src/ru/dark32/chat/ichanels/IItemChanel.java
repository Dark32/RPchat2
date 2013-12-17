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
	void setItemId(int id );

	/**
	 * @return ид вещи
	 */
	@Deprecated
	int getItemId();

	/**
	 * @return материал вещи
	 */
	Material getItemMaterial();

	/**
	 * @param ma
	 *            материал вещи
	 */
	void setItemMaterial(Material ma );

	/**
	 * @return метадата вещи
	 */
	int getItemSubId();

	/**
	 * @param sub
	 *            метадата вещи
	 */
	void setItemSubId(int sub );

	/**
	 * @param player
	 */
	void loseItem(Player player );

	/**
	 * @param item
	 *            вещь в руках
	 * @return истина, если совпало с вещью канала
	 */
	boolean equalItem(ItemStack item );

	/**
	 * @return обязателен ли префикс (если нет, то достаточно вещи в руках если
	 *         да - то нужно и вещь в руках и префикс)
	 */
	boolean isRequestPprefix();

	/**
	 * @param needобязателен
	 *            ли префикс (если нет, то достаточно вещи в руках если да - то
	 *            нужно и вещь в руках и префикс)
	 */
	void setRequestPprefix(boolean need );

	/**
	 * установить
	 * 
	 * @param amount
	 *            вещей тратится
	 */
	void setItemAmount(int amount );

	/**
	 * получить
	 * 
	 * @return вещей тратится
	 */
	int getItemAmount();
}
