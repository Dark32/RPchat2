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
	private YamlConfiguration		yaml;
	private File					yamlFile;
	private final SimpleDateFormat	SDF	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Mute(File file ){
		this.yamlFile = file;
		if (this.yamlFile.exists()) {
			yaml = YamlConfiguration.loadConfiguration(file);
		} else {
			yaml = new YamlConfiguration();
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

	protected static Pattern	chanels	= Pattern.compile("(g|w|s|v|l|p|a)\\s(\\d+)\\s(.*)");
	protected static Pattern	see		= Pattern.compile("(se{0,2})");

	@Override
	public void mute(String playerName, String par2, Player seder ) {
		Matcher m = chanels.matcher(par2);
		if (m.find()) {
			String ch = m.group(1);
			int time = 0;
			try {
				time = Integer.parseInt(m.group(2));
			} catch (Exception e) {
				time = 0;
			}
			String reason = m.group(3);
			int[] chs = new int[6];
			if (ch.equalsIgnoreCase("g")) {
				chs[0] = time;
			} else if (ch.equalsIgnoreCase("w")) {
				chs[1] = time;
			} else if (ch.equalsIgnoreCase("s")) {
				chs[2] = time;
			} else if (ch.equalsIgnoreCase("v")) {
				chs[3] = time;
			} else if (ch.equalsIgnoreCase("l")) {
				chs[4] = time;
			} else if (ch.equalsIgnoreCase("p")) {
				chs[5] = time;
			} else if (ch.equalsIgnoreCase("a")) {
				chs[0] = time;
				chs[1] = time;
				chs[2] = time;
				chs[3] = time;
				chs[4] = time;
				chs[5] = time;
			}
			mute(playerName, chs);
			for (int i = 0; i < 6; i++) {
				seder.sendMessage(ChatColor.GRAY + "%" + i + "  " + chs[i]);
			}
			seder.sendMessage(ChatColor.GRAY + "%" + playerName + " теперь молчит (" + ch
					+ ") из-за " + reason + " на срок " + time + " секунд");

		} else {
			m = see.matcher(par2);
			if (m.find()) {
				for (int i = 0; i < 6; i++) {
					String dateStr = yaml.getString("mute." + playerName + i, "unmute");
					if (dateStr != null) {
						try {
							Date date = SDF.parse(dateStr);
							boolean muter = (date.getTime() > System.currentTimeMillis());
							seder.sendMessage(ChatColor.GRAY
									+ "%"
									+ playerName
									+ " "
									+ i
									+ (muter ? " молчит " : " молчал ")
									+ (muter ? (" . Осталось " + (date.getTime() - System
											.currentTimeMillis())) + " секунд" : ""));
						} catch (ParseException e) {}

					}
				}
			} else {
				seder.sendMessage(ChatColor.GRAY + "%Ошибка форматирования ");
			}
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
