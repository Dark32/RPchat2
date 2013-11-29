package ru.dark32.chat;

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
			Main.getBanStorage().mute(args, sender);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("unmute")) {
			String[] _args = new String[3];
			_args[0] = args.length>0? args[0]:"empty";
			_args[1] = args.length>1? args[1]: "a";
			_args[2] = "1";
			Main.getBanStorage().mute(_args, sender);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("deaf")) {
			Main.getDeafStorage().deaf(args, sender);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("undeaf")) {
			if (args.length != 2) {
				return false;
			}
			String[] _args = new String[3];
			_args[0] = args[0];
			_args[1] = args[1];
			_args[2] = "undeaf";
			Main.getDeafStorage().deaf(_args, sender);
			return true;
		}
		return false;
	}

}
