package ru.dark32.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static final Logger		_log	= Logger.getLogger("Minecraft");
	public PluginManager			pm;
	private static IMute			muteStorage;
	private static IDeaf			deafStorage;
	public static final String		version	= "RPchat v 1.6h-0";
	public static FileConfiguration	config;

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
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			getLogger().info("Сonfig loaded");
		}
		config = this.getConfig();
		Util.init(this);
		ValueStorage.init();
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new TabListener(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		Main.muteStorage = new Mute(new File(getDataFolder(), "storage.yml"));
		Main.deafStorage = new Deaf(new File(getDataFolder(), "storage.yml"));
		
		getCommand("rpchat").setExecutor(new RPChatCommandExecutor());
		getCommand("mute").setExecutor(new RPChatCommandExecutor());
	}

	@Override
	public void onDisable() {
		muteStorage.saveMute();
		deafStorage.saveDeaf();
	}

	public static IMute getBanStorage() {
		return muteStorage;
	}
	public static IDeaf getDeafStorage() {
		return deafStorage;
	}
}