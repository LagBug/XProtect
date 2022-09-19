package me.lagbug.xprotect.bungee;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import me.lagbug.xprotect.bungee.events.PreLogin;
import me.lagbug.xprotect.bungee.utils.FileUtils;
import me.lagbug.xprotect.global.MySQL;
import me.lagbug.xprotect.global.ProtectionValues;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class XProtectBungee extends Plugin  {

	private static XProtectBungee instance;
	private final FileUtils fileUtils = new FileUtils();
	public boolean mysql = false;
	
	@Override
	public void onEnable() {
		System.out.println("WARNING ! This version of XProtect for Bungeecord is not supported and will NOT work at it's current state ! WARNING");
		
		instance = this;
		fileUtils.initiate(this, "config.yml", "antibot.yml", "lang/en_US.yml", "data/data.yml", "data/blacklist.yml", "data/whitelist.yml");

		initiate();
	}

	public void initiate() {
		mysql = getConfigFile().getString("storage.type").equals("MYSQL");

		if (mysql) {
			getProxy().getScheduler().runAsync(this, () -> {
				if (getConfigFile().getString("storage.mysql.host").isEmpty()) {
					System.out.println("[XProtect] The database information seems to be empty. Disabled the plugin.");
					return;
				}
				
				MySQL.initiate(getConfigFile().getString("storage.mysql.host"),
						getConfigFile().getString("storage.mysql.database"),
						Arrays.asList(getConfigFile().getString("storage.mysql.tables.verified"),
								getConfigFile().getString("storage.mysql.tables.blacklisted")),
						getConfigFile().getString("storage.mysql.username"), getConfigFile().getString("storage.mysql.password"),
						getConfigFile().getString("storage.mysql.statement"), getConfigFile().getInt("storage.mysql.port"));

				
				if (!MySQL.connect()) {
					System.out.println("[XProtect] Not able to connect to MySQL. Switched to FLAT file support.");
					mysql = false;
				}
			});
		}
		
		getProxy().getPluginManager().registerListener(this, new PreLogin());
		getProxy().getScheduler().schedule(this, ProtectionValues::resetTask, 0, getAntibotFile().getInt("antibot.joinLimiter.seconds"), TimeUnit.SECONDS);
	}

	public static XProtectBungee getInstance() {
		return instance;
	}

	public Configuration getConfigFile() {
		return fileUtils.getFile("config.yml");
	}
	
	public Configuration getAntibotFile() {
		return fileUtils.getFile("antibot.yml");
	}
	
	public Configuration getWhitelistFile() {
		return fileUtils.getFile("data/whitelist.yml");
	}
	
	public Configuration getBlacklistFile() {
		return fileUtils.getFile("data/blacklist.yml");
	}
	
	public void saveFile(String path) {
		fileUtils.saveFile(path);
	}
	
	public String getMessage(String path) {
		return ChatColor.translateAlternateColorCodes('&', fileUtils.getFile("lang/" + getConfigFile().getString("languageFile") + ".yml").getString(path));
	}
}
