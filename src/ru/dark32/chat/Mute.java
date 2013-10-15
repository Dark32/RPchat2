package ru.dark32.chat;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Mute implements IMute {
	protected static Pattern		_chanels	= Pattern
														.compile("(g|w|s|v|l|p|a)\\s(\\d+)\\s(.*)");
	protected static Pattern		_see		= Pattern.compile("(se{0,2})");
	private final String[]			_chanelName	= new String[] { "g", "w", "s", "v", "l", "p", "a" };
	private final SimpleDateFormat	SDF			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private YamlConfiguration		yaml;

	private File					yamlFile;

	public Mute(File file ){
		this.yamlFile = file;
		if (this.yamlFile.exists()) {
			yaml = YamlConfiguration.loadConfiguration(file);
		} else {
			yaml = new YamlConfiguration();
		}
	}

	@Override
	public boolean isMuted(String playerName, int chanel ) {
		int[] chs = new int[6];
		chs[chanel] = -1;
		String dateStr = yaml.getString("mute." + playerName + chanel, "unmute");
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
		for (int i = 0; i < 6; i++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, seconds[i]);
			yaml.set("mute." + playerName + i, SDF.format(cal.getTime()));

		}
		saveMute();
	}

	@Override
	public void mute(String playerName, String par2, Player seder ) {
		Matcher m = _chanels.matcher(par2);
		if (m.find()) {
			String chanel = m.group(1);
			int time = 0;
			try {
				time = Integer.parseInt(m.group(2));
			} catch (Exception e) {
				time = 0;
			}
			String reason = m.group(3);
			int[] chanelMuteTime = new int[6];
			if (chanel.equalsIgnoreCase(_chanelName[0])) {
				chanelMuteTime[0] = time;
			} else if (chanel.equalsIgnoreCase(_chanelName[1])) {
				chanelMuteTime[1] = time;
			} else if (chanel.equalsIgnoreCase(_chanelName[2])) {
				chanelMuteTime[2] = time;
			} else if (chanel.equalsIgnoreCase(_chanelName[3])) {
				chanelMuteTime[3] = time;
			} else if (chanel.equalsIgnoreCase(_chanelName[4])) {
				chanelMuteTime[4] = time;
			} else if (chanel.equalsIgnoreCase(_chanelName[5])) {
				chanelMuteTime[5] = time;
			} else if (chanel.equalsIgnoreCase(_chanelName[6])) {
				chanelMuteTime[0] = time;
				chanelMuteTime[1] = time;
				chanelMuteTime[2] = time;
				chanelMuteTime[3] = time;
				chanelMuteTime[4] = time;
				chanelMuteTime[5] = time;
			}
			mute(playerName, chanelMuteTime);
			for (int i = 0; i < 6; i++) {
				seder.sendMessage(ChatColor.GRAY + "%" + i + "  " + chanelMuteTime[i]);
			}
			seder.sendMessage(ChatColor.GRAY + "%" + playerName + " теперь молчит (" + chanel
					+ ") из-за " + reason + " на срок " + time + " секунд");

		} else {
			m = _see.matcher(par2);
			if (m.find()) {
				for (int i = 0; i < 6; i++) {
					String dateStr = yaml.getString("mute." + playerName + i, "unmute");
					if (dateStr != null) {
						try {
							Date date = SDF.parse(dateStr);
							boolean muted = (date.getTime() > System.currentTimeMillis());
							long time = date.getTime() - System.currentTimeMillis();
							seder.sendMessage(ChatColor.GRAY + "%" + playerName + " "
									+ (_chanelName[i]) + (muted ? " молчит " : " молчал ")
									+ (muted ? (" . Осталось " + time + " секунд") : ""));
						} catch (ParseException e) {}

					}
				}
			} else {
				seder.sendMessage(ChatColor.GRAY + "%Ошибка форматирования ");
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
				for (int i = 0; i < 6; i++) {
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
		for (int i = 0; i < 6; i++) {
			if (chanel[i] == -1) {
				yaml.set("mute." + playerName + i, null);
			}
		}

	}
}
