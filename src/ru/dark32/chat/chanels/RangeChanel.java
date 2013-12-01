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
public class RangeChanel extends BaseChanel implements IRangeChanel {
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
		Location playerLocation = sender.getLocation();
		List<Player> recipients = new LinkedList<Player>();
		if (Main.DEBUG_MODE) sender.sendMessage("debug:--------------------------------");
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			boolean isWorld = !isWorldChat() || sender.getWorld() == recipient.getWorld();
			boolean isDeaf = Main.getDeafStorage().isDeaf(recipient.getName(), getIndex());
			int dist = getDist(sender.getLocation(), recipient.getLocation());
			boolean isRange = dist < this.getRange() * this.getRange();
			if (Main.DEBUG_MODE)
				sender.sendMessage("debug: " + recipient.getName() + " | " + dist + "/"
						+ this.range * this.range + "|" + isWorld);
			if (isDeaf) {
				if (Main.DEBUG_MODE) sender.sendMessage("debug: deaf - " + recipient.getName());
				continue;
			} else if (Util.hasPermission(recipient, "mcnw.spy")) {
				if (Main.DEBUG_MODE) sender.sendMessage("debug: spy - " + recipient.getName());
				recipients.add(recipient);
			} else if (isRange) {
				if (Main.DEBUG_MODE)
					sender.sendMessage("debug: in range - " + recipient.getName());
				if (isWorld) {
					if (Main.DEBUG_MODE)
						sender.sendMessage("debug: in world - " + recipient.getName());
					recipients.add(recipient);
				} else {
					if (Main.DEBUG_MODE)
						sender.sendMessage("debug: out of world - " + recipient.getName());
					continue;
				}
			} else {
				if (Main.DEBUG_MODE)
					sender.sendMessage("debug: out of range - " + recipient.getName());
				continue;
			}
		}
		return recipients;
	}

	private int getDist(Location s, Location t ) {
		int dx = (int) (s.getX() - t.getX());
		dx *= dx;
		int dy = (int) (s.getY() - t.getY());
		dy *= dy;
		int dz = (int) (s.getZ() - t.getZ());
		dz *= dz;
		return dx + dy + dz;

	}

	@Override
	public String toString() {
		return super.toString() + ", range =>" + this.range;
	}
}
