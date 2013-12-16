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
	final transient private int	chaneles	= ChanelRegister.getChanels();
	final private String		canTHelp;
	final private String		canTSeeSelf;
	final private String		udeafSelf;
	final private String		canTSeeAllDeaf;
	final private String		canTSeeTargetDeaf;
	final private String		signMoreOne;
	final private String		canTUndeafSelf;
	final private String		canTUndeafTarget;
	final private String		canTDeafSelf;
	final private String		canTDeafTarget;
	final private String		deafMessage;
	final private String		noReason;
	final private String		undeafMessage;
	final private String		deafMessage2;

	public Deaf(){
		canTHelp = ChanelRegister.colorize(Main.config.getString("deaf.canTHelp",
				"&7$Вы не можете смотреть справку по глухоте"));
		canTSeeSelf = ChanelRegister.colorize(Main.config.getString("deaf.canTSeeSelf",
				"&7$Вы не можете смотреть свою глухоту"));
		udeafSelf = ChanelRegister.colorize(Main.config.getString("deaf.udeafSelf",
				"&7$Ваша глухота: "));
		canTSeeAllDeaf = ChanelRegister.colorize(Main.config.getString("deaf.canTSeeAllDeaf",
				"&7$Вы не можете смотреть все глухоты"));
		canTSeeTargetDeaf = ChanelRegister.colorize(Main.config.getString("deaf.canTSeeTargetDeaf",
				"&7$Вы не можете смотреть глухоту цели"));
		signMoreOne = ChanelRegister.colorize(Main.config.getString("deaf.signMoreOne",
				"&7$Сокращение канала не может быть длинее 1 символа: $1"));
		canTUndeafSelf = ChanelRegister.colorize(Main.config.getString("deaf.canTUndeafSelf",
				"&7$Вы не можете снять глухоту с себя"));
		canTUndeafTarget = ChanelRegister.colorize(Main.config.getString("deaf.canTUndeafTarget",
				"&7$Вы не можете снять глухоту с другово"));
		canTDeafSelf = ChanelRegister.colorize(Main.config.getString("deaf.canTDeafSelf",
				"&7$Вы не можете устанавливать глухоту себе"));
		canTDeafTarget = ChanelRegister.colorize(Main.config.getString("deaf.canTDeafTarget",
				"&7$Вы не можете устанавливать глухоту другим"));
		deafMessage = ChanelRegister.colorize(Main.config.getString("deaf.deafMessage",
				"&7$$n не слушает $c. Причина: $r"));
		noReason = ChanelRegister.colorize(Main.config.getString("deaf.noReason", "не указана"));
		undeafMessage = ChanelRegister.colorize(Main.config.getString("deaf.undeafMessage",
				"&7$$n теперь слышит канал $c"));
		deafMessage2 = ChanelRegister.colorize(Main.config.getString("deaf.deafMessage2",
				"&7$$n теперь не слушает $c. Причина: $r"));

	}

	private String getPlayerDeafString(final String playerName, final int chanel ) {
		return playerName + ".deaf." + ChanelRegister.getByIndex(chanel).getInnerName();
	}

	@Override
	public void deaf(final String[] args, final CommandSender sender ) {
		final 	boolean hasHelp = Util.hasPermission(sender,  Main.BASE_PERM+".deaf.help");
		final boolean hasSee = Util.hasPermission(sender,  Main.BASE_PERM+".deaf.see");
		final boolean hasAll = Util.hasPermission(sender,  Main.BASE_PERM+".deaf.all");
		final boolean hasSeeSelf = Util.hasPermission(sender,  Main.BASE_PERM+".deaf.see.self") || hasSee;
		final boolean hasDeaf = Util.hasPermission(sender,  Main.BASE_PERM+".deaf.deaf");
		final boolean hasDeafSelf = Util.hasPermission(sender,  Main.BASE_PERM+".deaf.deaf.self") || hasDeaf;
		final boolean hasUnDeaf = Util.hasPermission(sender,  Main.BASE_PERM+".deaf.undeaf");
		final 	boolean hasUnDeafSelf = Util.hasPermission(sender,  Main.BASE_PERM+".deaf.undeaf.self") || hasUnDeaf;
		final String target = args.length > 0 ? args[0] : sender.getName();
		final boolean isSelf = sender.getName().equalsIgnoreCase(target);
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
				sender.sendMessage(ChatColor.GRAY + "$" + target + " :");
				deafSeeTarget(sender, target);
			}
		} else if (args.length > 1) {
			if (args[1].length() != 1) {
				sender.sendMessage(signMoreOne.replace("$1", args[0]));
				return;
			}
			final int chanel = ChanelRegister.getIndexBySign(args[1].charAt(0));
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
			final String reason = args.length >= 3 ? StringUtils.join(args, " ", 2, args.length)
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
	public void deafSeeAll(final CommandSender sender ) {
		final ConfigurationSection cs = Main.yaml.getRoot();
		sender.sendMessage(ChatColor.GRAY + "$===============all================");
		if (cs == null) {
			return;
		}
		final Set<String> list = cs.getKeys(false);
		for (final String name : list) {
			deafSeeTarget(sender, name);
		}
	}

	@Override
	public void deafSeeSelf(final CommandSender sender ) {
		deafSeeTarget(sender, sender.getName());

	}

	@Override
	public void deafSeeTarget(final CommandSender sender, final String name ) {
		for (int i = 0; i < chaneles; i++) {
			final String reason = Main.yaml.getString(getPlayerDeafString(name, i) + "-reason","");
			final boolean isDeaf = this.isDeaf(name, i);
			if (isDeaf) {
				sender.sendMessage(deafMessage.replace("$n", name)
						.replace("$c", ChanelRegister.getByIndex(i).getName())
						.replace("$r", reason));
			}
		}

	}

	private void deafHelp(final CommandSender sender ) {
		final List<String> msg = new ArrayList<String>();
		msg.addAll(ValueStorage.deafHelp);
		for (final String string : msg) {
			sender.sendMessage(ChanelRegister.colorize(string));
		}

	}

	@Override
	public void caseDeaf(final CommandSender sender, final String name, final int chanel, final String reason ) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -1) {
				Main.yaml.set(getPlayerDeafString(name, i), true);
				Main.yaml.set(getPlayerDeafString(name, i) + "-reason", reason);
			}
		}
		sender.sendMessage(deafMessage2
				.replace("$n", name)
				.replace(
						"$c",
						(chanel >= 0 && chanel < this.chaneles ? ChanelRegister.getByIndex(chanel)
								.getName() : "a")).replace("$r", reason));

		saveDeaf();
	}

	@Override
	public void caseUnDeaf(final CommandSender sender,final  String name,final  int chanel ) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -1) {
				Main.yaml.set(getPlayerDeafString(name, i), false);
				Main.yaml.set(getPlayerDeafString(name, i) + "-reason", null);
			}
		}
		sender.sendMessage(undeafMessage.replace("$n", name).replace(
				"$c",
				(chanel >= 0 && chanel < chaneles ? ChanelRegister.getByIndex(chanel).getName() : "a"))

		);
	}

	@Override
	public boolean isDeaf(final String name,final  int chanel ) {
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
