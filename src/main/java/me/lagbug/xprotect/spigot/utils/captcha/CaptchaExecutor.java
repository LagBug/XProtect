package me.lagbug.xprotect.spigot.utils.captcha;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.captchas.Different;
import me.lagbug.xprotect.spigot.captchas.Drop;
import me.lagbug.xprotect.spigot.captchas.Gui;
import me.lagbug.xprotect.spigot.captchas.Map;
import me.lagbug.xprotect.spigot.captchas.Picture;
import me.lagbug.xprotect.spigot.common.utils.util.ActionUtil;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;

/**
 * This class is used to put players under a captcha test using easy-to-use
 * methods.
 *
 * @version 1.0
 */
public class CaptchaExecutor {

	// Get the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	// The constructor to execute the captcha
	public CaptchaExecutor(Player player) {
		// The delay the captcha should be sent after
		int delay = plugin.getCaptchasFile().getInt("settings.delay");

		// If the delay is more that 0, we create a task
		if (delay > 0) {
			Bukkit.getScheduler().runTaskLater(plugin, () -> execute(player, "withEnabled"), 20 * delay);
			return;
		}

		// Otherwise we execute the captcha instantly
		player.closeInventory();
		execute(player, "withEnabled");
	}

	// Constructor used to also specify the captcha type
	public CaptchaExecutor(Player player, String type) {
		execute(player, type);
	}

	@SuppressWarnings("deprecation")
	private void execute(Player player, String type) {
		// If all of the captchas are disabled, we return
		if (plugin.getCaptchas().isEmpty()) {
			CommonUtils.log(
					"Could not test " + player.getName() + " with a captcha because all of them are disabled");
			return;
		}

		// We return if the player is already under a captcha test
		if (plugin.getToVerify().containsKey(player)) {
			CommonUtils.log("Could not test " + player.getName()
					+ " with a captcha because they're aleardy under a captcha test");
			return;
		}

		// A list of captchas to be used
		List<Captcha> captchas = Arrays.asList(new Picture(), new Gui(), new Different(), new Drop(), new Map());

		// Switching the type given
		switch (type) {
		case "withEnabled":
			// Getting a random captcha from the ones enabled
			plugin.getCaptchas().get(new Random().nextInt(plugin.getCaptchas().size())).execute(player);
			break;
		case "random":
			// Getting a random captcha from the ones in here
			captchas.get(new Random().nextInt(captchas.size())).execute(player);
			break;
		default:
			// Getting a specific captcha from the type given
			boolean found = false;

			for (Captcha cap : captchas) {
				if (cap.getClass().getSimpleName().equalsIgnoreCase(type)) {
					cap.execute(player);
					found = true;
					break;
				}
			}

			// If a captcha type wasn't found, we use a random one instead
			if (!found) {
				execute(player, "random");
			}
			break;
		}

		// If the server has enabled hiding players while on captcha, we hide the player
		if (plugin.getCaptchasFile().getBoolean("whileOnCaptcha.hidePlayer")) {
			Bukkit.getOnlinePlayers().forEach(p -> {
				p.hidePlayer(player);
				player.hidePlayer(p);
			});
		}
		CommonUtils.log(player.getName() + " (" + player.getUniqueId() + ") is now under the captcha test");

		// If after a set amount of time there's no activity, we do some actions
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if (plugin.getToVerify().containsKey(player)) {
				ActionUtil.execute(player, plugin.getCaptchasFile().getStringList("onCaptchaInactivity"));
			}
		}, 20 * plugin.getCaptchasFile().getInt("settings.solveTime"));
	}
}
