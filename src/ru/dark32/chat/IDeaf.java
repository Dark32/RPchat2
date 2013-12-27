package ru.dark32.chat;

import org.bukkit.command.CommandSender;

public interface IDeaf {
	void deaf(String[] args, CommandSender sender );

	boolean isDeaf(String name, int chanel );

	void save();

	void seeAll(CommandSender sender );

	void seeSelf(CommandSender sender );

	void seeTarget(CommandSender sender, String name );

	void caseDeaf(CommandSender sender, String name, int chanel, String reason );

	void caseUnDeaf(CommandSender sender, String playerName, int chanel );
}
