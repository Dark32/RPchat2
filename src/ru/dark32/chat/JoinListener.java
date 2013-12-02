package ru.dark32.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ru.dark32.chat.chanels.ChanelRegister;

public class JoinListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent event ) {
		//event.setJoinMessage("");
		Player player = event.getPlayer();
		for (String s : ValueStorage.joinmsg) {
			player.sendMessage(ChanelRegister.colorize(s
					.replace("%sf", ChanelRegister.getSuffix(player.getName()))
					.replace("%pf", ChanelRegister.getPreffix(player.getName()))
					.replace("%p", player.getName())));

		}

	}
}
