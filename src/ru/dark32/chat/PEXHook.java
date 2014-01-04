package ru.dark32.chat;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PEXHook {
	public static String getPreffix(final String name ) {
		if (!Util.usePEX) {
			return "";
		}
		final PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);
		if (user == null) {
			return "";
		}
		return ChanelRegister.colorUTF8(user.getPrefix(), 3);
	}

	public static String getSuffix(final String name ) {
		if (!Util.usePEX) {
			return "";
		}
		final PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);
		if (user == null) {
			return "";
		}
		return ChanelRegister.colorUTF8(user.getSuffix(), 3);
	}
}
