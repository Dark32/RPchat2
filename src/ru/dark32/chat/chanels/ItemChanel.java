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
	private boolean		requestPprefix;

	@Override
	@Deprecated
	public void setItemId(final int id ) {
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
	public void setMaterial(final Material ma ) {
		this.material = ma;

	}

	@Override
	public int getSubId() {
		return this.subid;
	}

	@Override
	public void setSubId(final int sub ) {
		this.subid = sub;
	}

	@SuppressWarnings("deprecation" )
	@Override
	public void loseItem(Player player ) {
		final ItemStack inHand = player.getItemInHand();
		final int amoutHand = inHand.getAmount() - 1;
		ItemStack inHandrem = null;
		if (ValueStorage.experemental) {
			inHandrem = new ItemStack(inHand.getType(), amoutHand);
		} else {
			inHandrem = new ItemStack(inHand.getTypeId(), amoutHand);
		}
		if (amoutHand <= 0) {
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

	@SuppressWarnings("deprecation" )
	@Override
	public boolean equalItem(final ItemStack item ) {
		if (item == null) {
			return false;
		}

		if (Main.DEBUG_MODE) {
			Bukkit.getConsoleSender().sendMessage(
					"debug inhand " + item.getTypeId() + ":" + item.getDurability() + " - "
							+ item.getType());
			Bukkit.getConsoleSender().sendMessage(
					"debug need " + id + ":" + subid + " - " + material);
		}

		boolean isItem = item.getDurability() == this.subid
				&& (ValueStorage.experemental && item.getType() == this.material)
				|| item.getTypeId() == this.id;
		return isItem;
	}

	@Override
	public boolean isRequestPprefix() {
		return requestPprefix;
	}

	@Override
	public void setRequestPprefix(final boolean need ) {
		requestPprefix = need;

	}
}
