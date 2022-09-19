package me.lagbug.xprotect.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.captcha.CaptchaExecutor;

/**
 * Used to hook with the AuthMe plugin
 *
 * @version 1.0
 */
public class AuthMeLogin implements Listener {

	// Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	// Getting the instance of the API
	private final static AuthMeApi authme = AuthMeApi.getInstance();

	@EventHandler
	public void onLogin(LoginEvent e) {
		Player player = e.getPlayer();

		Bukkit.getScheduler().runTask(plugin, () -> {
			// If captchas are not enabled, we return
			if (!plugin.getCaptchasFile().getBoolean("settings.enabled")
					|| !plugin.getCaptchasFile().getBoolean("requireCaptcha.onJoin")) {
				return;
			}

			// If the player is verified, we return
			if (new Checker(player).isVerified()) {
				return;
			}
			
			// If the player can bypass the captcha we return
			if (plugin.getCaptchasFile().getBoolean("settings.bypass") && player.hasPermission(Permissions.BYPASS)) {
				return;
			}
			
			// And lastly if the user is authenticated or registered in AuthMe we return as well
			if (plugin.authme && !authme.isAuthenticated(player) || !authme.isRegistered(player.getName())) {
				return;
			}

			// Otherwise we send a captcha
			new CaptchaExecutor(e.getPlayer());
		});
	}

	public static boolean isAuthenticated(Player player) {
		return authme.isAuthenticated(player) || authme.isRegistered(player.getName());
	}
}