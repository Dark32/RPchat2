package ru.dark32.chat.ichanels;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IRangeRequisiteItemChanel extends IRangeChanel, IItemChanel {

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
