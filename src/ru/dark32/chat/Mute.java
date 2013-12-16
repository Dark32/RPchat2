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
	final private String					needName;
	final private String					muteMessage;
	final private String					unmuteMessage;
	final private String					canTHelp;
	final private String					canTSeeSelf;
	final private String					canTSeeAllMute;
	final private String					canTSeeTargetMute;
	final private String					signMoreOne;
	final private String					canTUnmute;
	final private String					canTMute;
	final private String					noReason;
	final private String					timeNotNum;
	final private String					subCMDErr;
	final private String					dataError;
	final private String					muteSee;

	public Mute(){
		needName = ChanelRegister.colorize(Main.config.getString("mute.needName", "&7$Вы должны указать имя"));
		muteMessage = ChanelRegister.colorize(Main.config.getString("mute.muteMessage",
				"&7$$n теперь молчит в $c на $t секунд по причине $r"));
		unmuteMessage = ChanelRegister.colorize(Main.config.getString("mute.unmuteMessage",
				"&7$&n  теперь может говорить в  $c"));
		canTHelp = ChanelRegister.colorize(Main.config.getString("mute.canTHelp",
				"&7$Вы не можете смотреть справку по молчанке"));
		canTSeeSelf = ChanelRegister.colorize(Main.config.getString("mute.canTSeeSelf",
				"&7$Вы не можете смотреть свою молчанку"));
		canTSeeAllMute = ChanelRegister.colorize(Main.config.getString("mute.canTSeeAllMute",
				"&7$Вы не можете смотреть все молчанки"));
		canTSeeTargetMute = ChanelRegister.colorize(Main.config.getString("mute.canTSeeTargetMute",
				"&7$Вы не можете смотреть молчанку цели"));
		signMoreOne = ChanelRegister.colorize(Main.config.getString("mute.signMoreOne",
				"&7$Сокращение канала не может быть длинее 1 символа: $1"));
		canTUnmute = ChanelRegister
				.colorize(Main.config.getString("mute.canTUnmute", "&7$Вы не можете снять молчанку"));
		canTMute = ChanelRegister.colorize(Main.config.getString("mute.canTMute",
				"&7$Вы не можете устанавливать молчанку"));
		noReason = ChanelRegister.colorize(Main.config.getString("mute.noReason", "не указана"));
		timeNotNum = ChanelRegister.colorize(Main.config.getString("mute.timeNotNum", "&7$Время должно быть числом: "));
		subCMDErr = ChanelRegister.colorize(Main.config.getString("mute.subCMDErr",
				"&7$Ошибка форматирования #2 [see|all]"));
		dataError = ChanelRegister.colorize(Main.config.getString("mute.dataError",
				"&7$Что-то не так с форматом времени: "));
		muteSee = ChanelRegister.colorize(Main.config.getString("mute.muteSee",
				"&7$$n молчит в $c. Осталось $t секунд. Причина: &r"));

	}

	private String getPlayerMuteString(final String playerName,final  int chanel ) {
		return playerName + ".mute." + ChanelRegister.getByIndex(chanel).getInnerName();
	}

	@Override
	public boolean isMuted(final String playerName,final  int chanel ) {
		if (getTimeMute(playerName, chanel) > 0) {
			return true;
		} else {
			unmute(playerName, chanel);
			return false;
		}
	}

	@Override
	public void caseMute(final CommandSender sender,final  String name,final  int chanel,final  int time,final  String reason ) {
		if ("empty".equals(name)) {
			sender.sendMessage(needName);
			return;
		}
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -1) {
				final Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, time);
				Main.yaml.set(getPlayerMuteString(name, i), SDF.format(cal.getTime()));
				Main.yaml.set(getPlayerMuteString(name, i) + "-reason", reason);
			}
		}
		final String _chanelName = (chanel >= 0 && chanel < chaneles ? ChanelRegister.getByIndex(chanel).getName() : "a");
		if (time > 5) {
			sender.sendMessage(muteMessage.replace("$n", name).replace("$c", _chanelName)
					.replace("$t", String.valueOf(time)).replace("$r", reason));
		} else {
			sender.sendMessage(unmuteMessage.replace("$n", name).replace("$c", _chanelName));
		}
		saveMute();

	}

	@Override
	public void mute(final String[] args, final CommandSender sender ) {
		final boolean hasHelp = Util.hasPermission(sender, Main.BASE_PERM + ".mute.help");
		final boolean hasSee = Util.hasPermission(sender, Main.BASE_PERM + ".mute.see");
		final boolean hasAll = Util.hasPermission(sender, Main.BASE_PERM + ".mute.all");
		final boolean hasSeeSelf = Util.hasPermission(sender, Main.BASE_PERM + ".mute.see.self") || hasSee;
		final boolean hasMute = Util.hasPermission(sender, Main.BASE_PERM + ".mute.mute");
		final boolean hasUnMute = Util.hasPermission(sender, Main.BASE_PERM + ".mute.unmute");
		if (args.length == 0) {
			if (!hasHelp) {
				sender.sendMessage(canTHelp);
				return;
			}
			muteHelp(sender);
		} else if (args.length == 1) {
			final String par = args[0];
			if ("see".equals(par)) {
				if (!hasSeeSelf) {
					sender.sendMessage(canTSeeSelf);
					return;
				}
				muteSeeSelf(sender);
			} else if ("all".equals(par)) {
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
				sender.sendMessage(ChatColor.GRAY + "$" + par + " :");
				muteSeeTarget(sender, par);
			}
		} else if (args.length == 2) {
			final String nick = args[0];
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
			if (args[1].length() != 1) {
				sender.sendMessage(signMoreOne.replace("$1", args[1]));
				return;
			}
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
			final String reason = args.length >= 3 ? StringUtils.join(args, " ", 3, args.length) : noReason;
			final String nick = args[0];
			final int chanel = ChanelRegister.getIndexBySign(args[1].charAt(0));
			caseMute(sender, nick, chanel, time, reason);
		}

	}

	@Override
	public void saveMute() {
		Set<String> list;
		final ConfigurationSection cs = Main.yaml.getRoot();
		if (cs != null) {
			list = cs.getKeys(false);
			for (final String name : list) {
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
	public void unmute(final String playerName,final  int chanel ) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -1) {
				Main.yaml.set(getPlayerMuteString(playerName, i), null);
				Main.yaml.set(getPlayerMuteString(playerName, i) + "-reason", null);
			}
		}

	}

	@Override
	public long getTimeMute(final String playerName,final  int chanel ) {
		final String dateStr = Main.yaml.getString(getPlayerMuteString(playerName, chanel));
		if (dateStr != null) {
			try {
				final Date date = SDF.parse(dateStr);
				return (date.getTime() - System.currentTimeMillis()) / 1000;
			}
			catch (ParseException e) {
				Bukkit.getConsoleSender().sendMessage(getPlayerMuteString(playerName, chanel));
				Bukkit.getConsoleSender().sendMessage(dataError + dateStr);
			}
		}
		return -1;

	}

	private void muteHelp(final CommandSender sender ) {
		final List<String> msg = new ArrayList<String>();
		msg.addAll(ValueStorage.muteHelp);
		for (final String s : msg) {
			sender.sendMessage(ChanelRegister.colorize(s));
		}
	}

	@Override
	public void muteSeeAll(final CommandSender sender ) {
		final ConfigurationSection cs = Main.yaml.getRoot();
		sender.sendMessage(ChatColor.GRAY + "$===============all================");
		if (cs == null) return;
		final Set<String> list = cs.getKeys(false);
		for (final String name : list) {
			muteSeeTarget(sender, name);
		}

	}

	@Override
	public void muteSeeSelf(final CommandSender sender ) {
		muteSeeTarget(sender, sender.getName());
	}

	@Override
	public void muteSeeTarget(final CommandSender sender,final  String name ) {
		for (int i = 0; i < chaneles; i++) {
			final long time = getTimeMute(name, i);
			final String reason = Main.yaml.getString(getPlayerMuteString(name, i) + "-reason");
			if (time > -1) {
				sender.sendMessage(muteSee.replace("$n", name)
						.replace("$c", ChanelRegister.getByIndex(i).getInnerName()).replace("$r", reason)
						.replace("$t", String.valueOf(time)));
			}
		}

	}
}
