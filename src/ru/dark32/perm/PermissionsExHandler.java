package ru.dark32.perm;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.dark32.chat.ChanelRegister;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsExHandler extends SuperpermsHandler {
	private final transient PermissionManager	manager;

	public PermissionsExHandler(){
		manager = PermissionsEx.getPermissionManager();
	}

	@Override
	public String getGroup(final Player base ) {
		final PermissionUser user = manager.getUser(base.getName());
		if (user == null) {
			return null;
		}
		return user.getGroupsNames()[0];
	}

	@Override
	public List<String> getGroups(final Player base ) {
		final PermissionUser user = manager.getUser(base.getName());
		if (user == null) {
			return null;
		}
		return Arrays.asList(user.getGroupsNames());
	}

	@Override
	public boolean inGroup(final Player base, final String group ) {
		final PermissionUser user = manager.getUser(base.getName());
		if (user == null) {
			return false;
		}

		return user.inGroup(group);
	}

	@Override
	public boolean hasPermission(final CommandSender base, final String node ) {
		return super.hasPermission(base, node);
	}

	@Override
	public String getPrefix(final Player base ) {
		final PermissionUser user = manager.getUser(base.getName());
		if (user == null) {
			return null;
		}
		return ChanelRegister.colorUTF8(user.getPrefix(base.getWorld().getName()),3);
	}

	@Override
	public String getSuffix(final Player base ) {
		final PermissionUser user = manager.getUser(base.getName());
		if (user == null) {
			return null;
		}

		return ChanelRegister.colorUTF8(user.getSuffix(base.getWorld().getName()),3);
	}
}