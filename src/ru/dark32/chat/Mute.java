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
	final private int				chaneles	= ChanelRegister.getChanels();
	private String					needName;
	private String					muteMessage;
	private String					unmuteMessage;
	private String					canTHelp;
	private String					canTSeeSelf;
	private String					canTSeeAllMute;
	private String					canTSeeTargetMute;
	private String					signMoreOne;
	private String					canTUnmute;
	private String					canTMute;
	private String					noReason;
	private String					timeNotNum;
	private String					subCMDErr;
	private String					dataError;
	private String					muteSee;

	public Mute(){
		needName = ChatColor.GRAY + "%Вы должны указать имя";
		muteMessage = ChatColor.GRAY + "%%n теперь молчит в %c на %t секунд по причине %r";
		unmuteMessage = ChatColor.GRAY + "%&n  теперь может говорить в 	 %c";
		canTHelp = ChatColor.GRAY + "%Вы не можете смотреть справку по молчанке";
		canTSeeSelf = ChatColor.GRAY + "%Вы не можете смотреть свою молчанку";
		canTSeeAllMute = ChatColor.GRAY + "%Вы не можете смотреть все молчанки";
		canTSeeTargetMute = ChatColor.GRAY + "%Вы не можете смотреть молчанку цели";
		signMoreOne = ChatColor.GRAY + "%Сокращение канала не может быть длинее 1 символа: $1";
		canTUnmute = ChatColor.GRAY + "%Вы не можете снять молчанку";
		canTMute = ChatColor.GRAY + "%Вы не можете устанавливать молчанку";
		noReason = "не указана";
		timeNotNum = ChatColor.GRAY + "%Время должно быть числом: ";
		subCMDErr = ChatColor.GRAY + "%Ошибка форматирования #2 [see|all]";
		dataError = "%Что-то не так с форматом времени: ";
		muteSee = ChatColor.GRAY + "%%n молчит в %c. Осталось " + "%t"
				+ " секунд. Причина: " + ChatColor.UNDERLINE + "&r";

	}

	private String getPlayerMuteString(String playerName, int chanel ) {
		return playerName + ".mute." + ChanelRegister.getByIndex(chanel).getInnerName();
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
			sender.sendMessage(needName);
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
		String _chanelName = (chanel >= 0 && chanel < chaneles ? ChanelRegister.getByIndex(chanel)
				.getName() : "a");
		if (time > 5) {
			sender.sendMessage(muteMessage.replace("%n", name).replace("%c", _chanelName)
					.replace("%t", String.valueOf(time)).replace("%r", reason));
		} else {
			sender.sendMessage(unmuteMessage.replace("%n", name).replace("%c", _chanelName));
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
				sender.sendMessage(canTHelp);
				return;
			}
			muteHelp(sender);
		} else if (args.length == 1) {
			String par = args[0];
			if (par.equals("see")) {
				if (!hasSeeSelf) {
					sender.sendMessage(canTSeeSelf);
					return;
				}
				muteSeeSelf(sender);
			} else if (par.equals("all")) {
				if (!hasAll) {
					sender.sendMessage(canTSeeAllMute);
					return;
				}
				muteSeeAll(sender);
			} else {
				if (!hasSee) {
					sender.sendMessage(canTSeeTargetMute);
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
						sender.sendMessage(canTSeeSelf);
						return;
					}
					muteSeeSelf(sender);
				} else {
					if (!hasSee) {
						sender.sendMessage(canTSeeTargetMute);
						return;
					}
					muteSeeTarget(sender, nick);
				}
			} else {
				sender.sendMessage(subCMDErr);
			}
		} else if (args.length >= 3) {
			if (!hasMute) {
				sender.sendMessage(canTMute);
				return;
			}
			String nick = args[0];
			if (args[1].length() != 1) {
				sender.sendMessage(signMoreOne.replace("$1", args[1]));
				return;
			}
			int chanel = ChanelRegister.getIndexBySign(args[1].charAt(0));
			int time = 0;
			try {
				time = Integer.parseInt(args[2]);
			}
			catch (Exception e) {
				sender.sendMessage(timeNotNum + args[2]);
				return;
			}
			if (time < 5 && !hasUnMute) {
				sender.sendMessage(canTUnmute);
				return;
			}
			String reason = args.length >= 3 ? StringUtils.join(args, " ", 3, args.length)
					: noReason;
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
				Bukkit.getConsoleSender().sendMessage(dataError + dateStr);
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
				sender.sendMessage(muteSee.replace("%n", name)
						.replace("%c", ChanelRegister.getByIndex(i).getInnerName())
						.replace("%r", reason).replace("%r", reason));
			}
		}

	}
}
