package ru.dark32.chat.chanels;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.dark32.chat.ChanelRegister;
import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ichanels.ETypeChanel;
import ru.dark32.chat.ichanels.IChanel;

public class BaseChanel implements IChanel {
	final private String		colorize;
	protected final int			COUNT_EXCLUDE	= 1;
	protected final int			COUNT_INCLUDE	= -1;
	protected final int			COUNT_OFF		= 0;
	final private boolean		enable;
	final private String		formatString;
	final private int			index;
	final private String		innerName;
	final private boolean		isWorld;
	final private String		listenerMessage;
	final private int			listenerMessageEnable;
	final private String		name;
	final private boolean		needPerm;
	final private String		noListenerMessage;
	final private boolean		overAll;
	final private boolean		pimkEnable;
	final private Instrument	pimkInstrument;

	final private Note			pimkNote;
	final private char			prefix;
	final private char			sign;
	final private boolean		tabes;
	private ETypeChanel			type;

	public BaseChanel(String par_name ){
		final String path_enable = "Chat." + par_name + ".enable";
		final String path_name = "Chat." + par_name + ".name";
		final String path_world = "Chat." + par_name + ".world";
		final String path_prefix = "Chat." + par_name + ".prefix";
		final String path_sign = "Chat." + par_name + ".sign";
		final String path_format = "Chat." + par_name + ".format";
		final String path_tab = "Chat." + par_name + ".tab";
		final String path_listenerMessage = "Chat." + par_name + ".listenerMessage";
		final String path_noListenerMessage = "Chat." + par_name + ".noListenerMessage";
		final String path_isListenerMessage = "Chat." + par_name + ".isListenerMessage";
		final String path_needPerm = "Chat." + par_name + ".needPerm";
		final String path_pimk_note = "Chat." + par_name + ".pimk.note";
		final String path_pimk_enable = "Chat." + par_name + ".pimk.enable";
		final String path_pimk_instrument = "Chat." + par_name + ".pimk.instrument";
		final String path_pimk_colorize = "Chat." + par_name + ".pimk.colorize";
		final String path_overAll = "Chat." + par_name + ".overAll";

		this.index = ChanelRegister.getNextIndex();
		this.innerName = par_name.toLowerCase(Locale.US);
		this.enable = Main.chatConfig.getBoolean(path_enable, false);
		this.name = Main.chatConfig.getString(path_name, path_name);
		this.isWorld = Main.chatConfig.getBoolean(path_world, false);
		this.prefix = Main.chatConfig.getString(path_prefix, path_prefix).charAt(0);
		this.sign = Main.chatConfig.getString(path_sign, path_sign).charAt(0);
		this.formatString = ChanelRegister.colorUTF8(Main.chatConfig.getString(path_format, path_format), 3);
		this.tabes = Main.chatConfig.getBoolean(path_tab, true);
		this.listenerMessage = ChanelRegister.colorUTF8(
				Main.chatConfig.getString(path_listenerMessage, path_listenerMessage), 3);
		this.noListenerMessage = ChanelRegister.colorUTF8(
				Main.chatConfig.getString(path_noListenerMessage, path_noListenerMessage), 3);
		this.listenerMessageEnable = Main.chatConfig.getInt(path_isListenerMessage, 0);
		this.needPerm = Main.chatConfig.getBoolean(path_needPerm, false);
		// PIMK -->
		String note = Main.chatConfig.getString(path_pimk_note, "1F#");
		int octava = note.length() > 0 ? note.charAt(0) : 1;
		Note.Tone tone = Note.Tone.F;
		boolean sharped = false;
		if (note.length() >= 2 && note.length() <= 3) {
			char char0 = note.charAt(0);
			char char1 = note.charAt(1);
			octava = (char0 == '2') ? 2 : (char0 == '0' ? 0 : char0 == '1' ? 1 : 1);
			tone = ('A' <= char1 && 'F' >= char1) ? Note.Tone.valueOf(String.valueOf(char1)) : Note.Tone.F;
			sharped = (note.length() == 3 && note.charAt(1) == '#');
		}
		// <-- PIMK
		this.pimkEnable = Main.chatConfig.getBoolean(path_pimk_enable, false);
		this.pimkInstrument = Instrument.valueOf(Main.chatConfig.getString(path_pimk_instrument, "PIANO"));
		this.pimkNote = new Note(octava, tone, sharped);
		this.colorize = Main.chatConfig.getString(path_pimk_colorize, "@");
		this.overAll = Main.chatConfig.getBoolean(path_overAll, true);
	}

	@Override
	public boolean canSend(final Player sender, final String message ) {
		return true;
	}

	void DEBUG(String message ) {
		if (Main.DEBUG_MODE) {
			DEBUG(message, Bukkit.getConsoleSender());
		};
	}

	void DEBUG(String message, CommandSender sender ) {
		if (Main.DEBUG_MODE) {
			sender.sendMessage(message);
		};
	}

	private String findMatch(String name, String[] message ) {
		for (String s : message) {
			if (name.startsWith(s)) {
				return s;
			}
		}
		return null;
	}

	@Override
	public String format(final Player player, final String msg ) {
		String iden = Integer.toHexString(player.getTicksLived() + player.getEntityId());
		return msg.replace("$suffix", ChanelRegister.getSuffix(player.getName()))
				.replace("$prefix", ChanelRegister.getPreffix(player.getName())).replace("$p", "%1$s")
				.replace("$msg", "%2$s").replace("$id", iden);
	}

	@Override
	public String getColorize() {
		return colorize;
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
	public String getInnerName() {
		return innerName;
	}

	@Override
	public String getListenerMessage(int count ) {
		return (count > 0) ? listenerMessage.replace("$n", String.valueOf(count)) : noListenerMessage;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Instrument getPimkInstrument() {
		return pimkInstrument;
	}

	@Override
	public Note getPimkNote() {
		return pimkNote;
	}

	@Override
	public char getPrefix() {
		return this.prefix;
	}

	@Override
	public List<Player> getRecipients(final Player sender ) {
		final List<Player> recipients = new LinkedList<Player>();
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if (isRecipient(sender, recipient)) {
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
	public ETypeChanel getType() {
		return this.type;
	}

	@Override
	public boolean isEnable() {
		return enable;
	}

	@Override
	public int isListenerMessage() {
		return listenerMessageEnable;
	}

	@Override
	public boolean isNeedPerm() {
		return needPerm;
	}

	@Override
	public boolean isOverAll() {
		return overAll;
	}

	@Override
	public boolean isPimk() {
		return pimkEnable;
	}

	/**
	 * 
	 * @param sender
	 * @param recipient
	 * @return Глухота, Слышит, Сам, В канале , Игнорируем<br>
	 *         !isInChanel || isSelf || !isHear || isDeaf || hasIgnore
	 */
	protected boolean isRecipient(final Player sender, final Player recipient ) {
		final boolean isDeaf = Main.getDeafStorage().isDeaf(recipient.getName(), getIndex());
		final boolean isHear = !isNeedPerm()
				|| Util.hasPermission(recipient, Main.BASE_PERM + "." + getInnerName() + ".say")
				|| Util.hasPermission(recipient, Main.BASE_PERM + "." + getInnerName() + ".hear");
		final boolean isSelf = sender == recipient && isListenerMessage() == COUNT_INCLUDE;
		final boolean isntInChanel =!(isOverAll() && Util.getModeIndex(recipient.getName()) == getIndex());
		final boolean hasIgnore = Main.getIgnoreStorage().hasIgnore(sender, recipient.getName(), this.getIndex());
		return !(isntInChanel || isSelf || !isHear || isDeaf || hasIgnore);
	}

	@Override
	public boolean isTabes() {
		return tabes;
	}

	@Override
	public boolean isWorldChat() {
		return this.isWorld;
	}

	@Override
	public String preformatMessage(final Player sender, final String message ) {
		return Util.randomRoll(message);
	}

	@SuppressWarnings("deprecation" )
	@Override
	public void preSend(final Player sender, final String message, final Set<Player> recipient ) {
		// pimk
		if (isPimk()) {
			// FIX TO DO IT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			final String[] messageArr = message.replace(",", "").replace(".", "").replace(":", "").replace(";", "")
					.replace("?", "").replace("!", "").replace("@", "").split(" ");
			for (final Iterator<Player> iterator = recipient.iterator(); iterator.hasNext();) {
				final Player player = iterator.next();
				final String name = player.getName();
				final String match = findMatch(name, messageArr);
				if (match != null) {
					player.playSound(player.getLocation(), Sound.valueOf("NOTE_" + getPimkInstrument()), 3f,
							getPimkNote().getId());
					sender.sendMessage(getListenerMessage(recipient.size())
							+ format(sender, getFormat()).replace("%2$s", message.replace(match, name))
									.replace("%1$s", sender.getName())
									.replace(match, getColorize() + match + ChatColor.getLastColors(message)));
					iterator.remove();
				}

			}
		}
		// отправляем число услышавших, если это включено
		if (isListenerMessage() == COUNT_EXCLUDE) {
			sender.sendMessage(getListenerMessage(recipient.size() - 1));
		} else if (isListenerMessage() == COUNT_INCLUDE) {
			sender.sendMessage(getListenerMessage(recipient.size())
					+ String.format(format(sender, getFormat()), sender.getName(), message));
		}
	}

	@Override
	public void setType(final ETypeChanel type ) {
		this.type = type;

	}

	@Override
	public String toString() {
		return super.toString() + ", index =>" + this.index + ", isWorld =>" + this.isWorld + ", name =>" + this.name
				+ ", prefix =>" + this.prefix + ", sign =>" + this.sign + ", type =>" + this.type;
	}
}
