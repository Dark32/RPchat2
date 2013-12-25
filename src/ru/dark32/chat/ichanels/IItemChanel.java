package ru.dark32.chat.ichanels;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Andrew Интерфейс канала с требованием вещи
 */
public interface IItemChanel extends IChanel {
	/**
	 * @param item
	 *            вещь в руках
	 * @return истина, если совпало с вещью канала
	 */
	boolean equalItem(ItemStack item );

	/**
	 * получить
	 * 
	 * @return вещей тратится
	 */
	int getItemAmount();

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
	 * @return метадата вещи
	 */
	int getItemSubId();

	/**
	 * @return обязателен ли префикс (если нет, то достаточно вещи в руках если
	 *         да - то нужно и вещь в руках и префикс)
	 */
	boolean isRequestPprefix();

	/**
	 * @param player
	 */
	void loseItem(Player player );
}
