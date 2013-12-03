/**
 * 
 */
package ru.dark32.chat.chanels;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ichanels.IRangeChanel;

/**
 * @author Andrew
 * 
 */
public class RangeRequisiteItemChanel extends RangeItemChanel implements IRangeChanel {
	@Override
	public List<Player> getRecipients(Player sender ) {
		List<Player> recipients = new LinkedList<Player>();
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			boolean isWorld = isWorldChat() && sender.getWorld() == recipient.getWorld();
			boolean isDeaf = Main.getDeafStorage().isDeaf(recipient.getName(), getIndex());
			int dist = getDist(sender.getLocation(), recipient.getLocation());
			boolean isRange = (this.getRange() <= 0) || (dist < this.getRange());
			boolean isTransceiver = Util.hasPermission(recipient, "mcnw." + getInnerName()
					+ ".no_item")
					|| hasItemInInvetery(recipient);
			if (isDeaf) {
				continue;
			} else if (Util.hasPermission(recipient, "mcnw.spy")) {
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

	/*
	 * Ничего не делаем. Рацию не едим при сообщениях :)
	 */
	@Override
	public void loseItem(Player player ) {
		return;
	}

	private boolean hasItemInInvetery(Player player ) {
		PlayerInventory inventary = player.getInventory();
		boolean hasItem = false;
		for (ItemStack item : inventary) {
			hasItem = equalItem(item);
			if (hasItem) {
				return hasItem;
			}
		}
		return hasItem;
	}
}
