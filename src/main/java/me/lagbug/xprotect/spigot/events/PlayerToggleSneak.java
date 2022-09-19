package me.lagbug.xprotect.spigot.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.Utils;

/**
 * This class is used to verify the player in the sneak captcha
 *
 * @version 1.0
 */
public class PlayerToggleSneak implements Listener {

	// Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@EventHandler
	public void onPlayerJoin(PlayerToggleSneakEvent e) {
		Player player = e.getPlayer();

		// If the player is not under the verification process we return
		if (!plugin.getToVerify().containsKey(player)) {
			return;
		}

		// If the player is sneaking and the random generated code from the plugin
		// matches the key we verify the player
		if (e.isSneaking() && plugin.getToVerify().get(player).equals(plugin.getCode() + "sneak")) {
			Utils.captchaSuccess(player);
		}
	}
}