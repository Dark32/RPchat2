/**
 * 
 */
package ru.dark32.chat.chanels;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.dark32.chat.Main;
import ru.dark32.chat.Util;
import ru.dark32.chat.ichanels.IPersonalMessagesChanel;

/**
 * @author Andrew
 * 
 */
public class PersonalMessageChanel extends BaseChanel implements IPersonalMessagesChanel {

	private String	formatToSting;
	private String	formatSpyString;
	private String	formatFromString;
	private int		pmSearchNickMode;

	/**
	 * @return the pmSearchNickMode
	 */
	@Override
	public int getPmSearchNickMode() {
		return pmSearchNickMode;
	}

	/**
	 * @param pmSearchNickMode
	 *            the pmSearchNickMode to set
	 */
	@Override
	public void setPmSearchNickMode(final int pmSearchNickMode ) {
		this.pmSearchNickMode = pmSearchNickMode;
	}

	@Override
	public boolean hasNameTarget(final String raw ) {
		return raw.length() > 1;
	}

	@Override
	public String getNameTarget(final String raw ) {
		final int _ind = raw.indexOf(' ');
		return raw.substring(1, (_ind == -1) ? raw.length() : _ind);
	}

	@Override
	public int hasMessage(final String raw ) {
		return raw.indexOf(' ');
	}

	@Override
	public String getMessage(final String raw, final int _ind ) {
		return _ind != -1 ? raw.substring(_ind + 1, raw.length()) : "";
	}

	@Override
	public Player getTargetByName(final String name ) {
		switch (pmSearchNickMode) {
			case -1:
				return Bukkit.getServer().getPlayerExact(name);
			case 0:
				return Bukkit.getServer().getPlayer(name);
			case 1:
				return Util.getPlayerSoft(name);
			default:
				return Bukkit.getServer().getPlayer(name);
		}
	}

	@Override
	public boolean hasTarget(final Player target ) {
		return target != null;
	}

	@Override
	public Player getTarget(final String targetName ) {
		return getTargetByName(targetName);
	}

	@Override
	public void sendMessage(final Player sender, final String raw ) {
		if (!this.hasNameTarget(raw)) {
			sender.sendMessage("Имя цели не указанно");
			return;
		}
		final String nameTarget = this.getNameTarget(raw);
		final Player target = this.getTargetByName(nameTarget);
		if (!this.hasTarget(target)) {
			sender.sendMessage("Цель не найдена");
			return;
		}
		final int _ind = this.hasMessage(raw);
		if (_ind < 0) {
			sender.sendMessage("Сообщение не введено");
			return;
		}
		final String messge = this.getMessage(raw, _ind); // извлекаем сообщение
		// отсылаем цели
		target.sendMessage(formatTo(sender, target, messge));
		// отсылаем себе
		responseSendMessage(sender, formatFrom(sender, target, messge));
		// отсылаем прослушку
		sendSpyMessage(sender, target, formatSpy(sender, target, messge));
	}

	@Override
	public void responseSendMessage(final Player sender, final String msg ) {
		sender.sendMessage(msg);

	}

	@Override
	public void sendSpyMessage(final Player sender, final Player target, final String msg ) {
		Bukkit.getConsoleSender().sendMessage(msg);

	}

	@Override
	public List<Player> getRecipients(final Player sender ) {
		final List<Player> recipients = new LinkedList<Player>();
		final String noSpy = Main.BASE_PERM + "." + this.getInnerName() + ".nospy";
		for (final Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if (!(Util.hasPermission(recipient, noSpy) || Util.hasPermission(sender, noSpy))
					&& Util.hasPermission(recipient, Main.BASE_PERM + "." + this.getInnerName() + ".pmspy")
					&& !recipient.equals(sender)) {
				recipients.add(recipient);
				sender.sendMessage(recipient.getName());
			}
		}
		return recipients;
	}

	@Override
	public void setFormatTo(final String formatTo ) {
		this.formatToSting = ChanelRegister.colorize(formatTo);

	}

	@Override
	public String formatTo(final Player sender, final Player target, final String msg ) {
		return format(sender, formatToSting).replace("%2$s", msg).replace("%1$s", sender.getName());
	}

	@Override
	public void setFormatFrom(final String formatFrom ) {
		this.formatFromString = ChanelRegister.colorize(formatFrom);

	}

	@Override
	public String formatFrom(final Player sender,final  Player target, final String msg ) {
		return format(sender, formatFromString).replace("$r", target.getName()).replace("%2$s", msg)
				.replace("%1$s", sender.getName());
	}

	@Override
	public void setFormatSpy(final String formatSpy ) {
		this.formatSpyString = ChanelRegister.colorize(formatSpy);

	}

	@Override
	public String formatSpy(final Player sender, final Player target, final String msg ) {
		return format(sender, formatSpyString).replace("%1$s", sender.getName()).replace("$r", target.getName())
				.replace("%2$s", msg);
	}

	@Override
	public void preSend(final Player sender, final String message, final Set<Player>  recipient) {
	// отправляем сообщение цели
		this.sendMessage(sender, message);

	}
}
