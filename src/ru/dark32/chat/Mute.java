package ru.dark32.chat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import ru.dark32.chat.chanels.ChanelRegister;

public class Mute implements IMute {
	private final SimpleDateFormat	SDF			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	final private int				chaneles	= Chanel.getValues();

	private String getPlayerMuteString(String playerName, int chanel ) {
		return playerName + ".mute." + Chanel.getByIndex(chanel).getSign();
	}

	@Override
	public boolean isMuted(String playerName, int chanel ) {
		if (getTimeMute(playerName, chanel) > 0) {
			return true;
		} else {
			unmute(playerName, chanel);
			return false;
		}
	}

	@Override
	public void caseMute(CommandSender sender, String name, int chanel, int time, String reason ) {
		if (name.equals("empty")) {
			sender.sendMessage(ChatColor.GRAY + "%Вы должны указать имя");
			return;
		}
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -2) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, time);
				Main.yaml.set(getPlayerMuteString(name, i), SDF.format(cal.getTime()));
				Main.yaml.set(getPlayerMuteString(name, i) + "-reason", reason);
			}
		}
		if (time > 5) {
			sender.sendMessage(ChatColor.GRAY + "%" + name + " теперь молчит в "
					+ (chanel >= 0 && chanel< Chanel.getValues() ? Chanel.getByIndex(chanel).getSign() : "a") + " на " + time
					+ " секунд");
		} else {
			sender.sendMessage(ChatColor.GRAY + "%" + name + " теперь может говорить в "
					+ (chanel >= 0 && chanel< Chanel.getValues()? Chanel.getByIndex(chanel).getSign() : "a"));
		}
		saveMute();

	}

	@Override
	public void mute(String[] args, CommandSender sender ) {
		boolean hasHelp = Util.hasPermission(sender, "mcnw.mute.help");
		boolean hasSee = Util.hasPermission(sender, "mcnw.mute.see");
		boolean hasAll = Util.hasPermission(sender, "mcnw.mute.all");
		boolean hasSeeSelf = Util.hasPermission(sender, "mcnw.mute.see.self") || hasSee;
		boolean hasMute = Util.hasPermission(sender, "mcnw.mute.mute");
		boolean hasUnMute = Util.hasPermission(sender, "mcnw.mute.unmute");
		if (args.length == 0) {
			if (!hasHelp) {
				sender.sendMessage(ChatColor.GRAY + "%Вы не можете смотреть справку");
				return;
			}
			muteHelp(sender);
		} else if (args.length == 1) {
			String par = args[0];
			if (par.equals("see")) {
				if (!hasSeeSelf) {
					sender.sendMessage(ChatColor.GRAY + "%Вы не можете смотреть свою молчанку");
					return;
				}
				muteSeeSelf(sender);
			} else if (par.equals("all")) {
				if (!hasAll) {
					sender.sendMessage(ChatColor.GRAY + "%Вы не можете смотреть список молчащих");
					return;
				}
				muteSeeAll(sender);
			} else {
				if (!hasSee) {
					sender.sendMessage(ChatColor.GRAY + "%Вы не можете смотреть молчанку игрока");
					return;
				}
				sender.sendMessage(ChatColor.GRAY + "%" + par + " :");
				muteSeeTarget(sender, par);
			}
		} else if (args.length == 2) {
			String nick = args[0];
			if (args[1].equals("see")) {
				if (nick.equals(sender.getName())) {
					if (!hasSeeSelf) {
						sender.sendMessage(ChatColor.GRAY + "%Вы не можете смотреть свою молчанку");
						return;
					}
					muteSeeSelf(sender);
				} else {
					if (!hasSee) {
						sender.sendMessage(ChatColor.GRAY
								+ "%Вы не можете смотреть молчанку игрока");
						return;
					}
					muteSeeTarget(sender, nick);
				}
			} else {
				sender.sendMessage(ChatColor.GRAY + "%Ошибка форматирования #2 [see|all]");
			}
		} else if (args.length >= 3) {
			if (!hasMute) {
				sender.sendMessage(ChatColor.GRAY + "%Вы не можете накладывать молчанку");
				return;
			}
			String nick = args[0];
			if (args[1].length() != 1) {
				sender.sendMessage(ChatColor.GRAY
						+ "%Сокращение канала не может быть длинее 1 символа: " + args[0]);
				return;
			}
			int chanel = Chanel.getIndexBySign(args[1].charAt(0));
			int time = 0;
			try {
				time = Integer.parseInt(args[2]);
			}
			catch (Exception e) {
				sender.sendMessage(ChatColor.GRAY + "%Время должно быть числом: " + args[2]);
				return;
			}
			if (time < 5 && !hasUnMute) {
				sender.sendMessage(ChatColor.GRAY + "%Вы не можете снять молчанку: " + time);
				return;
			}
			String reason = args.length >= 3 ? StringUtils.join(args, " ", 3, args.length)
					: "причина не указана";
			caseMute(sender, nick, chanel, time, reason);
		}

	}

	@Override
	public void saveMute() {
		Set<String> list;
		ConfigurationSection cs = Main.yaml.getRoot();
		if (cs != null) {
			list = cs.getKeys(false);
			for (String name : list) {
				for (int i = 0; i < chaneles; i++) {
					isMuted(name, i);
				}
			}
		}
		try {
			Main.yaml.save(Main.yamlFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unmute(String playerName, int chanel ) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -2) {
				Main.yaml.set(getPlayerMuteString(playerName, i), null);
				Main.yaml.set(getPlayerMuteString(playerName, i) + "-reason", null);
			}
		}

	}

	@Override
	public long getTimeMute(String playerName, int chanel ) {
		String dateStr = Main.yaml.getString(getPlayerMuteString(playerName, chanel));
		if (dateStr != null) {
			try {
				Date date = SDF.parse(dateStr);
				return (date.getTime() - System.currentTimeMillis()) / 1000;
			}
			catch (ParseException e) {
				Bukkit.getConsoleSender().sendMessage(getPlayerMuteString(playerName, chanel));
				Bukkit.getConsoleSender().sendMessage(
						"%Что-то не так с форматом времени: " + dateStr);
			}
		}
		return -1;

	}

	private void muteHelp(CommandSender sender ) {
		List<String> msg = new ArrayList<String>();
		msg.addAll(ValueStorage.muteHelp);
		for (String s : msg) {
			sender.sendMessage(ChanelRegister.colorize(s));
		}
	}

	@Override
	public void muteSeeAll(CommandSender sender ) {
		ConfigurationSection cs = Main.yaml.getRoot();
		sender.sendMessage(ChatColor.GRAY + "%===============all================");
		if (cs == null) return;
		Set<String> list = cs.getKeys(false);
		for (String name : list) {
			muteSeeTarget(sender, name);
		}

	}

	@Override
	public void muteSeeSelf(CommandSender sender ) {
		muteSeeTarget(sender, sender.getName());
	}

	@Override
	public void muteSeeTarget(CommandSender sender, String name ) {
		for (int i = 0; i < chaneles; i++) {
			long time = getTimeMute(name, i);
			String reason = Main.yaml.getString(getPlayerMuteString(name, i) + "-reason");
			if (time > -1) {
				boolean muted = time > 0;
				sender.sendMessage(ChatColor.GRAY
						+ "%"
						+ name
						+ " "
						+ (Chanel.getByIndex(i).getSign())
						+ (muted ? " молчит" : " молчал")
						+ (muted ? (". Осталось " + time + " секунд. Причина: "
								+ ChatColor.UNDERLINE + reason) : ""));
			}
		}

	}

}
