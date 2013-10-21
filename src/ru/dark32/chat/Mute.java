package ru.dark32.chat;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Mute implements IMute {
	private final SimpleDateFormat	SDF			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private YamlConfiguration		yaml;
	final private int				chaneles	= ChatMode.values().length;
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
		return playerName + ".mute." + ChatMode.values()[chanel].getSign();
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
			} catch (ParseException e) {
				// empty
			}
		}

		return false;

	}

	@Override
	public void mute(String playerName, int[] seconds ) {
		for (int i = 0; i < chaneles; i++) {
			if (seconds[i] > 0) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.SECOND, seconds[i]);
				yaml.set(getPlayerMuteString(playerName, i), SDF.format(cal.getTime()));
			}
		}
		saveMute();
	}

	@Override
	public void mute(String playerName, String par2, Player seder ) {
		String[] s = par2.split("\\s");
		if (s.length >= 2) {
			if (s[0].length() != 1) {
				seder.sendMessage(ChatColor.GRAY
						+ "%Сокращение канала не может быть длинее 1 символа: " + s[0]);
				return;
			}
			char chanel = s[0].charAt(0);
			int time = 0;
			try {
				time = Integer.parseInt(s[1]);
			} catch (Exception e) {
				seder.sendMessage(ChatColor.GRAY + "%Время должно быть числом: " + s[1]);
				return;
			}
			String reason = s.length >= 3 ? StringUtils.join(s, " ", 2, s.length - 1)
					: "причина не указана";
			int[] chanelMuteTime = new int[chaneles];
			if (chanel == ChatMode.GLOBAL.getSign()) {
				chanelMuteTime[0] = time;
			} else if (chanel == ChatMode.WORLD.getSign()) {
				chanelMuteTime[1] = time;
			} else if (chanel == ChatMode.SHOUT.getSign()) {
				chanelMuteTime[2] = time;
			} else if (chanel == ChatMode.LOCAL.getSign()) {
				chanelMuteTime[3] = time;
			} else if (chanel == ChatMode.WHISPER.getSign()) {
				chanelMuteTime[4] = time;
			} else if (chanel == ChatMode.PM.getSign()) {
				chanelMuteTime[5] = time;
			} else if (chanel == ChatMode.CHANCE.getSign()) {
				chanelMuteTime[6] = time;
			} else if (chanel == 'a') {
				chanelMuteTime[0] = time;
				chanelMuteTime[1] = time;
				chanelMuteTime[2] = time;
				chanelMuteTime[3] = time;
				chanelMuteTime[4] = time;
				chanelMuteTime[5] = time;
				chanelMuteTime[6] = time;
			} else {
				seder.sendMessage(ChatColor.GRAY + "%Сигнатура канала указана не верно: " + s[0]);
			}
			mute(playerName, chanelMuteTime);
			seder.sendMessage(ChatColor.GRAY + "%" + playerName + " теперь молчит (" + chanel
					+ ") из-за " + reason + " на срок " + time + " секунд");
		} else {
			if (s.length == 1) {
				if (s[0].equals("s") || s[0].equals("se") || s[0].equals("see")) {
					seder.sendMessage(ChatColor.GRAY + "%===============================");
					for (int i = 0; i < chaneles; i++) {
						String dateStr = yaml.getString(
								getPlayerMuteString(playerName, i),
									"unmute");
						if (dateStr != null) {
							try {
								Date date = SDF.parse(dateStr);
								boolean muted = (date.getTime() > System.currentTimeMillis());
								long time = (date.getTime() - System.currentTimeMillis()) / 1000;
								seder.sendMessage(ChatColor.GRAY + "%" + playerName + " "
										+ (ChatMode.values()[i].getSign())
										+ (muted ? " молчит" : " молчал")
										+ (muted ? (". Осталось " + time + " секунд") : ""));
							} catch (ParseException e) {}

						}
					}
				} else {
					seder.sendMessage(ChatColor.GRAY + "%Ошибка форматирования #2 [see]");
					seder.sendMessage(ChatColor.GRAY + "%Сигнатура или [see] введено не правильно: "+s[0]);
				}
			} else {
				seder.sendMessage(ChatColor.GRAY + "%Ошибка форматирования #1 [length]");
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
		} catch (IOException e) {
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
		int[] chs = new int[6];
		chs[chanel] = -1;
		String dateStr = yaml.getString(getPlayerMuteString(playerName, chanel), "unmute");
		if (dateStr != null) {
			try {
				Date date = SDF.parse(dateStr);
				return (date.getTime() - System.currentTimeMillis()) / 1000;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return -1;

	}

}
