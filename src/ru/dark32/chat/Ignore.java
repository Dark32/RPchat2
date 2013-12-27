package ru.dark32.chat;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class Ignore implements IIgnore {
	final private int	chaneles	= ChanelRegister.getChanels();
	private String		message2;
	private String		cantIgnorable;
	private String		message;
	private String		unIgnore;

	public Ignore(){
		message2 = getLoc("ignore.Message2");
		message = getLoc("ignore.Message");
		cantIgnorable = getLoc("ignore.cantIgnorable");
		unIgnore =  getLoc("ignore.unIgnore");
	}

	private String getLoc(final String key ) {
		return ChanelRegister.colorUTF8(Main.localeConfig.getString(key, key), 3);
	}

	private String getPlayerDeafString(final String playerName, final String targetName, final int chanel ) {
		return playerName + ".ignore." + ChanelRegister.getByIndex(chanel).getInnerName() + "." + targetName;
	}

	@Override
	public boolean hasIgnore(CommandSender sender, String target, int chanel ) {
		return Main.storage.getBoolean(getPlayerDeafString(sender.getName(), target, chanel));
	}

	@Override
	public void caseIgnore(CommandSender sender, String target, int chanel, String reason ) {
		if (hasntIgnorable(target)) {
			sender.sendMessage(cantIgnorable.replace("$name", sender.getName()).replace("$ignore", target));
		}

		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -1) {
				Main.storage.set(getPlayerDeafString(sender.getName(), target, i), true);
				Main.storage.set(getPlayerDeafString(sender.getName(), target, i) + "-reason", reason);
			}
		}
		String chanelName = (chanel >= 0 && chanel < this.chaneles ? ChanelRegister.getByIndex(chanel).getName() : "a");
		sender.sendMessage(message2.replace("$name", sender.getName()).replace("$channel", chanelName)
				.replace("$reason", reason).replace("$ignore", target));
	}

	@Override
	public void ignore(CommandSender sender, String raw ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void seeTarget(CommandSender sender, String target ) {
		final ConfigurationSection cs = Main.storage.getConfigurationSection(sender.getName() + ".ignore");
		if (cs == null) {
			return;
		}
		final Set<String> list = cs.getKeys(false);
		for (final String ignoreName : list) {
			for (int i = 0; i < chaneles; i++) {
				final String reason = Main.storage
						.getString(getPlayerDeafString(target, ignoreName, i) + "-reason", "");
				final boolean isIgnore = this.hasIgnore(sender, target, i);
				if (isIgnore) {
					sender.sendMessage(message.replace("$name", ignoreName)
							.replace("$channel", ChanelRegister.getByIndex(i).getName()).replace("$reason", reason));
				}
			}
		}
	}

	@Override
	public void seeSelf(CommandSender self ) {
		seeTarget(self, self.getName());
	}

	@Override
	public void seeAll(CommandSender sender ) {
		final ConfigurationSection cs = Main.storage.getRoot();
		sender.sendMessage(ChatColor.GRAY + "$===============all================");
		if (cs == null) {
			return;
		}
		final Set<String> list = cs.getKeys(false);
		for (final String name : list) {
			seeTarget(sender, name);
		}

	}

	@Override
	public void caseUnIgnore(CommandSender sender, String target, int chanel ) {
		for (int i = 0; i < chaneles; i++) {
			if (chanel == i || chanel == -1) {
				Main.storage.set(getPlayerDeafString(sender.getName(), target, i), true);
				Main.storage.set(getPlayerDeafString(sender.getName(), target, i) + "-reason", "");
			}
		}
		String chanelName = (chanel >= 0 && chanel < this.chaneles ? ChanelRegister.getByIndex(chanel).getName() : "a");
		sender.sendMessage(unIgnore.replace("$name", sender.getName()).replace("$channel", chanelName)
				.replace("$ignore", target));

	}

	@Override
	public boolean hasntIgnorable(String sender ) {
		return Util.hasPermission(sender, Main.BASE_PERM + ".ignore.non");
	}

}
