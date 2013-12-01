package ru.dark32.chat.ichanels;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Andrew
 *  Интерфейс канала с требованием вещи
 */
public interface IItemChanel extends IChanel {
	/**
	 * @param id Ид вещи
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
	public Material getMaterial();

	/**
	 * @param ma материал вещи
	 */
	public void setMaterial(Material ma );

	/**
	 * @return метадата вещи
	 */
	public int getSubId();

	/**
	 * @param sub метадата вещи
	 */
	public void setSubId(int sub);

	/**
	 * @param player
	 */
	public void loseItem(Player player );
	
	/**
	 * @param item вещь в руках
	 * @return истина, если совпало с вещью канала
	 */
	public boolean equalItem(ItemStack item);
}
