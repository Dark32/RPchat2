/**
 * 
 */
package ru.dark32.chat.chanels;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ru.dark32.chat.Main;
import ru.dark32.chat.ValueStorage;
import ru.dark32.chat.ichanels.IItemChanel;

/**
 * @author Andrew
 * 
 */
public class ItemChanel extends BaseChanel implements IItemChanel {

	private int			id;
	private int			subid;
	private Material	material;

	@Override
	@Deprecated
	public void setItemId(int id ) {
		this.id = id;

	}

	@Override
	@Deprecated
	public int getItemId() {
		return this.id;
	}

	@Override
	public Material getMaterial() {
		return material;
	}

	@Override
	public void setMaterial(Material ma ) {
		this.material = ma;

	}

	@Override
	public int getSubId() {
		return this.subid;
	}

	@Override
	public void setSubId(int sub ) {
		this.subid = sub;
	}

	@Override
	public void loseItem(Player player ) {
		ItemStack inHand = player.getItemInHand();
		int AmoutHand = inHand.getAmount() - 1;
		ItemStack inHandrem = null;
		if (ValueStorage.experemental) {
			inHandrem = new ItemStack(inHand.getType(), inHand.getAmount() - 1);
		} else {
			inHandrem = new ItemStack(inHand.getTypeId(), inHand.getAmount() - 1);
		}
		if (AmoutHand <= 0) {
			inHandrem = null;
		}
		player.setItemInHand(inHandrem);
		return;
	}

	@Override
	public String toString() {
		return super.toString() + ", id =>" + this.id + ", subid=>" + this.subid + ", material =>"
				+ this.material.name();
	}

	@Override
	public boolean equalItem(ItemStack item ) {
		if (Main.DEBUG_MODE) {
			Bukkit.getConsoleSender().sendMessage(
					"debug inhand " + item.getTypeId() + ":" + item.getDurability() + " - "
							+ item.getType());
			Bukkit.getConsoleSender().sendMessage(
					"debug need " + id + ":" + subid + " - " + material);
		}
		return item != null
				&& item.getDurability() == this.subid
				&& (ValueStorage.experemental ? item.getType() == this.material
						: item.getTypeId() == this.id);
	}
}
