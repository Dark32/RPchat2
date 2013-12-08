package ru.dark32.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ru.dark32.chat.chanels.ChanelRegister;
import ru.dark32.chat.ichanels.IChanel;

public class RPChatCommandExecutor implements CommandExecutor {
	private String	chanelswitch;
	private String	chanenotfound;
	private String	chanesignmore1;

	public RPChatCommandExecutor(){
		chanelswitch = ChanelRegister.colorize(Main.config.getString("help.changechanel",
				"Канал изменн на $1"));
		chanenotfound = ChanelRegister.colorize(Main.config.getString("help.chanenotfound",
				"Канал не найден"));
		chanesignmore1 = ChanelRegister.colorize(Main.config.getString("help.chanesignmore1",
				"Длина сигны больше 1 знака"));

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args ) {
		if (cmd.getName().equalsIgnoreCase("rpchat")) {
			if (args.length == 0) {
				getBase(sender);
				return true;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("help")) getHelp(sender);
				if (args[0].equalsIgnoreCase("channel")) getChannel(sender);
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
					char sign = args[0].charAt(0);
					int _ind = ChanelRegister.getIndexBySign(sign);
					if (_ind != -1) {
						Util.setChatMode(sender.getName(), _ind);
						sender.sendMessage(chanelswitch.replace("$1",
								ChanelRegister.getByIndex(_ind).getName()));
					} else {
						sender.sendMessage(chanenotfound);
					}
				} else {
					sender.sendMessage(chanesignmore1);
				}
			} else {
				getChannel(sender);
				return true;
			}
		}
		return false;
	}

	private void getChannel(CommandSender sender ) {
		List<String> msg = new ArrayList<String>();
		msg.add("&b=============================================");
		msg.addAll(ValueStorage.helpChannel);
		for (IChanel chanel : ChanelRegister.listChat)
			msg.add("&b" + chanel.getName() + " || " + chanel.getInnerName() + " || "
					+ chanel.getSign()+" || "+chanel.getPrefix() +" || "+ chanel.getType().toString() );
		msg.add("&b=============================================");
		for (String s : msg) {
			sender.sendMessage(ChanelRegister.colorize(s));
		}

	}

	private void getHelp(CommandSender sender ) {
		List<String> msg = new ArrayList<String>();
		msg.add("&b=============================================");
		msg.addAll(ValueStorage.helpHelp);
		msg.add("&b=============================================");
		for (String s : msg) {
			sender.sendMessage(ChanelRegister.colorize(s));
		}
	}

	public static void getBase(CommandSender player ) {
		List<String> msg = new ArrayList<String>();
		msg.add("&b=============================================");
		msg.add("&6" + Main.VERSION);
		msg.add("&6Autors: ufatos, dark32");
		msg.add("&6License: CC-BY-NC-ND");
		msg.add("&6Link: http://goo.gl/KpvB7c");
		msg.addAll(ValueStorage.helpBase);
		msg.add("&b=============================================");
		for (String s : msg) {
			player.sendMessage(ChanelRegister.colorize(s));
		}
	}
}
