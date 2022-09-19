package me.lagbug.xprotect.spigot.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.antibot.apis.IpInfo;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.utils.general.UpdateChecker.UpdateResult;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;

public class PlaceholderManager {

	private static final XProtect plugin = XProtect.getPlugin(XProtect.class);
	private static final List<Placeholder> placeholders = new ArrayList<>();

	public static List<Placeholder> get() {
		if (!placeholders.isEmpty()) {
			placeholders.clear();
		}

		long free = Runtime.getRuntime().freeMemory() / 1024L / 1024L;
		long max = Runtime.getRuntime().maxMemory() / 1024L / 1024L;
		long used = (max - free);

		// Plugin
		add("%plugin_author%", plugin.getDescription().getAuthors().get(0));
		add("%plugin_version%", plugin.getDescription().getVersion());
		add("%version_status%", plugin.updateResult == UpdateResult.FOUND ? "Outdated" : "Latest");

		// Statistics
		add("%bots_blocked%", CommonUtils.formatNumber(DataValues.BLOCKED_BOTS));
		add("%connections_checked%", CommonUtils.formatNumber(DataValues.CHECKED_CONNECTIONS));
		add("%successful_captchas%", CommonUtils.formatNumber(DataValues.SUCCESSFUL_CAPTCHAS));
		add("%failed_captchas%", CommonUtils.formatNumber(DataValues.FAILED_CAPTCHAS));

		// Performance
		add("%used_ram%", used + "MB");
		add("%max_ram%", max + "MB");
		add("%free_ram%", free + "MB");
		add("%cpu_processors%", Runtime.getRuntime().availableProcessors());
		add("%tps%", CommonUtils.getTPS());

		// Server
		add("%server_version%", Bukkit.getVersion());
		add("%online_players%", Bukkit.getOnlinePlayers().size());
		add("%max_players%", Bukkit.getMaxPlayers());
		add("%banned_players%", Bukkit.getBannedPlayers().size() + Bukkit.getIPBans().size());
		add("%whitelist_status%", Bukkit.hasWhitelist());
		add("%idle_timeout%", Bukkit.getIdleTimeout());
		add("%is_primary_thread%", Bukkit.isPrimaryThread());
		add("%active_workers%", Bukkit.getScheduler().getActiveWorkers().size());
		add("%pending_tasks%", Bukkit.getScheduler().getPendingTasks().size());
		add("%max_slots%", Bukkit.getMaxPlayers());

		return placeholders;
	}

	public static List<Placeholder> get(OfflinePlayer player) {
		add("%player_name%", player.getName());
		add("%player%", player.getName());
		add("%player_uuid%", player.getUniqueId().toString());
		add("%player_is_op%", player.isOp());

		Checker checker = new Checker(player.getUniqueId());
		add("%player_verified%", checker.isVerified());
		add("%player_blacklisted%", checker.isBlacklisted());

		if (player.isOnline()) {
			Player onlinePlayer = player.getPlayer();
			String address = onlinePlayer.getAddress().getAddress().getHostAddress();
			add("%player_ip%", address);

			add("%statistic_deaths%", onlinePlayer.getStatistic(Statistic.DEATHS));
			add("%statistic_time_since_death%", formatTime(onlinePlayer.getStatistic(Statistic.TIME_SINCE_DEATH) / 20));
			add("%statistic_damage_taken%", onlinePlayer.getStatistic(Statistic.DAMAGE_TAKEN));
			add("%statistic_damage_dealt%", onlinePlayer.getStatistic(Statistic.DAMAGE_DEALT));
			add("%statistic_player_kills%", onlinePlayer.getStatistic(Statistic.PLAYER_KILLS));
			add("%statistic_leave_game%", onlinePlayer.getStatistic(Statistic.LEAVE_GAME));
			add("%statistic_time_played%", formatTime(
					onlinePlayer.getStatistic(Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9")
							|| Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11")
							|| Bukkit.getVersion().contains("1.12") ? Statistic.valueOf("PLAY_ONE_TICK")
									: Statistic.PLAY_ONE_MINUTE)
							/ 20));

			Location location = onlinePlayer.getLocation();
			add("%player_world%", location.getWorld().getName());
			add("%player_x%", Math.round(location.getX()));
			add("%player_y%", Math.round(location.getY()));
			add("%player_z%", Math.round(location.getZ()));

			add("%player_health%", onlinePlayer.getHealth());
			add("%player_food_level%", (double) onlinePlayer.getFoodLevel());

			JSONObject detection = IpInfo.getInformation(address);

			add("%player_bot_status%", detection.get("bot"));
			add("%player_vpn%", detection.get("vpn"));
			add("%player_fraud_score%", detection.get("fraudScore"));
			add("%player_recent_abuse%", detection.get("abusive"));
			add("%player_country%", detection.get("country"));
			add("%player_city%", detection.get("city"));
			add("%player_region%", detection.get("region"));
			add("%player_organisation%", detection.get("organisation"));
		} else {
			add("%player_world%", "N/A");
			add("%player_x%", "N/A");
			add("%player_y%", "N/A");
			add("%player_z%", "N/A");

			add("%player_health%", "N/A");
			add("%player_food_level%", "N/A");
		}

		return placeholders;
	}

	public static String formatTime(int secs) {
		int remainder = secs % 86400;

		int days = secs / 86400;
		int hours = remainder / 3600;
		int minutes = (remainder / 60) - (hours * 60);
		int seconds = (remainder % 3600) - (minutes * 60);

		if (days > 0) {
			return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
		} else if (hours > 0) {
			return hours + "h " + minutes + "m " + seconds + "s";
		} else if (minutes > 0) {
			return minutes + "m " + seconds + "s";
		} else {
			return seconds + "s";
		}
	}

	private static void add(String key, String value) {
		placeholders.add(new Placeholder(key, value));
	}

	private static void add(String key, Object value) {
		add(key, String.valueOf(value));
	}

	public static class Placeholder {
		private String key;
		private String value;

		public Placeholder(String key, String value) {
			setKey(key);
			setValue(value);
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
