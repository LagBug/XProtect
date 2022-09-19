package me.lagbug.xprotect.spigot.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import me.lagbug.xprotect.global.Checker.PlayerData;
import me.lagbug.xprotect.global.MySQL;
import me.lagbug.xprotect.global.ProtectionValues;
import me.lagbug.xprotect.global.ProtectionValues.LockState;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;

public class PlayerDataCache {

	private static final XProtect plugin = XProtect.getPlugin(XProtect.class);

	private static final HashMap<String, PlayerData> whitelistData = new HashMap<>();
	private static HashMap<String, PlayerData> whitelistDataCopy = new HashMap<>();
	private static final HashMap<String, PlayerData> blacklistData = new HashMap<>();
	private static HashMap<String, PlayerData> blacklistDataCopy = new HashMap<>();

	// Adds the data from the MySQL/FLAT to the HashMaps
	public static void initiate() {
		if (!whitelistData.isEmpty()) {
			whitelistData.clear();
		}

		if (!blacklistData.isEmpty()) {
			blacklistData.clear();
		}

		if (plugin.mysql) {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				String whitelistTable = plugin.getConfigFile().getString("storage.mysql.tables.verified");
				String blacklistTable = plugin.getConfigFile().getString("storage.mysql.tables.blacklisted");

				try {
					if (MySQL.prepareStatement("SELECT * FROM " + whitelistTable + ";").executeQuery().next()) {
						ResultSet result = MySQL.executeQuery("SELECT * FROM " + whitelistTable + ";");

						while (result.next()) {
							whitelistData.put(result.getString("player_uuid"),
									new PlayerData(result.getString("player_name"), result.getString("player_ip")));
						}

						whitelistDataCopy = copy(whitelistData);
					}

					if (MySQL.prepareStatement("SELECT * FROM " + blacklistTable + ";").executeQuery().next()) {
						ResultSet result = MySQL.executeQuery("SELECT * FROM " + blacklistTable + ";");

						while (result.next()) {
							blacklistData.put(result.getString("player_uuid"),
									new PlayerData(result.getString("player_name"), result.getString("player_ip")));
						}
						blacklistDataCopy = copy(blacklistData);
					}
				} catch (SQLException ex) {
					CommonUtils.log("Exception thrown while trying to read player data from database.");
				}
			});
		} else {
			try {
				FileConfiguration whitelistFile = plugin.getWhitelistFile();
				FileConfiguration blacklistFile = plugin.getBlacklistFile();

				for (String key : whitelistFile.getKeys(false)) {
					whitelistData.put(key, new PlayerData(whitelistFile.getString(key + ".name"),
							whitelistFile.getString(key + ".address")));
				}

				for (String key : blacklistFile.getKeys(false)) {
					blacklistData.put(key, new PlayerData(blacklistFile.getString(key + ".name"),
							blacklistFile.getString(key + ".address")));
				}

				whitelistDataCopy = copy(whitelistData);
				blacklistDataCopy = copy(blacklistData);
			} catch (ConcurrentModificationException | NullPointerException ignored) {
				CommonUtils.log("Exception thrown while trying to read player data from files.");
			}
		}
	}

	public static HashMap<String, PlayerData> copy(HashMap<String, PlayerData> original) {
		HashMap<String, PlayerData> copy = new HashMap<>();
		for (Map.Entry<String, PlayerData> entry : original.entrySet()) {
			copy.put(entry.getKey(), entry.getValue());
		}
		return copy;
	}

	// Saves the data
	public static void save(boolean async) {
		// If the server is under attack we return to improve performance
		if (ProtectionValues.LOCK_STATE != LockState.OPEN) {
			return;
		}

		if (whitelistDataCopy.equals(whitelistData) && blacklistDataCopy.equals(blacklistData)) {
			return;
		}

		whitelistDataCopy = copy(whitelistData);
		blacklistDataCopy = copy(blacklistData);

		if (plugin.mysql) {
			if (async) {
				Bukkit.getScheduler().runTaskAsynchronously(plugin, PlayerDataCache::saveMySQLData);
			} else {
				saveMySQLData();
			}
		} else {
			// Getting the required files
			try {
				FileConfiguration[] files = { plugin.getWhitelistFile(), plugin.getBlacklistFile() };

				// Removing any un-verified players
				for (FileConfiguration file : files) {
					Map<String, PlayerData> map = getData(file);

					for (String key : file.getKeys(false)) {
						if (!map.containsKey(key)) {
							file.set(key, null);
						}
					}

					for (String key : map.keySet()) {
						PlayerData data = map.get(key);
						if (!file.contains(key)) {
							file.set(key + ".name", data.getUsername());
							file.set(key + ".address", data.getAddress());
						}
					}
				}

				// Saving the files
				plugin.saveFile("data/whitelist.yml");
				plugin.saveFile("data/blacklist.yml");
			} catch (ConcurrentModificationException | NullPointerException ignored) {
				CommonUtils.log("Exception thrown while trying to read player data from files");
			}
		}

	}

	private static void saveMySQLData() {
		String[] tables = { plugin.getConfigFile().getString("storage.mysql.tables.verified"),
				plugin.getConfigFile().getString("storage.mysql.tables.blacklisted") };

		try {
			for (String table : tables) {
				Map<String, PlayerData> map = getData(table);

				if (MySQL.prepareStatement("SELECT * FROM " + table + ";").executeQuery().next()) {
					ResultSet result = MySQL.executeQuery("SELECT * FROM " + table + ";");

					while (result.next()) {
						String uuid = result.getString("player_uuid");
						if (!map.containsKey(uuid)) {
							MySQL.prepareStatement("DELETE FROM " + table + " WHERE player_uuid = '" + uuid + "';")
									.executeUpdate();
						}
					}
				}

				for (String key : map.keySet()) {
					PlayerData data = map.get(key);

					if (!MySQL.prepareStatement("SELECT * FROM " + table + " WHERE player_uuid = '" + key + "';")
							.executeQuery().next()) {
						MySQL.prepareStatement(
								"INSERT INTO " + table + " (player_uuid, player_name, player_ip, date) VALUES('" + key
										+ "','" + data.getUsername() + "','" + data.getAddress() + "','"
										+ CommonUtils.getDate() + "');")
								.executeUpdate();
					}
				}
			}
		} catch (SQLException ex) {
			CommonUtils.log("Exception thrown while trying to read player data from database");
		}
	}

	private static Map<String, PlayerData> getData(String table) {
		return table.equals(plugin.getConfigFile().getString("storage.mysql.tables.verified")) ? whitelistData
				: blacklistData;
	}

	private static Map<String, PlayerData> getData(FileConfiguration file) {
		return file.equals(plugin.getWhitelistFile()) ? whitelistData : blacklistData;
	}

	public static Map<String, PlayerData> getWhitelistData() {
		return whitelistData;
	}

	public static Map<String, PlayerData> getBlacklistData() {
		return blacklistData;
	}
}
