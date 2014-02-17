package ru.dark32.perm;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PermissionsHandler implements IPermission {
	private transient IPermission	handler			= new NullPermissionsHandler();
	private transient String		defaultGroup	= "default";
	private final transient Plugin	plugin;
	private static final Logger		LOGGER			= Logger.getLogger("rpChat2");

	public PermissionsHandler(final Plugin plugin ){
		this.plugin = plugin;
		checkPermissions();
	}

	@Override
	public String getGroup(final Player base ) {
		String group = handler.getGroup(base);
		if (group == null) {
			group = defaultGroup;
		}
		return group;
	}

	@Override
	public List<String> getGroups(final Player base ) {
		List<String> groups = handler.getGroups(base);
		if (groups == null || groups.isEmpty()) {
			groups = Collections.singletonList(defaultGroup);
		}
		return Collections.unmodifiableList(groups);
	}

	@Override
	public boolean inGroup(final Player base, final String group ) {
		return handler.inGroup(base, group);
	}

	@Override
	public boolean hasPermission(final CommandSender sender, final String node ) {
		if (!(sender instanceof Player)) {
			return true;
		} else {
			return handler.hasPermission(sender, node);
		}
	}

	@Override
	public String getPrefix(final Player base ) {
		String prefix = handler.getPrefix(base);
		if (prefix == null) {
			prefix = "";
		}
		return prefix;
	}

	@Override
	public String getSuffix(final Player base ) {
		String suffix = handler.getSuffix(base);
		if (suffix == null) {
			suffix = "";
		}
		return suffix;
	}

	public void checkPermissions() {
		final PluginManager pluginManager = plugin.getServer().getPluginManager();

		final Plugin permExPlugin = pluginManager.getPlugin("PermissionsEx");
		if (permExPlugin != null && permExPlugin.isEnabled()) {
			if (!(handler instanceof PermissionsExHandler)) {
				LOGGER.log(Level.INFO, "rpChat2: Using PermissionsEx based permissions.");
				handler = new PermissionsExHandler();
			}
			return;
		} else {
			if (!(handler instanceof SuperpermsHandler)) {
				LOGGER.log(Level.INFO, "rpChat2: Using superperms based permissions.");
				handler = new SuperpermsHandler();
			}
		}
	}

	public String getName() {
		return handler.getClass().getSimpleName().replace("Handler", "");
	}

	@Override
	public boolean hasPermission(String target, String node ) {
		return handler.hasPermission(target, node);
	}
}