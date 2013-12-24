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
public class RangeChanel extends BaseChanel implements IRangeChanel {
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
		if (Main.DEBUG_MODE) {
			sender.sendMessage("debug:--------------------------------");
		}
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			final boolean isWorld = !isWorldChat() || sender.getWorld() == recipient.getWorld();
			final boolean isDeaf = Main.getDeafStorage().isDeaf(recipient.getName(), getIndex());
			final int dist = getDist(sender.getLocation(), recipient.getLocation());
			final boolean isRange = dist < this.getRange() * this.getRange();
			final boolean isHear = !isNeedPerm()
					|| Util.hasPermission(recipient, Main.BASE_PERM + "." + getInnerName() + ".say")
					|| Util.hasPermission(recipient, Main.BASE_PERM + "." + getInnerName() + ".hear");
			if (Main.DEBUG_MODE) {
				sender.sendMessage("debug: " + recipient.getName() + " | " + dist + "/" + this.range * this.range + "|"
						+ isWorld);
			}
			final boolean isSelf = sender == recipient && isListenerMessage() == COUNT_INCLUDE;
			final boolean isInChanel = isOverAll() && Util.getModeIndex(recipient.getName()) == getIndex();
			if (!isInChanel) {
				continue;
			} else if (isSelf) {
				continue;
			} else if (!isHear) {
				continue;
			} else if (isDeaf) {
				if (Main.DEBUG_MODE) {
					sender.sendMessage("debug: deaf - " + recipient.getName());
				}
				continue;
			} else if (Util.hasPermission(recipient, Main.BASE_PERM + ".spy")) {
				if (Main.DEBUG_MODE) {
					sender.sendMessage("debug: spy - " + recipient.getName());
				}
				recipients.add(recipient);
			} else if (isRange) {
				if (Main.DEBUG_MODE) {
					sender.sendMessage("debug: in range - " + recipient.getName());
				}
				if (isWorld) {
					if (Main.DEBUG_MODE) {
						sender.sendMessage("debug: in world - " + recipient.getName());
					}
					recipients.add(recipient);
				} else {
					if (Main.DEBUG_MODE) {
						sender.sendMessage("debug: out of world - " + recipient.getName());
					}
					continue;
				}
			} else {
				if (Main.DEBUG_MODE) {
					sender.sendMessage("debug: out of range - " + recipient.getName());
				}
				continue;
			}
		}
		return recipients;
	}

	private int getDist(final Location sender, final Location target ) {
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
