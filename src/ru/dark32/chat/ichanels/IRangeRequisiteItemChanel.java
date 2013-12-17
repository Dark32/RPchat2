package ru.dark32.chat.ichanels;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IRangeRequisiteItemChanel extends IRangeChanel, IItemChanel {
	/**
	 * @param id
	 *            ид вещи
	 */
	@Deprecated
	void setRequiseteItemId(int id );

	/**
	 * @param id
	 *            суб ид вещи
	 */
	void setRequiseteItemSubId(int id );

	/**
	 * @param material
	 *            материал
	 */
	void setRequiseteItemMaterial(Material material );

	/**
	 * @return ид вещи
	 */
	@Deprecated
	int getRequiseteItemId();

	/**
	 * @return суб ид вещи
	 */
	int getRequiseteItemSubId();

	/**
	 * @return материал вещи
	 */
	Material getRequiseteItemMaterial();

	/**
	 * установить
	 * 
	 * @param amount
	 *            вещей тратится
	 */
	void setRequiseteItemAmount(int amount );

	/**
	 * получить
	 * 
	 * @return вещей тратится
	 */
	int getRequiseteItemAmount();

	/**
	 * @param item
	 *            требуемая вещь
	 * @return если есть, то истена
	 */
	boolean equalRequiseteItem(ItemStack item );

	/**
	 * @param player
	 *            игрок, слушатель
	 * @param item
	 *            проверяемая вещь
	 */
	void loseRequiseteItem(Player player, ItemStack item );
}
