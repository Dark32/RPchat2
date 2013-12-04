/**
 * 
 */
package ru.dark32.chat.chanels;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.dark32.chat.Util;
import ru.dark32.chat.ValueStorage;
import ru.dark32.chat.ichanels.IPersonalMessagesChanel;

/**
 * @author Andrew
 * 
 */
public class PersonalMessageChanel extends BaseChanel implements IPersonalMessagesChanel {

	private String	formatTo;
	private String	formatSpy;
	private String	formatFrom;
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
	public void setPmSearchNickMode(int pmSearchNickMode ) {
		this.pmSearchNickMode = pmSearchNickMode;
	}

	@Override
	public boolean hasNameTarget(String raw ) {
		return raw.length() > 1;
	}

	@Override
	public String getNameTarget(String raw ) {
		int _ind = raw.indexOf(" ");
		return raw.substring(1, (_ind == -1) ? raw.length() : _ind);
	}

	@Override
	public int hasMessage(String raw ) {
		return raw.indexOf(" ");
	}

	@Override
	public String getMessage(String raw, int _ind ) {
		return _ind != -1 ? raw.substring(_ind + 1, raw.length()) : "";
	}

	@Override
	public Player getTargetByName(String name ) {
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
	public boolean hasTarget(Player target ) {
		return target != null;
	}

	@Override
	public Player getTarget(String targetName ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMessage(Player sender, String raw ) {
		if (!this.hasNameTarget(raw)) {
			sender.sendMessage("Имя цели не указанно");
			return;
		}
		String nameTarget = this.getNameTarget(raw);
		Player target = this.getTargetByName(nameTarget);
		if (!this.hasTarget(target)) {
			sender.sendMessage("Цель не найдена");
			return;
		}
		int _ind = this.hasMessage(raw);
		if (_ind > 0) {
			sender.sendMessage("Сообщение не введено");
			return;
		}
		String messge = this.getMessage(raw, _ind); // извлекаем сообщение
		// отсылаем цели
		target.sendMessage(formatTo(sender, target, messge));
		// отсылаем себе
		responseSendMessage(target, formatFrom(sender, target, messge));
		// отсылаем прослушку
		sendSpyMessage(sender, target, formatSpy(sender, target, messge));
	}

	@Override
	public void responseSendMessage(Player sender, String msg ) {
		sender.sendMessage(msg);

	}

	@Override
	public void sendSpyMessage(Player sender, Player target, String msg ) {
		Bukkit.getConsoleSender().sendMessage(msg);

	}

	@Override
	public List<Player> getRecipients(Player sender ) {
		List<Player> recipients = new LinkedList<Player>();
		for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
			if (!Util.hasPermission(recipient, "mcnw." + this.getInnerName() + ".nospy")
					&& Util.hasPermission(sender, "mcnw." + this.getInnerName() + ".pmspy")
					&& !recipient.equals(sender)) {
				recipients.add(recipient);
			}
		}
		return recipients;
	}

	@Override
	public void setFormatTo(String formatTo ) {
		this.formatTo = ChanelRegister.colorize(formatTo);

	}

	@Override
	public String formatTo(Player sender, Player target, String msg ) {
		msg = format(sender, formatTo).replace("%2$s", msg);
		return msg;
	}

	@Override
	public void setFormatFrom(String formatFrom ) {
		this.formatFrom = ChanelRegister.colorize(formatFrom);

	}

	@Override
	public String formatFrom(Player sender, Player target, String msg ) {
		msg = format(sender, formatFrom).replace("%r", target.getName()).replace("%2$s", msg);
		return msg;
	}

	@Override
	public void setFormatSpy(String formatSpy ) {
		this.formatSpy = ChanelRegister.colorize(formatSpy);

	}

	@Override
	public String formatSpy(Player sender, Player target, String msg ) {
		msg = format(sender, formatSpy).replace("%r", target.getName()).replace("%2$s", msg);
		return msg;
	}
}
