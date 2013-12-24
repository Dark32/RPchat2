/**
 * 
 */
package ru.dark32.chat.chanels;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ichanels.IRangeChanel;

/**
 * @author Andrew
 * 
 */
public class RangeItemChanel extends ItemChanel implements IRangeChanel {

	private int	range;

	@Override
	public void setRange(final int range ) {
		this.range = range;
	}

	@Override
	public int getRange() {
		return this.range;
	}

	@Override
	public List<Player> getRecipients(final Player sender ) {
		final List<Player> recipients = new LinkedList<Player>();
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			final boolean isWorld = isWorldChat() && sender.getWorld() == recipient.getWorld();
			final int dist = getDist(sender.getLocation(), recipient.getLocation());
			final boolean isRange = (this.getRange() < 0) || (dist < this.getRange());
			DEBUG("debug: " + recipient.getName() + " | " + dist + "/" + this.range * this.range + "|" + isWorld,
					sender);
			if (isRecipient(sender, recipient)) {
				continue;
			} else if (Util.hasPermission(recipient, Main.BASE_PERM + ".spy")) {
				DEBUG("debug: spy - " + recipient.getName(), sender);
				recipients.add(recipient);
			} else if (isRange) {
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

	protected int getDist(final Location sender, final Location target ) {
		int distX = (int) (sender.getX() - target.getX());
		distX *= distX;
		int distY = (int) (sender.getY() - target.getY());
		distY *= distY;
		int distZ = (int) (sender.getZ() - target.getZ());
		distZ *= distZ;
		return distX + distY + distZ;

	}

	@Override
	public String toString() {
		return super.toString() + ", range =>" + this.range;
	}
}
