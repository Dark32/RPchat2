package ru.dark32.chat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class Deaf implements IDeaf {
	final private int	chaneles	= Chanel.getValues();

	private String getPlayerDeafString(String playerName, int chanel ) {
		return playerName + ".deaf." + Chanel.getByIndex(chanel).getSign();
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
				sender.sendMessage(ChatColor.GRAY + "%Вы не можете смотреть справку по глухоте");
				return;
			}
			deafHelp(sender);
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("see")) {
				if (!hasSeeSelf) {
					sender.sendMessage(ChatColor.GRAY + "%Вы не можете смотреть свою глухоту");
					return;
				}
				sender.sendMessage(ChatColor.GRAY + "%Ваша глухота: ");
				deafSeeSelf(sender);
			} else if (args[0].equalsIgnoreCase("all")) {
				if (!hasAll) {
					sender.sendMessage(ChatColor.GRAY + "%Вы не можете смотреть все глухоты");
					return;
				}
				deafSeeAll(sender);
			} else {
				if (!hasSee) {
					sender.sendMessage(ChatColor.GRAY + "%Вы не можете смотреть глухоту цели");
					return;
				}
				sender.sendMessage(ChatColor.GRAY + "%"+ target+" :");
				deafSeeTarget(sender, target);
			}
		} else if (args.length > 1) {
			if (args[1].length() != 1) {
				sender.sendMessage(ChatColor.GRAY
						+ "%Сокращение канала не может быть длинее 1 символа: " + args[0]);
				return;
			}
			int chanel = Chanel.getIndexBySign(args[1].charAt(0));
			if (args.length > 2 && args[2].equals("undeaf")) {
				if (isSelf) {
					if (!hasUnDeafSelf) {
						sender.sendMessage(ChatColor.GRAY + "%Вы не можете снять глухоту с себя");
						return;
					}
					caseUnDeaf(sender, target, chanel);
					return;
				} else {
					if (!hasUnDeaf) {
						sender.sendMessage(ChatColor.GRAY + "%Вы не можете снять глухоту с другово");
						return;
					}
					caseUnDeaf(sender,target, chanel);
					return;
				}
			}
			String reason = args.length >= 3 ? StringUtils.join(args, " ", 2, args.length): "не указана";
			if (isSelf) {
				if (!hasDeafSelf) {
					sender.sendMessage(ChatColor.GRAY + "%Вы не можете устанавливать глухоту себе");
					return;
				}
				caseDeaf(sender ,target, chanel, reason);
				return;
			} else {
				if (!hasDeaf) {
					sender.sendMessage(ChatColor.GRAY + "%Вы не можете устанавливать глухоту другим");
					return;
				}
				caseDeaf(sender ,target, chanel, reason);;
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
				sender.sendMessage(ChatColor.GRAY + "%" + name + (Chanel.getByIndex(i).getSign())
						+ " не слушает. Причина: " + ChatColor.UNDERLINE + reason);
			}
		}

	}

	private void deafHelp(CommandSender sender ) {
		List<String> msg = new ArrayList<String>();
		msg.addAll(ValueStorage.deafHelp);
		for (String s : msg) {
			sender.sendMessage(ChatListener.tCC(s));
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
		sender.sendMessage(ChatColor.GRAY + "%" + name + " теперь не слушает канал "+ (chanel >= 0 && chanel< Chanel.getValues() ? Chanel.getByIndex(chanel).getSign() : "a"));
		saveDeaf();
	}

	@Override
	public void caseUnDeaf(CommandSender sender,String name, int chanel ) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -2) {
				Main.yaml.set(getPlayerDeafString(name, i), false);
				Main.yaml.set(getPlayerDeafString(name, i) + "-reason", null);
			}
		}
		System.out.print(chanel);
		sender.sendMessage(ChatColor.GRAY + "%"+name+" теперь слышит канал "+(chanel >= 0 && chanel< Chanel.getValues()? Chanel.getByIndex(chanel).getSign() : "a"));
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
