package ru.dark32.chat.chanels;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.dark32.chat.ChatListener;
import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ichanels.ETypeChanel;
import ru.dark32.chat.ichanels.IChanel;

public class BaseChanel implements IChanel {
	private boolean		enable;
	private String		format;
	private int			index;
	private boolean		isWorld;
	private String		name;
	private char		prefix;
	private char		sign;
	private ETypeChanel	type;
	private String		innerName;

	@Override
	public boolean getEnable() {
		return enable;
	}

	@Override
	public String getFormat() {
		return this.format;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public char getPrefix() {
		return this.prefix;
	}

	@Override
	public List<Player> getRecipients(Player sender ) {
		List<Player> recipients = new LinkedList<Player>();
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			boolean isDeaf = Main.getDeafStorage().isDeaf(recipient.getName(), getIndex());
			boolean isWorld = isWorldChat() && sender.getWorld() == recipient.getWorld();
			if (isDeaf) {
				continue;
			} else if (Util.hasPermission(recipient, "mcnw.spy")) {
				recipients.add(recipient);
			} else if (!isWorld) {
				continue;
			} else {
				recipients.add(recipient);
			}
		}
		return recipients;
	}

	@Override
	public char getSign() {
		return this.sign;
	}

	private String getString(String key ) {
		return ChatListener.tCC(Main.config.getString(key, key));
	}

	@Override
	public boolean isWorldChat() {
		return this.isWorld;
	}

	@Override
	public void setEnable(boolean enbl ) {
		this.enable = enbl;
	}

	@Override
	public void setFormat(String key ) {
		this.format = getString(key);
	}

	@Override
	final public void setIndex(int i ) {
		this.index = i;
	}

	@Override
	public void setName(String name ) {
		this.name = name;
	}

	@Override
	public void setPrefix(String key ) {
		this.prefix = getString(key).charAt(0);
	}

	@Override
	public void setSign(char sign ) {
		this.sign = sign;
	}

	@Override
	public void setWorldChat(boolean isWorld ) {
		this.isWorld = isWorld;
	}

	@Override
	public void setType(ETypeChanel type ) {
		this.type = type;

	}

	@Override
	public ETypeChanel getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return super.toString() + ", index =>" + this.index + ", isWorld =>" + this.isWorld
				+ ", name =>" + this.name + ", prefix =>" + this.prefix + ", sign =>" + this.sign
				+ ", type =>" + this.type;
	}

	@Override
	public String getInnerName() {
		return innerName;
	}

	@Override
	public void setInnerName(String name ) {
		innerName = name.toLowerCase();

	}
}
