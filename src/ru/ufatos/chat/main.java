package ru.ufatos.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class main extends JavaPlugin {

	public static final Logger _log = Logger.getLogger("Minecraft");
	static boolean usePEX = false;
	static boolean usePB = false;
	public PluginManager pm;

	public void onEnable() {
		pm = Bukkit.getPluginManager();
		if (pm.getPlugin("PermissionsEx") != null) {
			usePEX = true;
		} else if (pm.getPlugin("PermissionsBukkit") != null) {
			usePB = true;
		} else {
			getLogger().warning("Permissions plugins not found!");
		}
		// И опять спасибо DmitriyMX за распаковку конфига. Туторы на его сайте DmitriyMX.ru
		File fileConf = new File(getDataFolder(), "config.yml");
		if (!fileConf.exists()) {
			InputStream resourceAsStream = main.class.getResourceAsStream("/ru/ufatos/chat/config.yml");
			getDataFolder().mkdirs();
			try {
				FileOutputStream fos = new FileOutputStream(fileConf);
				byte[] buff = new byte[65536];
				int n;
				while ((n = resourceAsStream.read(buff)) > 0) {
					fos.write(buff, 0, n);
					fos.flush();
				}
				fos.close();
				buff = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			getLogger().info("Сonfig loaded");
		}
		FileConfiguration config = this.getConfig();
		getServer().getPluginManager().registerEvents(new Chat(config), this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("updnames")) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				Chat.updateDisplayName(player);
			}
			return true;
		}
		return false;
	}

	public static boolean hasPermission(Player player, String permission) {
		if (usePEX) {
			return PermissionsEx.getUser(player).has(permission);
		} else if (usePB) {
			return player.hasPermission(permission);
		} else {
			return player.isOp();
		}
		//return usePEX ? PermissionsEx.getUser(player).has(permission) : usePB ? player.hasPermission(permission):player.isOp();
	}
}