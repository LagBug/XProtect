package me.lagbug.xprotect.global;

import me.lagbug.xprotect.global.enums.PlayerStatus;
import me.lagbug.xprotect.spigot.utils.PlayerDataCache;
import org.bukkit.OfflinePlayer;

import java.net.SocketAddress;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.UUID;

/**
 * This class contains useful methods to get if a player is verified or do
 * actions like verify/unverify the player.
 *
 * @version 1.0
 */
public class Checker {

	// The required information for the player
	private String uuid, name, address;

	// The constructor to set the UUID
	public Checker(UUID uuid) {
		this.uuid = uuid.toString();
	}

	// The constructor to set the info through an OfflinePlayer
	public Checker(OfflinePlayer player) {
		this(player.getUniqueId());
		this.name = player.getName();

		if (player.isOnline()) {
			this.address = player.getPlayer().getAddress().getAddress().getHostAddress();
		}
	}

	// The constructor to set the required information
	public Checker(OfflinePlayer player, String address) {
		this(player);
		this.address = address;
	}

	// The constructor using only an IP
	public Checker(SocketAddress address) {
		this.address = address.toString();
	}

	// The constructor using only an IP
	public Checker(String name) {
		this.name = name;
	}

	/*
	 * This method is used to get whether a player is verified/blacklisted or not.
	 * If MYSQL is enabled, it will retrieve that through the database. Otherwise,
	 * it'll use YAML files.
	 * 
	 * @return - if the player is verified
	 */
	public boolean is(PlayerStatus status) {
		return contains(status);
	}

	public boolean isVerified() {
		return is(PlayerStatus.VERIFIED);
	}

	public boolean isBlacklisted() {
		return is(PlayerStatus.BLACKLISTED);
	}

	/*
	 * Unverifies or removes a player from the blacklist. In other words it removes
	 * them from the list of verified players.
	 */
	public void un(PlayerStatus status) {
		getPlayerData(status).remove(uuid);
	}

	public void unVerify() {
		un(PlayerStatus.VERIFIED);
	}

	public void unBlacklist() {
		un(PlayerStatus.BLACKLISTED);
	}

	/*
	 * Verifies or blacklists a player. In other words it adds them to the list of
	 * verified players.
	 */
	public void force(PlayerStatus status) {
		getPlayerData(status).put(uuid, new PlayerData(name, address));
	}

	public void forceVerify() {
		force(PlayerStatus.VERIFIED);
	}

	public void forceBlacklist() {
		force(PlayerStatus.BLACKLISTED);
	}


	private boolean contains(PlayerStatus status) {
		Map<String, PlayerData> playerData = getPlayerData(status);

		try {
			for (String key : playerData.keySet()) {
				PlayerData data = playerData.get(key);

				if (uuid != null && key.equals(uuid)) {
					return true;
				}

				if (!address.equals("127.0.0.1") && data.getAddress() != null && data.getAddress().equals(address)) {
					return true;
				}

				if (name != null && data.getUsername() != null && data.getUsername().equals(name)) {
					return true;
				}
			}
		} catch (ConcurrentModificationException | NullPointerException ex) {
			return false;
		}
		return false;
	}

	private Map<String, PlayerData> getPlayerData(PlayerStatus status) {
		if (status.equals(PlayerStatus.VERIFIED)) {
			return PlayerDataCache.getWhitelistData();
		} else {
			return PlayerDataCache.getBlacklistData();
		}
	}
	
	public static class PlayerData {
		private final String username;
		private final String address;

		public PlayerData(String username, String address) {
			this.username = username;
			this.address = address;
		}

		public String getUsername() {
			return username;
		}

		public String getAddress() {
			return address;
		}
	}
}
