package ru.dark32.chat;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import ru.dark32.chat.chanels.ChanelRegister;
import ru.dark32.chat.ichanels.ETypeChanel;
import ru.dark32.chat.ichanels.IBroadChanel;

public class TabListener implements Listener {
	@EventHandler
	public void tabComplete(final PlayerChatTabCompleteEvent e ) {
		e.getTabCompletions().clear();
		final String chatMessage = e.getChatMessage();
		final Collection<String> completions = e.getTabCompletions();
		final char firstChar = chatMessage.charAt(0);
		final int chanind = ChanelRegister.getIndexByPrefix(firstChar);
		if (chanind != -1 && ChanelRegister.getByIndex(chanind).isTabes()) {
			final String _nick = chatMessage.length() > 1 ? chatMessage.substring(1) : "";
			String _name = "";
			for (final Player player : Bukkit.getOnlinePlayers()) {
				_name = player.getName();
				if ((_nick.length() > 0 && _name.startsWith(_nick)) || _nick.isEmpty()) {
					completions.add(firstChar + _name);
				}
			}
		} else if (chanind != -1 && ChanelRegister.getByIndex(chanind).getType() == ETypeChanel.BROAD) {
			final IBroadChanel ch = (IBroadChanel) ChanelRegister.getByIndex(chanind);
			for (final String pattern : ch.getPatterns()) {
				final String broad = chatMessage.length() > 1 ? chatMessage.substring(1) : "";
				if ((broad.length() > 0 && pattern.startsWith(broad)) || broad.isEmpty()) {
					completions.add(firstChar + pattern);
				}
			}
		} else {
			return;
		}
	}
}
