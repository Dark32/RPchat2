/**
 * 
 */
package ru.dark32.chat.chanels;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

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
	public List<Player> getRecipients(Player sender ) {
		final List<Player> recipients = new LinkedList<Player>();
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			final boolean isWorld = isWorldChat() && sender.getWorld() == recipient.getWorld();
			final boolean isDeaf = Main.getDeafStorage().isDeaf(recipient.getName(), getIndex());
			final int dist = getDist(sender.getLocation(), recipient.getLocation());
			final boolean isRange = dist < this.getRange();
			if (isDeaf) {
				continue;
			} else if (Util.hasPermission(recipient, "mcnw.spy")) {
				recipients.add(recipient);
			} else if (isRange) {
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

	protected int getDist(Location sender, Location target ) {
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
