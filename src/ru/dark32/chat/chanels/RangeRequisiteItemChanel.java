/**
 * 
 */
package ru.dark32.chat.chanels;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ValueStorage;
import ru.dark32.chat.ichanels.IRangeRequisiteItemChanel;

/**
 * @author Andrew
 * 
 */
public class RangeRequisiteItemChanel extends RangeItemChanel implements IRangeRequisiteItemChanel {
	private int			requisiteItemId;
	private int			requisiteItemAmount;
	private int			requisiteItemSubId;
	private Material	requisiteItemMaterial;

	@Override
	public List<Player> getRecipients(final Player sender ) {
		final List<Player> recipients = new LinkedList<Player>();
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			final boolean isWorld = !isWorldChat() || sender.getWorld() == recipient.getWorld();
			final boolean isDeaf = Main.getDeafStorage().isDeaf(recipient.getName(), getIndex());
			final int dist = getDist(sender.getLocation(), recipient.getLocation());
			final boolean isRange = (this.getRange() < 0) || (dist < this.getRange());
			final boolean isTransceiver = Util.hasPermission(recipient, Main.BASE_PERM + "." + getInnerName()
					+ ".no_item")
					|| hasItemInInvetery(recipient);
			final boolean isHear = !isNeedPerm()
					|| Util.hasPermission(recipient, Main.BASE_PERM + "." + getInnerName() + ".say")
					|| Util.hasPermission(recipient, Main.BASE_PERM + "." + getInnerName() + ".hear");
			Bukkit.getConsoleSender().sendMessage(recipient.getName() + "-" + isWorld);
			if (!isHear) {
				continue;
			} else if (isDeaf) {
				continue;
			} else if (Util.hasPermission(recipient, Main.BASE_PERM + ".spy")) {
				recipients.add(recipient);
			} else if (isRange && isTransceiver) {
				if (isWorld) {
					recipients.add(recipient);
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return recipients;
	}

	private boolean hasItemInInvetery(final Player player ) {
		Bukkit.getConsoleSender().sendMessage("-" + player.getName());
		final PlayerInventory inventary = player.getInventory();
		boolean hasItem = false;
		for (final ItemStack item : inventary) {
			hasItem = equalRequiseteItem(item);
			if (hasItem) {
				loseRequiseteItem(player, item);
				return hasItem;
			}
		}
		Bukkit.getConsoleSender().sendMessage("" + hasItem);
		return hasItem;
	}

	@Deprecated
	@Override
	public void setRequiseteItemId(final int id ) {
		this.requisiteItemId = id;

	}

	@Override
	public void setRequiseteItemSubId(final int subId ) {
		this.requisiteItemSubId = subId;

	}

	@Override
	public void setRequiseteItemMaterial(final Material material ) {
		this.requisiteItemMaterial = material;

	}

	@Deprecated
	@Override
	public int getRequiseteItemId() {
		return requisiteItemId;
	}

	@Override
	public int getRequiseteItemSubId() {
		return requisiteItemSubId;
	}

	@Override
	public Material getRequiseteItemMaterial() {
		return requisiteItemMaterial;
	}

	@Override
	public void setRequiseteItemAmount(final int amount ) {
		this.requisiteItemAmount = amount;

	}

	@Override
	public int getRequiseteItemAmount() {
		return this.requisiteItemAmount;
	}

	@SuppressWarnings("deprecation" )
	@Override
	public boolean equalRequiseteItem(final ItemStack item ) {
		if (item == null) {
			return false;
		}

		if (Main.DEBUG_MODE) {
			Bukkit.getConsoleSender().sendMessage(
					"debug inhand " + item.getTypeId() + ":" + item.getDurability() + " - " + item.getType());
			Bukkit.getConsoleSender().sendMessage(
					"debug need " + requisiteItemId + ":" + requisiteItemSubId + " - " + requisiteItemMaterial);
		}

		final boolean isItem = item.getDurability() == this.requisiteItemSubId
				&& item.getAmount() > this.requisiteItemAmount
				&& ((ValueStorage.experemental && item.getType() == this.requisiteItemMaterial) || item.getTypeId() == this.requisiteItemId);
		return isItem;
	}

	@SuppressWarnings("deprecation" )
	@Override
	public void loseRequiseteItem(final Player player, final ItemStack item ) {
		if (this.requisiteItemAmount == 0) {
			return;
		}
		final int amoutHand = item.getAmount() - this.requisiteItemAmount;
		ItemStack inHandrem = null;
		if (ValueStorage.experemental) {
			inHandrem = new ItemStack(item.getType(), amoutHand);
		} else {
			inHandrem = new ItemStack(item.getTypeId(), amoutHand);
		}
		if (amoutHand <= 0) {
			inHandrem = null;
		}
		player.setItemInHand(inHandrem);
		return;
	}
}
