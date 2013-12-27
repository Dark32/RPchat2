package ru.dark32.chat;

import org.bukkit.command.CommandSender;

public interface IMute {
	boolean isMuted(String playerName, int chanel );

	void unmute(String playerName, int chanel );

	void save();

	long getTimeMute(String playerName, int chanel );

	void mute(String[] args, CommandSender sender );

	void seeAll(CommandSender sender );

	void seeSelf(CommandSender sender );

	void seeTarget(CommandSender sender, String name );

	void caseMute(CommandSender sender, String name, int chanel, int time, String reason );

}
