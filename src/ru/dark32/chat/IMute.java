package ru.dark32.chat;

import org.bukkit.command.CommandSender;

public interface IMute {
	public boolean isMuted(String playerName, int chanel);

	public void unmute(String playerName, int chanel);

	public void saveMute();

	public long getTimeMute(String playerName, int chanel);

	public void mute(String[] args, CommandSender sender);

	public void muteSeeAll(CommandSender sender);

	public void muteSeeSelf(CommandSender sender);

	public void muteSeeTarget(CommandSender sender, String name);

	public void caseMute(CommandSender sender, String name, int chanel,
			int time, String reason);

}
