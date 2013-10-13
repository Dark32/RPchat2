package ru.dark32.chat;

import org.bukkit.ChatColor;

public enum ChatMode {

		GLOBAL(0, ChatColor.GOLD, "$" ),
		WORLD(1, ChatColor.GOLD, ">" ),
		SHOUT(2, ChatColor.RED, "!"),
		WHISPER(3, ChatColor.GRAY, "#"),
		LOCAL(4, ChatColor.WHITE, "-" ),
		PM(5, ChatColor.DARK_PURPLE, "@" );
	private int			id;
	private ChatColor	color;
	private String		ch;

	private ChatMode(int i, ChatColor c, String s ){
		id = i;
		color = c;
		ch = s;
		}

	public ChatColor getColor() {
		return color;
	}

	public int getModeId() {
		return id;
	}

	public String getStartChar() {
		return ch;
	}
}
