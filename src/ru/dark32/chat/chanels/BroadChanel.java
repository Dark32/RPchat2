package ru.dark32.chat.chanels;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.dark32.chat.ChanelRegister;
import ru.dark32.chat.Main;
import ru.dark32.chat.ichanels.IBroadChanel;

public class BroadChanel extends BaseChanel implements IBroadChanel {
	final private List<String>	paterns;

	public BroadChanel(String name ){
		super(name);
		final String path_pattern = "Chat." + name + ".pattern";
		this.paterns = Main.chatConfig.getStringList(path_pattern);
	}

	@Override
	final public List<String> getPatterns() {
		return paterns;
	}

	@Override
	final public String preformatMessage(final Player sender, final String message ) {
		Bukkit.getConsoleSender().sendMessage("BROAD: " + sender.getName() + ":" + message);
		return ChanelRegister.colorUTF8(message,3);
	}
}
