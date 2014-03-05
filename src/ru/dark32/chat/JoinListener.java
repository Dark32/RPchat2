package ru.dark32.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event ) {
		String joinMessage = Main.localeConfig.getString(
				"String.join." + Main.getPermissionsHandler().getGroup(event.getPlayer()), "&e$name join the game");
		if (joinMessage.contains("$name")) {
			joinMessage = joinMessage.replace("$name", event.getPlayer().getName());
		}
		if (joinMessage.contains("$suffix")) {
			joinMessage = joinMessage.replace("$suffix", Main.getPermissionsHandler().getSuffix(event.getPlayer()));
		}
		if (joinMessage.contains("$prefix")) {
			joinMessage = joinMessage.replace("$prefix", Main.getPermissionsHandler().getPrefix(event.getPlayer()));
		}
		if (joinMessage.contains("$p")) {
			joinMessage = joinMessage.replace("$p", "%1$s");
		}
		if (joinMessage.contains("$msg")) {
			joinMessage = joinMessage.replace("$msg", "%2$s");
		}
		if (joinMessage.contains("$id")) {
			String iden = Integer.toHexString(event.getPlayer().getTicksLived() + event.getPlayer().getEntityId());

			joinMessage = joinMessage.replace("$id", iden);
		}
		event.setJoinMessage(ChanelRegister.colorUTF8(joinMessage,3));
		if (ValueStorage.motd) {
			final Player player = event.getPlayer();
			for (final String line : ValueStorage.joinmsg) {
				player.sendMessage(ChanelRegister.colorUTF8(
						line.replace("$suffix", Main.getPermissionsHandler().getSuffix(player))
								.replace("$prefix", Main.getPermissionsHandler().getPrefix(player))
								.replace("$p", player.getName()), 3));

			}
		}

	}
}
