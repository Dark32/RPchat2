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

	@SuppressWarnings("deprecation" )
	public RangeRequisiteItemChanel(String name ){
		super(name);
		this.setRange(Main.chatConfig.getInt("Chat." + name + ".range"));
		this.setRequiseteItemId(Main.chatConfig.getInt("Chat." + name + ".requisete.id", this.getItemId()));
		this.setRequiseteItemSubId(Main.chatConfig.getInt("Chat." + name + ".requisete.subid", this.getItemSubId()));
		this.setRequiseteItemAmount(Main.chatConfig.getInt("Chat." + name + ".requisete.amount", 0));
		this.setRequiseteItemMaterial(Material.getMaterial(Main.chatConfig.getString("Chat." + name
				+ ".requisete.material", this.getItemMaterial().name())));
	}

	@Override
	public List<Player> getRecipients(final Player sender ) {
		final List<Player> recipients = new LinkedList<Player>();
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			final boolean isWorld = !isWorldChat() || sender.getWorld() == recipient.getWorld();
			final int dist = getDist(sender.getLocation(), recipient.getLocation());
			final boolean isRange = (this.getRange() == 0) || (dist < this.getRange());
			final boolean isTransceiver = Util.hasPermission(recipient, Main.BASE_PERM + "." + getInnerName()
					+ ".no_item")
					|| recipient == sender || hasItemInInvetery(recipient);
			DEBUG("debug: " + recipient.getName() + " | " + dist + "/" + this.getRange() * this.getRange() + "|"
					+ isWorld, sender);
			if (isRecipient(sender, recipient)) {
				continue;
			} else if (Util.hasPermission(recipient, Main.BASE_PERM + ".spy")) {
				DEBUG("debug: spy - " + recipient.getName(), sender);
				recipients.add(recipient);
			} else if (isRange && isTransceiver) {
				if (isWorld) {
					DEBUG("debug: in world - " + recipient.getName(), sender);
					recipients.add(recipient);
				} else {
					DEBUG("debug: out of world - " + recipient.getName(), sender);
					continue;
				}
			} else {
				DEBUG("debug: out of range - " + recipient.getName(), sender);
				continue;
			}
		}
		return recipients;
	}

	private boolean hasItemInInvetery(final Player player ) {
		final PlayerInventory inventary = player.getInventory();
		boolean hasItem = false;
		for (final ItemStack item : inventary) {
			hasItem = equalRequiseteItem(item);
			if (hasItem) {
				loseRequiseteItem(player, item);
				return hasItem;
			}
		}
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
		DEBUG("debug inhand " + item.getTypeId() + ":" + item.getDurability() + " - " + item.getType());
		DEBUG("debug need " + requisiteItemId + ":" + requisiteItemSubId + " - " + requisiteItemMaterial);
		final boolean isItem = item.getDurability() == this.requisiteItemSubId
				&& item.getAmount() >= this.requisiteItemAmount
				&& ((ValueStorage.experemental && item.getType() == this.requisiteItemMaterial) || item.getTypeId() == this.requisiteItemId);
		return isItem;
	}

	@SuppressWarnings("deprecation" )
	@Override
	public void loseRequiseteItem(final Player player, final ItemStack item ) {
		if (this.requisiteItemAmount == 0) {
			return;
		}
		ItemStack loseItem = null;
		if (ValueStorage.experemental) {
			loseItem = new ItemStack(item.getType(), this.requisiteItemAmount);
		} else {
			loseItem = new ItemStack(item.getTypeId(), this.requisiteItemAmount);
		}
		player.getInventory().removeItem(loseItem);
		return;
	}
}
