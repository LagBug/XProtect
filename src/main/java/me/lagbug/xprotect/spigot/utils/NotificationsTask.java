package me.lagbug.xprotect.spigot.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.global.ProtectionValues;
import me.lagbug.xprotect.global.ProtectionValues.LockState;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.utils.communication.Actionbar;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;

/**
 * Run the notifications task to notify about an attack
 *
 * @version 1.0
 */
public class NotificationsTask extends BukkitRunnable {

	// The instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	// The last address blocked by the plugin
	private static String address = "";

	@Override
	public void run() {
		// If the server is not locked or if there's no one online, we return
		if (ProtectionValues.LOCK_STATE == LockState.OPEN || Bukkit.getOnlinePlayers().isEmpty()) {
			return;
		}

		// Looping through each player
		for (Player player : Bukkit.getOnlinePlayers()) {
			// If the player is online and has the required permission
			if (player != null && player.isOnline() && player.hasPermission(Permissions.NOTIFY)) {
				// We send the actionbar message
				Actionbar.send(player,
						plugin.getMessage("general.underAttack")
								.replace("%total_bots%", CommonUtils.formatNumber(ProtectionValues.TOTAL_PLAYERS))
								.replace("%current_bots%", CommonUtils.formatNumber(ProtectionValues.LAST_PLAYERS))
								.replace("%address%", address));
			}
		}
	}

	// Sets the last address
	public static void setLastAddress(String newAddress) {
		address = newAddress;
	}
}