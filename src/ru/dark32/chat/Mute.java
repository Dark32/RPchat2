package ru.dark32.chat;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Mute implements IMute {
	private final SimpleDateFormat	SDF			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private YamlConfiguration		yaml;
	final private int				chaneles	= Chanel.getValues();
	private File					yamlFile;

	public Mute(File file ){
		this.yamlFile = file;
		if (this.yamlFile.exists()) {
			yaml = YamlConfiguration.loadConfiguration(file);
		} else {
			yaml = new YamlConfiguration();
		}
	}

	private String getPlayerMuteString(String playerName, int chanel ) {
		return playerName + ".mute." + Chanel.getByIndex(chanel).getSign();
	}

	@Override
	public boolean isMuted(String playerName, int chanel ) {
		int[] chs = new int[chaneles];
		chs[chanel] = -1;
		String dateStr = yaml.getString(getPlayerMuteString(playerName, chanel));
		if (dateStr != null) {
			try {
				Date date = SDF.parse(dateStr);
				if (date.getTime() > System.currentTimeMillis()) {
					return true;
				} else {
					unmute(playerName, chs);
				}
			}
			catch (ParseException e) {
				// empty
			}
		}

		return false;

	}

	@Override
	public void mute(String playerName, int[] seconds, String reason ) {
		for (int i = 0; i < chaneles; i++) {
			if (seconds[i] > 0) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, seconds[i]);
				yaml.set(getPlayerMuteString(playerName, i), SDF.format(cal.getTime()));
				yaml.set(getPlayerMuteString(playerName, i) + "-reason", reason);
			}
		}
		saveMute();
	}

	@Override // рабить на подмкетоды
	public void mute(String playerName, String par2, CommandSender sender ) {
		boolean isPlayer = (sender instanceof Player);
		boolean hasMute = isPlayer ? Util.hasPermission((Player) sender, "mcnw.mute.mute") : true;
		boolean hasUnMute = isPlayer ? Util.hasPermission((Player) sender, "mcnw.mute.unmute")
				: true;
		boolean hasSeeMute = isPlayer ? Util.hasPermission((Player) sender, "mcnw.mute.see") : true;
		boolean hasSeeSelfMute = isPlayer ? Util.hasPermission((Player) sender, "mcnw.mute.see.self") : true;
		boolean hasAllMute = isPlayer ? Util.hasPermission((Player) sender, "mcnw.mute.see.all") : true;
		String[] s = par2.split("\\s");
		if (s.length >= 2) {
			if (!hasMute) {
				sender.sendMessage(ChatColor.GRAY + "%У вас нет на это прав");
				return;
			}
			if (s[0].length() != 1) {
				sender.sendMessage(ChatColor.GRAY
						+ "%Сокращение канала не может быть длинее 1 символа: " + s[0]);
				return;
			}
			char chanel = s[0].charAt(0);
			int time = 0;
			try {
				time = Integer.parseInt(s[1]);
			}
			catch (Exception e) {
				sender.sendMessage(ChatColor.GRAY + "%Время должно быть числом: " + s[1]);
				return;
			}
			if ( !hasUnMute && time < 5) {
				sender.sendMessage(ChatColor.GRAY + "%У вас нет прав на снятие молчанки");
				return;
			}
			String reason = s.length >= 3 ? StringUtils.join(s, " ", 2, s.length)
					: "причина не указана";
			int[] chanelMuteTime = new int[chaneles];
			if (chanel != 'a') {
				int _ind = Chanel.getIndexBySign(chanel);
				if (_ind >= 0) {
					chanelMuteTime[_ind] = time;
				} else {
					sender.sendMessage(ChatColor.GRAY + "%Сигнатура канала указана не верно: "
							+ chanel);
					return;
				}
			} else {
				chanelMuteTime[0] = time;
				chanelMuteTime[1] = time;
				chanelMuteTime[2] = time;
				chanelMuteTime[3] = time;
				chanelMuteTime[4] = time;
				chanelMuteTime[5] = time;
				chanelMuteTime[6] = time;
				chanelMuteTime[7] = time;
			}
			mute(playerName, chanelMuteTime, reason);
			sender.sendMessage(ChatColor.GRAY + "%" + playerName + " теперь молчит (" + chanel
					+ ") из-за " + ChatColor.UNDERLINE + reason + ChatColor.RESET + ChatColor.GRAY
					+ " на срок " + time + " секунд");
		} else {
			if (s.length == 1) {
				if (s[0].equals("s") || s[0].equals("se") || s[0].equals("see")) {
					if (!hasSeeMute ||!(hasSeeSelfMute && sender.getName().equals(playerName))) {
						sender.sendMessage(ChatColor.GRAY + "%Вам нельзя просматривать молчанки");
						return;
					}
					sender.sendMessage(ChatColor.GRAY + "%===============================");
					for (int i = 0; i < chaneles; i++) {
						long time = getTimeMute(playerName, i);
						String reason = yaml.getString(getPlayerMuteString(playerName, i)
								+ "-reason");
						if (time > -1) {
							boolean muted = time>0;
							sender.sendMessage(ChatColor.GRAY
									+ "%"+ playerName+ " "
										+ (Chanel.getByIndex(i).getSign())
										+ (muted ? " молчит" : " молчал")
										+ (muted ? (". Осталось " + time + " секунд. Причина: "
												+ ChatColor.UNDERLINE + reason) : ""));
						}
					}
				}if (s[0].equals("all")){
					if (!hasAllMute) {
						sender.sendMessage(ChatColor.GRAY + "%Вам нельзя просматривать список молчанок");
						return;
					}
					ConfigurationSection cs = yaml.getConfigurationSection("mute");
					if (cs != null) {
						Set<String> list = cs.getKeys(false);
						for (String name : list) {
							for (int i = 0; i < chaneles; i++) {
								long time = getTimeMute(playerName, i);
								String reason = yaml.getString(getPlayerMuteString(playerName, i)
										+ "-reason");
								if (isMuted(name, i)){
									if (time > -1) {
									  sender.sendMessage(ChatColor.GRAY
											+ "%"+ playerName+ " "
										+ (Chanel.getByIndex(i).getSign())
										+ " молчит. Осталось " + time + " секунд. Причина: "
												+ ChatColor.UNDERLINE + reason);
									  }
								}
							}
						}
					}
				}else {
					sender.sendMessage(ChatColor.GRAY + "%Ошибка форматирования #2 [see|all]");
					sender.sendMessage(ChatColor.GRAY
							+ "%Сигнатура или [see|all] введено не правильно: " + s[0]);
				}
			} else {
				sender.sendMessage(ChatColor.GRAY + "%Ошибка форматирования #1 [length]");
			}
		}
	}

	@Override
	public void saveMute() {
		Set<String> list;
		ConfigurationSection cs = yaml.getConfigurationSection("mute");
		if (cs != null) {
			list = cs.getKeys(false);
			for (String name : list) {
				for (int i = 0; i < chaneles; i++) {
					isMuted(name, i);
				}
			}
		}
		try {
			yaml.save(yamlFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unmute(String playerName, int[] chanel ) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel[i] == -1) {
				yaml.set(getPlayerMuteString(playerName, i), null);
			}
		}

	}

	@Override
	public long getTimeMute(String playerName, int chanel ) {
		String dateStr = yaml.getString(getPlayerMuteString(playerName, chanel));
		if (dateStr != null) {
			try {
				Date date = SDF.parse(dateStr);
				return (date.getTime() - System.currentTimeMillis()) / 1000;
			}
			catch (ParseException e) {
				Bukkit.getConsoleSender().sendMessage(getPlayerMuteString(playerName, chanel));
				Bukkit.getConsoleSender().sendMessage("%Что-то не так с форматом времени: " + dateStr);
				e.printStackTrace();
			}
		}
		return -1;

	}

}
