package ru.dark32.chat;

import org.bukkit.command.CommandSender;

public interface IDeaf {
	public void deaf(String[] args, CommandSender sender);
	public void caseDeaf(String playerName, int chanel, String reason);
	public void caseUnDeaf(String playerName, int chanel);
	public boolean isDeaf(String name, int chanel);
	public void saveDeaf();
}
