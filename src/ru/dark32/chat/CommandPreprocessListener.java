package ru.dark32.chat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ru.dark32.chat.ichanels.IChanel;

public class CommandPreprocessListener implements Listener {
	public CommandPreprocessListener(){

	}

	@EventHandler
	public void tabComplete(final PlayerCommandPreprocessEvent event ) {
		final Player player = event.getPlayer();
		final String[] args = event.getMessage().split(" ");
		final String msg = event.getMessage();
		Util.DEBUG("DEBUG: " + player.getName() + event.getMessage());
		Set<Player> recipients = new HashSet<Player>();
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			recipients.add(recipient);
		}
	//	AsyncPlayerChatEvent event2 = new AsyncPlayerChatEvent(true, player,
	//			StringUtils.join(args, " ", 1, args.length), recipients);
	//	Bukkit.getServer().getPluginManager().callEvent(event2);
	//	Bukkit.getServer().broadcastMessage(event2.getMessage());

		for (IChanel chanel : ChanelRegister.listChat) {
			if (chanel.getCmdSwitch().length() > 0 && msg.contains(chanel.getCmdSwitch())
					&& Util.setChatMode(player.getName(), chanel.getIndex())) {
				event.setCancelled(true);
			}
			if (chanel.getCmdSend().length() > 0 && msg.contains(chanel.getCmdSend())) {
				if ((!chanel.isNeedPerm() || Main.getPermissionsHandler().hasPermission(player, "mcnw.spy") || Main
						.getPermissionsHandler().hasPermission(player,
								Main.BASE_PERM + "." + chanel.getInnerName() + ".say"))) {
					AsyncPlayerChatEvent event3 = new AsyncPlayerChatEvent(true, player, msg.replaceFirst(
							chanel.getCmdSend(), Character.toString(chanel.getPrefix())), recipients);
					Bukkit.getServer().getPluginManager().callEvent(event3);
					event.setCancelled(true);
				}
			}
		}
		// event.setCancelled(true);

	}

}
