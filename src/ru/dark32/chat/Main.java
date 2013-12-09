package ru.dark32.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.dark32.chat.chanels.ChanelRegister;
import ru.dark32.chat.chanels.ChatListener;

public class Main extends JavaPlugin {

	public static final Logger		LOG	= Logger.getLogger("Minecraft");
	public PluginManager			pluginManager;
	private static IMute			muteStorage;
	private static IDeaf			deafStorage;
	public static final String		VERSION	= "RPchat v 2.0.1w(3u1t1u) (singularity)";
	public static FileConfiguration	config;
	public static File yamlFile;
	public static YamlConfiguration yaml;
	public static final boolean		DEBUG_MODE	= true; // выключить в релизе
	@Override
	public void onEnable() {
		pluginManager = Bukkit.getPluginManager();
		if (pluginManager.getPlugin("PermissionsEx") != null) {
			Util.usePEX = true;
		} else if (pluginManager.getPlugin("PermissionsBukkit") != null) {
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
		ChanelRegister.init();
		File file = new File(getDataFolder(), "storage.yml");
		Main.yamlFile =file;
		if (Main.yamlFile.exists()) {
			Main.yaml = YamlConfiguration.loadConfiguration(file);
		} else {
			Main.yaml = new YamlConfiguration();
		}
		getServer().getPluginManager().registerEvents(new TabListener(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		Main.muteStorage = new Mute();
		Main.deafStorage = new Deaf();
		RPChatCommandExecutor executer = new RPChatCommandExecutor();
		getCommand("rpchat").setExecutor(executer);
		getCommand("mute").setExecutor(executer);
		getCommand("unmute").setExecutor(executer);
		getCommand("deaf").setExecutor(executer);
		getCommand("undeaf").setExecutor(executer);
		getCommand("sw").setExecutor(executer);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		
		}

	@Override
	public void onDisable() {
		muteStorage.saveMute();
		//deafStorage.saveDeaf();
	}

	public static IMute getBanStorage() {
		return muteStorage;
	}
	public static IDeaf getDeafStorage() {
		return deafStorage;
	}
}