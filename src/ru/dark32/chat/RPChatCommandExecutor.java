package ru.dark32.chat;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RPChatCommandExecutor implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args ) {
		if (cmd.getName().equalsIgnoreCase("rpchat")) {
			ChatListener.getHelp(sender);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("mute")) {
			String _msg = (args.length > 1) ? StringUtils.join(args, " ", 1, args.length) : "see";
			String target = (args.length >= 1) ? target = args[0] : sender.getName();
			Main.getBanStorage().mute(target, _msg, sender);
			return true;
		}
		return false;
	}

}
