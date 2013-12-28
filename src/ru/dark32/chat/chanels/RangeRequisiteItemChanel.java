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
	private int			requisiteItemAmount;
	private int			requisiteItemId;
	private Material	requisiteItemMaterial;
	private int			requisiteItemSubId;

	@SuppressWarnings("deprecation" )
	public RangeRequisiteItemChanel(String name ){
		super(name);
		final String path_reqId = "Chat." + name + ".requisete.id";
		final String path_reqSubId = "Chat." + name + ".requisete.subid";
		final String path_reqAmount = "Chat." + name + ".requisete.amount";
		final String path_reqMaterial = "Chat." + name + ".requisete.material";
		this.requisiteItemId = Main.chatConfig.getInt(path_reqId, this.getItemId());
		this.requisiteItemSubId = Main.chatConfig.getInt(path_reqSubId, this.getItemSubId());
		this.requisiteItemAmount = Main.chatConfig.getInt(path_reqAmount, 0);
		this.requisiteItemMaterial = Material.getMaterial(Main.chatConfig.getString(path_reqMaterial, this
				.getItemMaterial().name()));
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
			if (Util.hasPermission(recipient, Main.BASE_PERM + ".spy") && sender!=recipient) {
				DEBUG("debug: spy - " + recipient.getName(), sender);
				recipients.add(recipient);
				continue;
			} else if (isRecipient(sender, recipient)) {
				DEBUG("debug: isn't Recipient - " + recipient.getName(), sender);
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

	@Override
	public int getRequiseteItemAmount() {
		return this.requisiteItemAmount;
	}

	@Deprecated
	@Override
	public int getRequiseteItemId() {
		return requisiteItemId;
	}

	@Override
	public Material getRequiseteItemMaterial() {
		return requisiteItemMaterial;
	}

	@Override
	public int getRequiseteItemSubId() {
		return requisiteItemSubId;
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
