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
	final private String			needName;
	final private String			muteMessage;
	final private String			unmuteMessage;
	final private String			canTHelp;
	final private String			canTSeeSelf;
	final private String			canTSeeAllMute;
	final private String			canTSeeTargetMute;
	final private String			signMoreOne;
	final private String			canTUnmute;
	final private String			canTMute;
	final private String			noReason;
	final private String			timeNotNum;
	final private String			subCMDErr;
	final private String			dataError;
	final private String			muteSee;

	public Mute(){
		needName = Util
				.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.needName", "mute.needName")));
		muteMessage = Util.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.muteMessage",
				"mute.muteMessage")));
		unmuteMessage = Util.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.unmuteMessage",
				"mute.unmuteMessage")));
		canTHelp = Util
				.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.canTHelp", "mute.canTHelp")));
		canTSeeSelf = Util.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.canTSeeSelf",
				"mute.canTSeeSelf")));
		canTSeeAllMute = ChanelRegister.colorize(Main.localeConfig.getString("mute.canTSeeAllMute",
				"mute.canTSeeAllMute"));
		canTSeeTargetMute = ChanelRegister.colorize(Main.localeConfig.getString("mute.canTSeeTargetMute",
				"mute.canTSeeTargetMute"));
		signMoreOne = Util.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.signMoreOne",
				"mute.signMoreOne")));
		canTUnmute = Util.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.canTUnmute",
				"mute.canTUnmute")));
		canTMute = Util
				.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.canTMute", "mute.canTMute")));
		noReason = Util
				.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.noReason", "mute.noReason")));
		timeNotNum = Util.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.timeNotNum",
				"mute.timeNotNum")));
		subCMDErr = Util.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.subCMDErr",
				"mute.subCMDErr")));
		dataError = Util.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.dataError",
				"mute.dataError")));
		muteSee = Util.parseUTF8(ChanelRegister.colorize(Main.localeConfig.getString("mute.muteSee", "mute.muteSee")));

	}

	private String getPlayerMuteString(final String playerName, final int chanel ) {
		return playerName + ".mute." + ChanelRegister.getByIndex(chanel).getInnerName();
	}

	@Override
	public boolean isMuted(final String playerName, final int chanel ) {
		if (getTimeMute(playerName, chanel) > 0) {
			return true;
		} else {
			unmute(playerName, chanel);
			return false;
		}
	}

	@Override
	public void caseMute(final CommandSender sender, final String name, final int chanel, final int time,
			final String reason ) {
		if ("empty".equals(name)) {
			sender.sendMessage(needName);
			return;
		}
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -1) {
				final Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, time);
				Main.storage.set(getPlayerMuteString(name, i), SDF.format(cal.getTime()));
				Main.storage.set(getPlayerMuteString(name, i) + "-reason", reason);
			}
		}
		final String _chanelName = (chanel >= 0 && chanel < chaneles ? ChanelRegister.getByIndex(chanel).getName()
				: "a");
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
		final ConfigurationSection cs = Main.storage.getRoot();
		if (cs != null) {
			list = cs.getKeys(false);
			for (final String name : list) {
				for (int i = 0; i < chaneles; i++) {
					isMuted(name, i);
				}
			}
		}
		try {
			Main.storage.save(Main.storageFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unmute(final String playerName, final int chanel ) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -1) {
				Main.storage.set(getPlayerMuteString(playerName, i), null);
				Main.storage.set(getPlayerMuteString(playerName, i) + "-reason", null);
			}
		}

	}

	@Override
	public long getTimeMute(final String playerName, final int chanel ) {
		final String dateStr = Main.storage.getString(getPlayerMuteString(playerName, chanel));
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
		final ConfigurationSection cs = Main.storage.getRoot();
		sender.sendMessage(ChatColor.GRAY + "$===============all================");
		if (cs == null) {
			return;
		}
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
	public void muteSeeTarget(final CommandSender sender, final String name ) {
		for (int i = 0; i < chaneles; i++) {
			final long time = getTimeMute(name, i);
			final String reason = Main.storage.getString(getPlayerMuteString(name, i) + "-reason");
			if (time > -1) {
				sender.sendMessage(muteSee.replace("$n", name)
						.replace("$c", ChanelRegister.getByIndex(i).getInnerName()).replace("$r", reason)
						.replace("$t", String.valueOf(time)));
			}
		}

	}
}
