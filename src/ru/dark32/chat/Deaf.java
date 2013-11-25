package ru.dark32.chat;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Deaf implements IDeaf {
	private YamlConfiguration yaml;
	final private int chaneles = Chanel.getValues();
	private File yamlFile;

	public Deaf(File file) {
		this.yamlFile = file;
		if (this.yamlFile.exists()) {
			yaml = YamlConfiguration.loadConfiguration(file);
		} else {
			yaml = new YamlConfiguration();
		}
	}

	private String getPlayerDeafString(String playerName, int chanel) {
		return playerName + ".deaf." + Chanel.getByIndex(chanel).getSign();
	}

	@Override
	public void deaf(String[] args, CommandSender sender) {
		boolean isPlayer = (sender instanceof Player);
		boolean hasHelp = isPlayer ? Util.hasPermission((Player) sender,
				"mcnw.deaf.help") : true;
		boolean hasSee = isPlayer ? Util.hasPermission((Player) sender,
				"mcnw.deaf.see") : true;
		boolean hasAll = isPlayer ? Util.hasPermission((Player) sender,
				"mcnw.deaf.all") : true;
		boolean hasSeeSelf = isPlayer ? Util.hasPermission((Player) sender,
				"mcnw.deaf.see.self") || hasSee : true;
		boolean hasDeaf = isPlayer ? Util.hasPermission((Player) sender,
				"mcnw.deaf.deaf") : true;
		boolean hasDeafSelf = isPlayer ? Util.hasPermission((Player) sender,
				"mcnw.deaf.deaf.self") || hasDeaf : true;
		String target = args[0];
		boolean isSelf = sender.getName().equalsIgnoreCase(target);
		if (args.length == 1) {
			if (!hasHelp) {
				sender.sendMessage(ChatColor.GRAY
						+ "%Вы не можете смотреть справку");
				return;
			}
			deafHelp(sender);
		} else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("see")) {
				if (!hasSeeSelf) {
					sender.sendMessage(ChatColor.GRAY
							+ "%Вы не можете смотреть свою глухоту");
					return;
				}
				deafSeeSelf(sender);
			} else if (args[1].equalsIgnoreCase("all")) {
				if (!hasAll) {
					sender.sendMessage(ChatColor.GRAY
							+ "%Вы не можете смотреть все глухоты");
					return;
				}
				deafSeeAll(sender);
			} else {
				if (!hasSee) {
					sender.sendMessage(ChatColor.GRAY
							+ "%Вы не можете смотреть глухоту цели");
					return;
				}
				deafSeeTarget(sender, target);
			}
		} else if (args.length > 2) {
			if (!(hasDeafSelf && isSelf) || !hasDeaf) {
				sender.sendMessage(ChatColor.GRAY
						+ "%Вы не можете устанавливать глухоту "
						+ (isSelf ? "себе" : "другим"));
				return;
			}
			if (args[1].length() != 1) {
				sender.sendMessage(ChatColor.GRAY
						+ "%Сокращение канала не может быть длинее 1 символа: "
						+ args[0]);
				return;
			}
			int chanel = Chanel.getIndexBySign(args[1].charAt(0));
			String reason = args.length >= 3 ? StringUtils.join(args, " ", 2,
					args.length) : "не указана";
			caseDeaf(sender.getName(), chanel, reason);
		}
	}

	private void deafSeeAll(CommandSender sender) {
		ConfigurationSection cs = yaml.getRoot();
		sender.sendMessage(ChatColor.GRAY
				+ "%===============all================");
		if (cs == null) {
			return;
		}
		Set<String> list = cs.getKeys(false);
		sender.sendMessage(list.toString());
		for (String name : list) {
			deafSeeTarget(sender, name);
		}
	}

	private void deafSeeSelf(CommandSender sender) {
		deafSeeTarget(sender, sender.getName());

	}

	private void deafSeeTarget(CommandSender sender, String name) {
		sender.sendMessage(ChatColor.GRAY + "=======" + name + "=======");
		for (int i = 0; i < chaneles; i++) {
			String reason = yaml.getString(getPlayerDeafString(name, i)
					+ "-reason");
			boolean isDeaf = this.isDeaf(name, i);
			if (isDeaf) {
				sender.sendMessage(ChatColor.GRAY + "%"
						+ (Chanel.getByIndex(i).getSign())
						+ " не слушает. Причина: " + ChatColor.UNDERLINE
						+ reason);
			}
		}

	}

	private void deafHelp(CommandSender sender) {
		sender.sendMessage("============Справка============");

	}

	@Override
	public void caseDeaf(String playerName, int chanel, String reason) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -2) {
				yaml.set(getPlayerDeafString(playerName, i), true);
				yaml.set(getPlayerDeafString(playerName, i) + "-reason", reason);
			}
		}
		saveDeaf();
	}

	@Override
	public void caseUnDeaf(String playerName, int chanel) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -2) {
				yaml.set(getPlayerDeafString(playerName, i), null);
				yaml.set(getPlayerDeafString(playerName, i) + "-reason", null);
			}
		}
	}

	@Override
	public boolean isDeaf(String name, int chanel) {
		return yaml.getBoolean(getPlayerDeafString(name, chanel));
	}

	@Override
	public void saveDeaf() {
		try {
			yaml.save(yamlFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
