package ru.dark32.chat.ichanels;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IRangeRequisiteItemChanel extends IRangeChanel,IItemChanel {
	/**
	 * @param id ид вещи
	 */
	@Deprecated
	public void setRequiseteItemId(int id );

	/**
	 * @param id суб ид вещи
	 */
	public void setRequiseteItemSubId(int id );

	/**
	 * @param material материал
	 */
	public void setRequiseteItemMaterial(Material material);
	
	/**
	 * @return ид вещи
	 */
	@Deprecated
	public int getRequiseteItemId();

	/**
	 * @return суб ид вещи
	 */
	public int getRequiseteItemSubId();

	/**
	 * @return материал вещи
	 */
	public Material getRequiseteItemMaterial();
	
	/**
	 * установить
	 * 
	 * @param amount
	 *            вещей тратится
	 */
	public void setRequiseteItemAmount(int amount );

	/**
	 * получить
	 * 
	 * @return вещей тратится
	 */
	public int getRequiseteItemAmount();

	/**
	 * @param item требуемая вещь
	 * @return если есть, то истена
	 */
	boolean equalRequiseteItem(ItemStack item );

	/**
	 * @param player игрок, слушатель
	 * @param item проверяемая вещь
	 */
	void loseRequiseteItem(Player player , ItemStack item);
}
