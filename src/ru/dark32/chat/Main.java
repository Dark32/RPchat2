package ru.dark32.chat;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.dark32.chat.chanels.ChanelRegister;
import ru.dark32.chat.chanels.ChatListener;

public class Main extends JavaPlugin {

	public static final Logger		LOG			= Logger.getLogger("Minecraft");
	public PluginManager			pluginManager;
	private static IMute			muteStorage;
	private static IDeaf			deafStorage;
	public static final String		VERSION		= "RPchat v 2.0.5w 1u";
	public static FileConfiguration	config;
	public static File				storageFile;
	public static YamlConfiguration	storage;
	
	public static File				chatConfigFile;
	public static YamlConfiguration	chatConfig;
	
	public static File				localeConfigFile;
	public static YamlConfiguration	localeConfig;
	// выключить в релизе
	public static final boolean		DEBUG_MODE	= !true;
	public static final String		BASE_PERM	= "mcnw";

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

		config = this.getConfig();
		Util.init(this);
		ValueStorage.init();
		ChanelRegister.init();
		
		Main.storageFile =  new File(getDataFolder(), "storage.yml");
		if (Main.storageFile.exists()) {
			Main.storage = YamlConfiguration.loadConfiguration(storageFile);
		} else {
			Main.storage = new YamlConfiguration();
		}
		
		String chat = config.getString("chat","chat");
		Main.chatConfigFile =  new File(getDataFolder(), chat+".yml");
		if (Main.chatConfigFile.exists()) {
			Main.chatConfig = YamlConfiguration.loadConfiguration(chatConfigFile);
		} else {
			getLogger().warning("chat.yml not found");
		}
		
		String locale = config.getString("locale","locale");
		Main.localeConfigFile =  new File(getDataFolder(), locale+".yml");
		if (Main.localeConfigFile.exists()) {
			Main.localeConfig = YamlConfiguration.loadConfiguration(chatConfigFile);
		} else {
			getLogger().warning("chat.yml not found");
		}
		
		getServer().getPluginManager().registerEvents(new TabListener(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		Main.muteStorage = new Mute();
		Main.deafStorage = new Deaf();
		final RPChatCommandExecutor executer = new RPChatCommandExecutor();
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
		// deafStorage.saveDeaf();
	}

	public static IMute getBanStorage() {
		return muteStorage;
	}

	public static IDeaf getDeafStorage() {
		return deafStorage;
	}
}