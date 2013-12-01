/**
 * 
 */
package ru.dark32.chat.chanels;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
	public void setRange(int range ) {
		this.range = range;
	}

	@Override
	public int getRange() {
		return this.range;
	}

	@Override
	public List<Player> getRecipients(Player sender ) {
		List<Player> recipients = new LinkedList<Player>();
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			boolean isWorld = isWorldChat() && sender.getWorld() == recipient.getWorld();
			boolean isDeaf = Main.getDeafStorage().isDeaf(recipient.getName(), getIndex());
			int dist = getDist(sender.getLocation(), recipient.getLocation());
			boolean isRange = dist < this.getRange();
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

	private int getDist(Location s, Location t ) {
		return (int) (NumberConversions.square(s.getX() - t.getX())
				+ NumberConversions.square(s.getY() - t.getY()) + NumberConversions.square(s.getZ()
				- t.getZ()));

	}
	@Override
	public String toString() {
		return super.toString() + ", range =>" + this.range;
	}
}
