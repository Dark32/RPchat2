package ru.dark32.chat.chanels;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.dark32.chat.ichanels.IBroadChanel;

public class BroadChanel extends BaseChanel implements IBroadChanel {

	private List<String>	paterns;

	@Override
	public List<String> getPatterns() {
		return paterns;
	}

	@Override
	public void setPattern(final List<String> list ) {
		paterns = list;

	}
	@Override
	public String preformatMessage(final Player sender,final String message ) {
		Bukkit.getConsoleSender().sendMessage("BROAD: "+ sender.getName()+":"+message);
		return ChanelRegister.colorize(message);
	}
}
