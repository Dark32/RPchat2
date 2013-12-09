package ru.dark32.chat.chanels;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ichanels.ETypeChanel;
import ru.dark32.chat.ichanels.IChanel;

public class BaseChanel implements IChanel {
	private boolean		enable;
	private String		formatString;
	private int			index;
	private boolean		isWorld;
	private String		name;
	private char		prefix;
	private char		sign;
	private ETypeChanel	type;
	private String		innerName;
	private boolean		tabes;
	private String		listenerMessage;
	private boolean		listenerMessageEnable;
	private String		noListenerMessage;

	@Override
	public boolean isEnable() {
		return enable;
	}

	@Override
	public String getFormat() {
		return this.formatString;
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
		final List<Player> recipients = new LinkedList<Player>();
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			final boolean isDeaf = Main.getDeafStorage().isDeaf(recipient.getName(), getIndex());
			final boolean isWorld = isWorldChat() && sender.getWorld() == recipient.getWorld();
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

	@Override
	public boolean isWorldChat() {
		return this.isWorld;
	}

	@Override
	public void setEnable(final boolean enbl ) {
		this.enable = enbl;
	}

	@Override
	public void setFormat(final String key ) {
		this.formatString = ChanelRegister.colorize(key);
	}

	@Override
	final public void setIndex(final int indx ) {
		this.index = indx;
	}

	@Override
	public void setName(final String name ) {
		this.name = name;
	}

	@Override
	public void setPrefix(final String key ) {
		this.prefix = key.charAt(0);
	}

	@Override
	public void setSign(final char sign ) {
		this.sign = sign;
	}

	@Override
	public void setWorldChat(final boolean isWorld ) {
		this.isWorld = isWorld;
	}

	@Override
	public void setType(final ETypeChanel type ) {
		this.type = type;

	}

	@Override
	public ETypeChanel getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return super.toString() + ", index =>" + this.index + ", isWorld =>" + this.isWorld + ", name =>" + this.name
				+ ", prefix =>" + this.prefix + ", sign =>" + this.sign + ", type =>" + this.type;
	}

	@Override
	public String getInnerName() {
		return innerName;
	}

	@Override
	public void setInnerName(final String name ) {
		innerName = name.toLowerCase(Locale.US);

	}

	@Override
	public String format(final Player player, final String msg ) {
		return msg.replace("$sf", ChanelRegister.getSuffix(player.getName()))
				.replace("$pf", ChanelRegister.getPreffix(player.getName())).replace("$p", "%1$s")
				.replace("$msg", "%2$s");
	}

	@Override
	public void setTabes(final boolean tables ) {
		this.tabes = tables;

	}

	@Override
	public boolean isTabes() {
		return tabes;
	}

	// обычно не надо обрабатывать само сообщение
	@Override
	public String preformatMessage(Player sender, String message ) {
		return message;
	}

	@Override
	public boolean canSend(Player sender, String message ) {
		return true;
	}

	@Override
	public void preSend(Player sender, String message, int recipient ) {
		// отправляем число услышавших, если это включено
		if (isListenerMessage()) {
			sender.sendMessage(getListenerMessage(recipient - 1));
		}
	}

	@Override
	public void setListenerMessage(String listenerMessage, String noListenerMessage, boolean enable ) {
		this.listenerMessage = ChanelRegister.colorize(listenerMessage);
		this.noListenerMessage = ChanelRegister.colorize(noListenerMessage);
		this.listenerMessageEnable = enable;

	}

	@Override
	public String getListenerMessage(int count ) {
		return (count > 0) ? listenerMessage.replace("$n", String.valueOf(count)) : noListenerMessage;
	}

	@Override
	public boolean isListenerMessage() {
		return listenerMessageEnable;
	}
}
