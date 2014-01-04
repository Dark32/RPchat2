package ru.dark32.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
	@EventHandler
	public void onJoin(final PlayerJoinEvent event ) {
		// event.setJoinMessage("");
		if (ValueStorage.motd) {
			final Player player = event.getPlayer();
			for (final String s : ValueStorage.joinmsg) {
				player.sendMessage(ChanelRegister.colorUTF8(s.replace("$suffix", PEXHook.getSuffix(player.getName()))
						.replace("$prefix", PEXHook.getPreffix(player.getName())).replace("$p", player.getName()), 3));

			}
		}

	}
}
