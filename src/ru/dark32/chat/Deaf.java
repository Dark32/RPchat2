package ru.dark32.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import ru.dark32.chat.chanels.ChanelRegister;

public class Deaf implements IDeaf {
	final private int	chaneles	= ChanelRegister.getChanels();
	private String		canTHelp;
	private String		canTSeeSelf;
	private String		udeafSelf;
	private String		canTSeeAllDeaf;
	private String		canTSeeTargetDeaf;
	private String		signMoreOne;
	private String		canTUndeafSelf;
	private String		canTUndeafTarget;
	private String		canTDeafSelf;
	private String		canTDeafTarget;
	private String		deafMessage;
	private String		noReason;
	private String		undeafMessage;

	public Deaf(){
		canTHelp = ChanelRegister.colorize(Main.config.getString("mute.canTHelp",
				"&7%Вы не можете смотреть справку по глухоте"));
		canTSeeSelf = ChanelRegister.colorize(Main.config.getString("mute.canTSeeSelf",
				"&7%Вы не можете смотреть свою глухоту"));
		udeafSelf = ChanelRegister.colorize(Main.config.getString("mute.udeafSelf",
				"&7%Ваша глухота: "));
		canTSeeAllDeaf = ChanelRegister.colorize(Main.config.getString("mute.canTSeeAllDeaf",
				"&7%Вы не можете смотреть все глухоты"));
		canTSeeTargetDeaf = ChanelRegister.colorize(Main.config.getString("mute.canTSeeTargetDeaf",
				"&7%Вы не можете смотреть глухоту цели"));
		signMoreOne = ChanelRegister.colorize(Main.config.getString("mute.signMoreOne",
				"&7%Сокращение канала не может быть длинее 1 символа: $1"));
		canTUndeafSelf = ChanelRegister.colorize(Main.config.getString("mute.canTUndeafSelf",
				"&7%Вы не можете снять глухоту с себя"));
		canTUndeafTarget = ChanelRegister.colorize(Main.config.getString("mute.canTUndeafTarget",
				"&7%Вы не можете снять глухоту с другово"));
		canTDeafSelf = ChanelRegister.colorize(Main.config.getString("mute.canTDeafSelf",
				"&7%Вы не можете устанавливать глухоту себе"));
		canTDeafTarget = ChanelRegister.colorize(Main.config.getString("mute.canTDeafTarget",
				"&7%Вы не можете устанавливать глухоту другим"));
		deafMessage = ChanelRegister.colorize(Main.config.getString("mute.deafMessage",
				"&7%%n не слушает %c. Причина: %r"));
		noReason = ChanelRegister.colorize(Main.config.getString("mute.noReason", "не указана"));
		undeafMessage = ChanelRegister.colorize(Main.config.getString("mute.undeafMessage",
				"&7%%n теперь слышит канал %c"));
	}

	private String getPlayerDeafString(String playerName, int chanel ) {
		return playerName + ".deaf." + ChanelRegister.getByIndex(chanel).getInnerName();
	}

	@Override
	public void deaf(String[] args, CommandSender sender ) {
		boolean hasHelp = Util.hasPermission(sender, "mcnw.deaf.help");
		boolean hasSee = Util.hasPermission(sender, "mcnw.deaf.see");
		boolean hasAll = Util.hasPermission(sender, "mcnw.deaf.all");
		boolean hasSeeSelf = Util.hasPermission(sender, "mcnw.deaf.see.self") || hasSee;
		boolean hasDeaf = Util.hasPermission(sender, "mcnw.deaf.deaf");
		boolean hasDeafSelf = Util.hasPermission(sender, "mcnw.deaf.deaf.self") || hasDeaf;
		boolean hasUnDeaf = Util.hasPermission(sender, "mcnw.deaf.undeaf");
		boolean hasUnDeafSelf = Util.hasPermission(sender, "mcnw.deaf.undeaf.self") || hasUnDeaf;
		String target = args.length > 0 ? args[0] : sender.getName();
		boolean isSelf = sender.getName().equalsIgnoreCase(target);
		if (args.length == 0) {
			if (!hasHelp) {
				sender.sendMessage(canTHelp);
				return;
			}
			deafHelp(sender);
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("see")) {
				if (!hasSeeSelf) {
					sender.sendMessage(canTSeeSelf);
					return;
				}
				sender.sendMessage(udeafSelf);
				deafSeeSelf(sender);
			} else if (args[0].equalsIgnoreCase("all")) {
				if (!hasAll) {
					sender.sendMessage(canTSeeAllDeaf);
					return;
				}
				deafSeeAll(sender);
			} else {
				if (!hasSee) {
					sender.sendMessage(canTSeeTargetDeaf);
					return;
				}
				sender.sendMessage(ChatColor.GRAY + "%" + target + " :");
				deafSeeTarget(sender, target);
			}
		} else if (args.length > 1) {
			if (args[1].length() != 1) {
				sender.sendMessage(signMoreOne.replace("$1", args[0]));
				return;
			}
			int chanel = ChanelRegister.getIndexBySign(args[1].charAt(0));
			if (args.length > 2 && args[2].equals("undeaf")) {
				if (isSelf) {
					if (!hasUnDeafSelf) {
						sender.sendMessage(canTUndeafSelf);
						return;
					}
					caseUnDeaf(sender, target, chanel);
					return;
				} else {
					if (!hasUnDeaf) {
						sender.sendMessage(canTUndeafTarget);
						return;
					}
					caseUnDeaf(sender, target, chanel);
					return;
				}
			}
			String reason = args.length >= 3 ? StringUtils.join(args, " ", 2, args.length)
					: noReason;
			if (isSelf) {
				if (!hasDeafSelf) {
					sender.sendMessage(canTDeafSelf);
					return;
				}
				caseDeaf(sender, target, chanel, reason);
				return;
			} else {
				if (!hasDeaf) {
					sender.sendMessage(canTDeafTarget);
					return;
				}
				caseDeaf(sender, target, chanel, reason);;
				return;
			}
		}
	}

	@Override
	public void deafSeeAll(CommandSender sender ) {
		ConfigurationSection cs = Main.yaml.getRoot();
		sender.sendMessage(ChatColor.GRAY + "%===============all================");
		if (cs == null) {
			return;
		}
		Set<String> list = cs.getKeys(false);
		for (String name : list) {
			deafSeeTarget(sender, name);
		}
	}

	@Override
	public void deafSeeSelf(CommandSender sender ) {
		deafSeeTarget(sender, sender.getName());

	}

	@Override
	public void deafSeeTarget(CommandSender sender, String name ) {
		for (int i = 0; i < chaneles; i++) {
			String reason = Main.yaml.getString(getPlayerDeafString(name, i) + "-reason");
			boolean isDeaf = this.isDeaf(name, i);
			if (isDeaf) {
				sender.sendMessage(deafMessage.replace("%n", name)
						.replace("%c", ChanelRegister.getByIndex(i).getName())
						.replace("%r", reason));
			}
		}

	}

	private void deafHelp(CommandSender sender ) {
		List<String> msg = new ArrayList<String>();
		msg.addAll(ValueStorage.deafHelp);
		for (String s : msg) {
			sender.sendMessage(ChanelRegister.colorize(s));
		}

	}

	@Override
	public void caseDeaf(CommandSender sender, String name, int chanel, String reason ) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -2) {
				Main.yaml.set(getPlayerDeafString(name, i), true);
				Main.yaml.set(getPlayerDeafString(name, i) + "-reason", reason);
			}
		}
		sender.sendMessage(ChatColor.GRAY
				+ "%"
				+ name
				+ " теперь не слушает канал "
				+ (chanel >= 0 && chanel < this.chaneles ? ChanelRegister.getByIndex(chanel)
						.getName() : "a"));
		saveDeaf();
	}

	@Override
	public void caseUnDeaf(CommandSender sender, String name, int chanel ) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -2) {
				Main.yaml.set(getPlayerDeafString(name, i), false);
				Main.yaml.set(getPlayerDeafString(name, i) + "-reason", null);
			}
		}
		sender.sendMessage(undeafMessage.replace("%n", name).replace(
				"%c",
				(chanel >= 0 && chanel < chaneles ? ChanelRegister.getByIndex(chanel).getName()
						: "a"))

		);
	}

	@Override
	public boolean isDeaf(String name, int chanel ) {
		return Main.yaml.getBoolean(getPlayerDeafString(name, chanel));
	}

	@Override
	public void saveDeaf() {
		try {
			Main.yaml.save(Main.yamlFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
