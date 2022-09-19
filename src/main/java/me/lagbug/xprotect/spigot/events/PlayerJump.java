package me.lagbug.xprotect.spigot.events;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.Utils;

/**
 * This class is used to verify the player in the jump captcha
 *
 * @version 1.1
 */
public class PlayerJump implements Listener {

	// Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@EventHandler
	public void onPlayerStatisticIncrement(PlayerStatisticIncrementEvent e) {
		if (e.getStatistic() != Statistic.JUMP) {
			return;
		}

		Player player = e.getPlayer();

		// If the player is not under the verification process we return
		if (!plugin.getToVerify().containsKey(player)) {
			return;
		}
		// If the random generated code from the plugin matches the key we verify the player
		if (plugin.getToVerify().get(player).equals(plugin.getCode() + "jump")) {
			Utils.captchaSuccess(player);
		}
	}
}