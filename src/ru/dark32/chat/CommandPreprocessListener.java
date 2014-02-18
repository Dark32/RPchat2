package ru.dark32.chat;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandPreprocessListener implements Listener {
	public CommandPreprocessListener(){

	}

	@EventHandler
	public void tabComplete(final PlayerCommandPreprocessEvent event ) {
		final Player player = event.getPlayer();
		final String[] args = event.getMessage().split(" ");
		final String cmd = args[0];
		Bukkit.getConsoleSender().sendMessage("DEBUG: " + player.getName() + event.getMessage());
		Set<Player> recipients = new HashSet<Player>();
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			recipients.add(recipient);
		}
		AsyncPlayerChatEvent event2 = new AsyncPlayerChatEvent(true, player,
				StringUtils.join(args, " ", 1, args.length), recipients);
		Bukkit.getServer().getPluginManager().callEvent(event2);
		Bukkit.getServer().broadcastMessage(event2.getMessage());
		// event.setCancelled(true);

	}

}
