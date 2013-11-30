package ru.dark32.chat.chanels;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Andrew
 *
 */
public class ChatListener2 implements Listener {
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event ) {
		Player sender = event.getPlayer();
		String message = event.getMessage();
		char firstChar = message.charAt(0);
	}
}
