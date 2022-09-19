package me.lagbug.xprotect.spigot;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.lagbug.xprotect.spigot.events.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.lagbug.xprotect.global.MySQL;
import me.lagbug.xprotect.global.ProtectionValues;
import me.lagbug.xprotect.spigot.captchas.Code;
import me.lagbug.xprotect.spigot.captchas.Color;
import me.lagbug.xprotect.spigot.captchas.Different;
import me.lagbug.xprotect.spigot.captchas.Drop;
import me.lagbug.xprotect.spigot.captchas.Equation;
import me.lagbug.xprotect.spigot.captchas.Gui;
import me.lagbug.xprotect.spigot.captchas.Jump;
import me.lagbug.xprotect.spigot.captchas.Picture;
import me.lagbug.xprotect.spigot.captchas.Reverse;
import me.lagbug.xprotect.spigot.captchas.Slot;
import me.lagbug.xprotect.spigot.captchas.Sneak;
import me.lagbug.xprotect.spigot.commands.ProtectCommand;
import me.lagbug.xprotect.spigot.common.utils.general.Metrics;
import me.lagbug.xprotect.spigot.common.utils.general.UpdateChecker;
import me.lagbug.xprotect.spigot.common.utils.general.UpdateChecker.UpdateResult;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;
import me.lagbug.xprotect.spigot.common.utils.util.FileUtils;
import me.lagbug.xprotect.spigot.utils.NotificationsTask;
import me.lagbug.xprotect.spigot.utils.PlayerDataCache;
import me.lagbug.xprotect.spigot.utils.SpigotConsoleFilter;
import me.lagbug.xprotect.spigot.utils.SpigotGlobalSettings;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

public class XProtect extends JavaPlugin {

	private final Map<Player, String> toVerify = new HashMap<>();
	private final Map<UUID, Integer> failedCaptchas = new HashMap<>();
	private final Map<Player, ItemStack> lastItem = new HashMap<>();
	private final Map<Player, OfflinePlayer> editing = new HashMap<>();

	private final List<Captcha> captchas = new ArrayList<>();

	private final FileUtils fileUtils = new FileUtils();
	private FileConfiguration configFile, langFile;
	private final String user = "%%__USER__%%";
	private String code = "";

	public boolean mysql, authme, protocol;
	public UpdateResult updateResult = null;

	@Override
	public void onEnable() {
		// We register our only command
		getCommand("protect").setExecutor(new ProtectCommand());
		
		// We register all the events we are going use
		Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
		Bukkit.getPluginManager().registerEvents(new AsyncPlayerChat(), this);
		Bukkit.getPluginManager().registerEvents(new AsyncPlayerPreLogin(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerToggleSneak(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJump(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDropItem(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerItemConsume(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerItemHeld(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClose(), this);
		Bukkit.getPluginManager().registerEvents(new BlockingListener(), this);

		// We initiate everything that's required
		initiate(false);

		CommonUtils.forceLog(getDescription().getName() + " v" + getDescription().getVersion() + " has been enabled successfully", "Plugin licensed to [https://www.spigotmc.org/members/" + user + "/]");
		// We register the bStats Metrics
		new Metrics(this);

		// If the update checker is enabled, we schedule it
		if (configFile.getBoolean("updateChecker")) {
			new UpdateChecker(this, 67863).schedule(600);
		}
	}

	@Override
	public void onDisable() {
		// We save any unsaved data without registering a new task
		PlayerDataCache.save(false);
		
		// If any player is under the verification process, we kick them
		for (Player p : toVerify.keySet()) {
			p.kickPlayer(getMessage("kick.restart"));
		}
		
		// If any player has an open inventory, we close it
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.closeInventory();
		}

		// If MySQL is enabled, we close the connection
		if (mysql) {
			try {
				MySQL.getConnection().close();
			} catch (SQLException e) {
				CommonUtils.log("Could not close the MySQL connection");
			}	
		}
		
		// If ProtocolLib support is enabled, we unregister our Listener class
		if (protocol) {
			PacketHandshake.unregister();
		}
		
		CommonUtils.forceLog(getDescription().getName() + " v" + getDescription().getVersion() + " has been disabled successfully");
	}

	public void initiate(boolean isReload) {
		// If a reload has occurred, we reset the captchas
		if (!captchas.isEmpty()) {	
			captchas.clear();
		}

		// We initialize the FilUtils
		fileUtils.initiate(this, "config.yml", "chat.yml", "antibot.yml", "captchas.yml", "files/bad_words.txt",
				"data/data.yml", "data/blacklist.yml", "data/whitelist.yml", "guis/home.yml",
				"guis/managers/server_manager.yml", "guis/managers/player_manager.yml", "lang/en_US.yml");

		// Set a random code to prevent players from bypassing captchas
		code = CommonUtils.randomString(6) + "-";

		// Setting the main configuration files
		configFile = fileUtils.getFile("config.yml");
		langFile = fileUtils.getFile("lang/" + configFile.getString("languageFile") + ".yml");
		
		// Initiating the CommonUtils
		CommonUtils.initiate(this, configFile.getBoolean("debug"));

		// Setting some global booleans
		mysql = configFile.getString("storage.type").equals("MYSQL");
		authme = !configFile.getBoolean("dependencies.AuthMe.forceDisable") && CommonUtils.isPluginEnabled("AuthMe");
		protocol = !configFile.getBoolean("dependencies.ProtocolLib.forceDisable") && CommonUtils.isPluginEnabled("ProtocolLib");

		// If MySQL is enabled we try to connect asynchronously
		if (mysql && !isReload) {
			Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
				// We initiate a new MySQL object with the login information
				MySQL.initiate(configFile.getString("storage.mysql.host"),
						configFile.getString("storage.mysql.database"),
						Arrays.asList(configFile.getString("storage.mysql.tables.verified"),
								configFile.getString("storage.mysql.tables.blacklisted")),
						configFile.getString("storage.mysql.username"), configFile.getString("storage.mysql.password"),
						configFile.getString("storage.mysql.statement"), configFile.getInt("storage.mysql.port"));

				// If the connection was not successful we automatically which to FLAT file support
				if (!MySQL.connect()) {
					mysql = false;
					CommonUtils.forceLog("Automatically switched to FLAT file support");
				}
				
				PlayerDataCache.initiate();
			});
		} else {
			PlayerDataCache.initiate();
		}

		// If AuthMe is installed we add support for it
		if (authme) {
			Bukkit.getPluginManager().registerEvents(new AuthMeLogin(), this);
			CommonUtils.forceLog("Using AuthMe as an authentication plugin");
		}
		

		// If ProtocolLib is installed we add support for it to increase performance
		if (protocol) {
			new PacketHandshake();
			CommonUtils.forceLog("Using ProtocolLib for maximum protection");
		}

		// We register our tasks
		registerTasks();

		// If the console-filer is enabled, we add it on the root logger
		if (getAntibotFile().getBoolean("onAttack.cleanConsole")) {
			Logger logger = ((Logger) LogManager.getRootLogger());
			logger.addFilter(new SpigotConsoleFilter());
			CommonUtils.forceLog("Using console filter while under attack");
		}
		
		//If the captchas are enabled we add those that are enabled in an ArrayList
		if (getCaptchasFile().getBoolean("settings.enabled")) {
			captchas.addAll(Arrays.asList(new Picture(), new Gui(), new Different(), new Slot(), new Drop(), new Jump(),
					new me.lagbug.xprotect.spigot.captchas.Map(), new Equation(), new Code(), new Color(), new Sneak(), new Reverse()));

			// We remove those that are not enabled
			for (String key : getCaptchasFile().getConfigurationSection("captchas").getKeys(false)) {
				boolean enabled = getCaptchasFile().getBoolean("captchas." + key + ".enabled");

				for (int i = 0; i < captchas.size(); i++) {
					if (captchas.get(i).getClass().getSimpleName().toLowerCase().equals(key) && !enabled) {
						captchas.remove(i);
					}
				}
			}
		}
		CommonUtils.forceLog("Registered " + captchas.size() + " captcha types");
		new SpigotGlobalSettings();
	}

	private void registerTasks() {
		// If actionbar alerts are enabled, we schedule a task for them
		if (getAntibotFile().getBoolean("onAttack.actionbar")) {
			new NotificationsTask().runTaskTimer(this, 20, 20);
		}
		
		// Register any tasks required
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, ProtectionValues::resetTask, 0, 20L * getAntibotFile().getInt("antibot.joinLimiter.seconds"));
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, ProtectionValues::lastPlayersTask, 0, 20); //Every second
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> PlayerDataCache.save(true), 20 * 5, 20L * 60 * getConfigFile().getInt("saveDataEvery"));
	}

	public List<Captcha> getCaptchas() {
		return captchas;
	}

	public Map<Player, String> getToVerify() {
		return toVerify;
	}

	public Map<Player, ItemStack> getLastItem() {
		return lastItem;
	}

	public Map<Player, OfflinePlayer> getEditing() {
		return editing;
	}
	
	public Map<UUID, Integer> getFailedCaptchas() {
		return failedCaptchas;
	}

	public String getMessage(String path) {
		return langFile.contains(path)
				? ChatColor.translateAlternateColorCodes('&',
						langFile.getString(path).replace("%prefix%", configFile.getString("prefix")))
				: "Error: The specified path (lang/../" + path + ") could not be found.";
	}

	public String getCode() {
		return code;
	}

	public String getUser() {
		return user;
	}

	public FileConfiguration getConfigFile() {
		return configFile;
	}

	public FileConfiguration getChatFile() {
		return getFile("chat.yml");
	}

	public FileConfiguration getAntibotFile() {
		return getFile("antibot.yml");
	}

	public FileConfiguration getCaptchasFile() {
		return getFile("captchas.yml");
	}

	public FileConfiguration getDataFile() {
		return getFile("data/data.yml");
	}

	public FileConfiguration getWhitelistFile() {
		return getFile("data/whitelist.yml");
	}

	public FileConfiguration getBlacklistFile() {
		return getFile("data/blacklist.yml");
	}

	public YamlConfiguration getFile(String path) {
		return fileUtils.getFile(path);
	}

	public void saveFile(String path) {
		fileUtils.saveFile(path);
	}

	public void reloadFiles() {
		fileUtils.reloadFiles();
	}
}