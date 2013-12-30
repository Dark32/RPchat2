package ru.dark32.chat;

import org.bukkit.command.CommandSender;

public interface IIgnore {
	boolean hasIgnore(CommandSender sender, String target, int chanel );

	void caseIgnore(CommandSender sender, String target, int chanel, String reason );

	void ignore(final String[] args,CommandSender sender);

	void seeTarget(CommandSender sender, String target );

	void seeSelf(CommandSender self );

	void seeAll(CommandSender sender );

	void caseUnIgnore(CommandSender sender, String target, int chanel );
	
	boolean hasntIgnorable(String sender);
}
