/**
 * 
 */
package ru.dark32.chat.chanels;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.dark32.chat.ChatListener;
import ru.dark32.chat.Util;
import ru.dark32.chat.ValueStorage;
import ru.dark32.chat.ichanels.IPersonalMessagesChanel;

/**
 * @author Andrew
 * 
 */
public class PersonalMessageChanel extends BaseChanel implements IPersonalMessagesChanel {

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
		switch (ValueStorage.PmSearchNickMode) {
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
		String messge = this.getMessage(raw, _ind);
	}

	@Override
	public void responseSendMessage(Player sender ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendSpyMessage(Player sender ) {
		// TODO Auto-generated method stub

	}
}
