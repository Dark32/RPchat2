package ru.dark32.chat;

import org.bukkit.command.CommandSender;

public interface IDeaf {
	public void deaf(String[] args, CommandSender sender);

	public boolean isDeaf(String name, int chanel);

	public void saveDeaf();

	public void deafSeeAll(CommandSender sender);

	public void deafSeeSelf(CommandSender sender);

	public void deafSeeTarget(CommandSender sender, String name);

	void caseDeaf(CommandSender sender, String name, int chanel, String reason );

	void caseUnDeaf(CommandSender sender, String playerName, int chanel );
}
