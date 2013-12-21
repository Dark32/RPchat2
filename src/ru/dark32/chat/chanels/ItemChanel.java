package ru.dark32.chat.chanels;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ValueStorage;
import ru.dark32.chat.ichanels.IItemChanel;

/**
 * @author Andrew
 * 
 */
public class ItemChanel extends BaseChanel implements IItemChanel {

	private int			itemId;
	private int			itemSubId;
	private Material	itemMaterial;
	private boolean		requestPprefix;
	private int			itemAmount;

	@Override
	@Deprecated
	public void setItemId(final int id ) {
		this.itemId = id;

	}

	@Override
	@Deprecated
	public int getItemId() {
		return this.itemId;
	}

	@Override
	public Material getItemMaterial() {
		return itemMaterial;
	}

	@Override
	public void setItemMaterial(final Material ma ) {
		this.itemMaterial = ma != null ? ma : Material.AIR;

	}

	@Override
	public int getItemSubId() {
		return this.itemSubId;
	}

	@Override
	public void setItemSubId(final int sub ) {
		this.itemSubId = sub;
	}

	@SuppressWarnings("deprecation" )
	@Override
	public void loseItem(final Player player ) {
		final ItemStack inHand = player.getItemInHand();
		if (this.itemAmount == 0) {
			return;
		}
		final int amoutHand = inHand.getAmount() - this.itemAmount;
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
		return super.toString() + ", id =>" + this.itemId + ", subid=>" + this.itemSubId
				+ ", material =>" + this.itemMaterial.name() + ", count =>" + this.getItemAmount();
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
					"debug need " + itemId + ":" + itemSubId + " - " + itemMaterial);
		}

		final boolean isItem = item.getDurability() == this.itemSubId
				&& item.getAmount() >= this.itemAmount
				&& ((ValueStorage.experemental && item.getType() == this.itemMaterial) || item
						.getTypeId() == this.itemId);
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

	@Override
	public void setItemAmount(final int amount ) {
		this.itemAmount = amount;

	}

	@Override
	public int getItemAmount() {
		return itemAmount;
	}

	@Override
	public boolean canSend(final Player sender, final String message ) {
		if (!Util.hasPermission(sender, Main.BASE_PERM + "." + this.getInnerName() + ".no_item")) {
			// если вещь в руках совпала
			if (((IItemChanel) this).equalItem(sender.getItemInHand())) {
				// теряем 1 вещь
				((IItemChanel) this).loseItem(sender);
			} else { // иначе
				// глаголим, что вещи нет
				sender.sendMessage(ValueStorage.nei);
				// event.setCancelled(true);
				return false;
			}
		}
		return true;
	}
}
