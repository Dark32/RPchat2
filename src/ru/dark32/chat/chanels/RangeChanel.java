package ru.dark32.chat.chanels;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import ru.dark32.chat.Main;
import ru.dark32.chat.ichanels.IRangeChanel;

/**
 * @author Andrew
 * 
 */
public class RangeChanel extends BaseChanel implements IRangeChanel {
	private int	range;

	public RangeChanel(String name ){
		super(name);
		final String path_range = "Chat." + name + ".range";
		this.range = Main.chatConfig.getInt(path_range, 200);

	}

	@Override
	public int getRange() {
		return this.range;
	}

	@Override
	public Set<Player> getRecipients(final Player sender ) {
		final Set<Player> recipients = new HashSet<Player>();
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			final boolean isWorld = !isWorldChat() || sender.getWorld() == recipient.getWorld();
			final int dist = getDist(sender.getLocation(), recipient.getLocation());
			final boolean isRange = dist < this.getRange();
			DEBUG("debug: " + recipient.getName() + " | " + dist + "/" + this.range + "|" + isWorld,
					sender);
			/*if (Util.hasPermission(recipient, Main.BASE_PERM + ".spy") && sender!=recipient) {
				DEBUG("debug: spy - " + recipient.getName(), sender);
				recipients.add(recipient);
				continue;
			} else */
			if (isRecipient(sender, recipient)) {
				DEBUG("debug: isn't Recipient - " + recipient.getName(), sender);
				continue;
			} else  if (isRange) {
				DEBUG("debug: in range - " + recipient.getName(), sender);
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

	private int getDist(final Location sender, final Location target ) {
		int distX = (int) (sender.getX() - target.getX());
		distX *= distX;
		int distY = (int) (sender.getY() - target.getY());
		distY *= distY;
		int distZ = (int) (sender.getZ() - target.getZ());
		distZ *= distZ;
		return (int)Math.sqrt( distX + distY + distZ);

	}

	@Override
	public String toString() {
		return super.toString() + ", range =>" + this.range;
	}
}
