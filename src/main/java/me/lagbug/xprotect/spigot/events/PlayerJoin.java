package me.lagbug.xprotect.spigot.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.xephi.authme.api.v3.AuthMeApi;
import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.captcha.CaptchaExecutor;

public class PlayerJoin implements Listener {

	// The main instance of the plugin
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		// If captchas are disabled, we return here
		if (!plugin.getCaptchasFile().getBoolean("settings.enabled")) {
			return;
		}

		// If the value in the config file is true and the player has the bypass
		// permission, a captcha won't be asked
		if (plugin.getCaptchasFile().getBoolean("settings.bypass")
				&& (player.hasPermission(Permissions.BYPASS) || player.isOp())) {
			new Checker(player).forceVerify();
			return;
		}

		// If the player is verified, or captchas on join are disabled, we return
		if (!plugin.getCaptchasFile().getBoolean("requireCaptcha.onJoin") || new Checker(player).isVerified()) {
			return;
		}

		// If AuthMe is enabled and the player is authenticated/registered we return
		if (plugin.authme && (!AuthMeApi.getInstance().isAuthenticated(player)
				|| !AuthMeApi.getInstance().isRegistered(player.getName()))) {
			return;
		}

		// Otherwise we cancel the join message and initialize a CaptchaExecutor
		e.setJoinMessage(null);
		new CaptchaExecutor(player);
	}
}