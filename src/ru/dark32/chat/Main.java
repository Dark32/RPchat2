package ru.dark32.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static final Logger	_log	= Logger.getLogger("Minecraft");
	public PluginManager		pm;
	private IMute				muteStorage;

	@Override
	public void onEnable() {
		pm = Bukkit.getPluginManager();
		if (pm.getPlugin("PermissionsEx") != null) {
			Util.usePEX = true;
		} else if (pm.getPlugin("PermissionsBukkit") != null) {
			Util.usePB = true;
		} else {
			getLogger().warning("Permissions plugins not found!");
		}
		// спасибо DmitriyMX за распаковку конфига. Туторы на его сайте
		// DmitriyMX.ru
		File fileConf = new File(getDataFolder(), "config.yml");
		if (!fileConf.exists()) {
			InputStream resourceAsStream = Main.class
					.getResourceAsStream("/ru/dark32/chat/config.yml");
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
		muteStorage = new Mute(new File(getDataFolder(), "storage.yml"));
		Util.init(this);
		getServer().getPluginManager().registerEvents(new Chat(config, this), this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args ) {
		if (cmd.getName().equalsIgnoreCase("rpchat")) {
			// for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			// Chat.updateDisplayName(player);
			// }
			return true;
		}
		return false;
	}

	@Override
	public void onDisable() {
		muteStorage.saveMute();
	}

	public IMute getBanStorage() {
		return muteStorage;
	}
}