package ru.dark32.chat;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

public class TabListener implements Listener {
	@EventHandler
	public void tabComplete(PlayerChatTabCompleteEvent e ) {
		e.getTabCompletions().clear();
		String chatMessage = e.getChatMessage();
		Collection<String> completions = e.getTabCompletions();
		char firstChar = chatMessage.charAt(0);
		if (firstChar == ValueStorage.pm.getPrefix()) {
			String _nick = chatMessage.length() > 1 ? chatMessage.substring(1) : "";
			String _name = "";
			for (Player player : Bukkit.getOnlinePlayers()) {
				_name = player.getName();
				if ((_nick.length() > 0 && _name.startsWith(_nick)) || _nick.isEmpty()) {
					completions.add(ValueStorage.pm.getPrefix() + _name);
				}
			}
		}else if (firstChar == ValueStorage.broadcast.getPrefix()) {
			for (String s : ValueStorage.broadList) {
				String broad = chatMessage.length() > 1 ? chatMessage.substring(1) : "";
				if ((broad.length() > 0 && s.startsWith(broad)) || broad.isEmpty()) {
					completions.add(ValueStorage.broadcast.getPrefix() + s);
				}
			}
		} else {
			return;
		}
	}
}
