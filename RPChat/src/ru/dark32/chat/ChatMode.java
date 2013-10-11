package ru.dark32.chat;

import org.bukkit.ChatColor;

public enum ChatMode {

    GLOBAL(0, ChatColor.GOLD, "$", ";"),
    WORLD(1, ChatColor.GOLD,">","."),
    SHOUT(2, ChatColor.RED,"!","!"),
    WHISPER(3, ChatColor.GRAY,"#","№"),
    LOCAL(4, ChatColor.WHITE, "-","-"),
     PM(5, ChatColor.DARK_PURPLE, "@","2");
    private int id;
    private ChatColor color;
    private String ch, ch2;

    private ChatMode(int i, ChatColor c, String s, String s2) {
        id = i;
        color = c;
        ch = s;
        ch2 = s2;
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

    public String getAnotherStartChar() {
        return ch2;
    }
}
