package ru.dark32.chat;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent event ) {
		event.setJoinMessage("");
		Player player = event.getPlayer();
		for (String s : ValueStorage.joinmsg) {
			player.sendMessage(ChatListener.tCC(s
					.replace("%sf", ChatListener.getSuffix(player.getName()))
					.replace("%pf", ChatListener.getPreffix(player.getName()))
					.replace("%p", player.getName())));

		}

	}
}
