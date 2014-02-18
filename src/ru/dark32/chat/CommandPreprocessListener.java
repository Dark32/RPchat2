package ru.dark32.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandPreprocessListener implements Listener {
	public CommandPreprocessListener(){

	}

	@EventHandler
	public void tabComplete(final PlayerCommandPreprocessEvent event ) {
		final Player player = event.getPlayer();
		final String[] args = event.getMessage().split(" ");
		final String cmd = args[0];
		Bukkit.getConsoleSender().sendMessage("DEBUG: "+player.getName() + event.getMessage());
		// event.setCancelled(true);

	}

}
