package ru.dark32.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ru.dark32.chat.ichanels.IChanel;

public class RPChatCommandExecutor implements CommandExecutor {
	private String	chanelswitch;
	private String	chanenotfound;
	private String	chanesignmore1;

	public RPChatCommandExecutor(){
		chanelswitch = ChanelRegister.colorUTF8(Main.localeConfig.getString("help.changechanel", "help.changechanel"),
				3);
		chanenotfound = ChanelRegister.colorUTF8(
				Main.localeConfig.getString("help.chanenotfound", "help.chanenotfound"), 3);
		chanesignmore1 = ChanelRegister.colorUTF8(
				Main.localeConfig.getString("help.chanesignmore1", "help.chanesignmore1"), 3);

	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args ) {
		if (cmd.getName().equalsIgnoreCase("rpchat")) {
			if (args.length == 0) {
				RPChatCommandExecutor.getBase(sender);
				return true;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("help")) {
					getHelp(sender);
				}
				if (args[0].equalsIgnoreCase("channel")) {
					getChannel(sender);
				}
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("mute")) {
			Main.getBanStorage().mute(args, sender);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("unmute")) {
			String[] _args = new String[3];
			_args[0] = args.length > 0 ? args[0] : "empty";
			_args[1] = args.length > 1 ? args[1] : "a";
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
		if (cmd.getName().equalsIgnoreCase("sw")) {
			if (args.length == 1) {
				if (args[0].length() == 1) {
					final char sign = args[0].charAt(0);
					final int _ind = ChanelRegister.getIndexBySign(sign);
					if (_ind != -1) {
						Util.setChatMode(sender.getName(), _ind);
						sender.sendMessage(chanelswitch.replace("$1", ChanelRegister.getByIndex(_ind).getName()));
					} else {
						sender.sendMessage(chanenotfound);
					}
				} else {
					sender.sendMessage(chanesignmore1);
				}
			} else {
				getChannel(sender);
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("ignore")) {
			Main.getDeafStorage().deaf(args, sender);
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("unignore")) {
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

	private void getChannel(final CommandSender sender ) {
		final List<String> msg = new ArrayList<String>();
		msg.add("&b=============================================");
		msg.addAll(ValueStorage.helpChannel);
		for (final IChanel chanel : ChanelRegister.listChat) {
			msg.add("&b" + chanel.getName() + " || " + chanel.getInnerName() + " || " + chanel.getSign() + " || "
					+ chanel.getPrefix() + " || " + chanel.getType().toString());
		}
		msg.add("&b=============================================");
		for (final String s : msg) {
			sender.sendMessage(ChanelRegister.colorize(s));
		}

	}

	private void getHelp(final CommandSender sender ) {
		final List<String> msg = new ArrayList<String>();
		msg.add("&b=============================================");
		msg.addAll(ValueStorage.helpHelp);
		msg.add("&b=============================================");
		for (final String s : msg) {
			sender.sendMessage(ChanelRegister.colorize(s));
		}
	}

	public static void getBase(final CommandSender player ) {
		final List<String> msg = new ArrayList<String>();
		msg.add("&b=============================================");
		msg.add("&6" + Main.VERSION);
		msg.add("&6Autors: ufatos, dark32");
		msg.add("&6License: CC-BY-NC-ND");
		// msg.add("&6Link: http://goo.gl/KpvB7c");
		msg.addAll(ValueStorage.helpBase);
		msg.add("&b=============================================");
		for (final String s : msg) {
			player.sendMessage(ChanelRegister.colorize(s));
		}
	}
}
