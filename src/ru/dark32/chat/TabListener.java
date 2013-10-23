package ru.dark32.chat;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

public class TabListener implements Listener{
	@EventHandler
	public void tabComplete(PlayerChatTabCompleteEvent e ) {
		e.getTabCompletions().clear();
		String chatMessage = e.getChatMessage();
		Collection<String> completions = e.getTabCompletions();
		char firstChar = chatMessage.charAt(0);
		if (firstChar == ChatMode.PM.getFirstChar()) {
			String _nick = chatMessage.length() > 1 ? chatMessage.substring(1) : "";
			String _name = "";
			for (Player player : Bukkit.getOnlinePlayers()) {
				_name = player.getName();
				if ((_nick.length() > 0 && _name.startsWith(_nick)) || _nick.isEmpty()) {
					completions.add(ChatMode.PM.getFirstLetter() + _name);
				}
			}
		} else if (firstChar == '%') {
			String _nick = chatMessage.length() > 1 ? chatMessage.substring(1) : "";
			String _name = "";
			for (Player player : Bukkit.getOnlinePlayers()) {
				_name = player.getName();
				if ((_nick.length() > 0 && _name.startsWith(_nick)) || _nick.isEmpty()) {
					completions.add('%' + _name);
				}
			}
		} else if (firstChar == ChatMode.BROADCAST.getFirstChar()) {
			for (String s : ValueStorage.broadcastList) {
				String broad = chatMessage.length() > 1 ? chatMessage.substring(1) : "";
				if ((broad.length() > 0 && s.startsWith(broad)) || broad.isEmpty()) {
					completions.add(ChatMode.BROADCAST.getFirstLetter() + s);
				}
			}
		} else {
			return;
		}
	}
}
