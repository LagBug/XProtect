package me.lagbug.xprotect.spigot.captchas;

import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

/**
 * This class is used to specify what happens in the code captcha
 *
 * @version 1.0
 */
public class Code extends Captcha {

	// Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@Override
	public void execute(Player player) {
		// Generating a random 6 digit code
		String code = CommonUtils.randomString(6);

		// Sending instructions on what to do
		Utils.sendInstructions(player, plugin.getMessage("instructions.code").replace("%code%", code), true);
		// Adding the player to the verification process
		plugin.getToVerify().put(player, code);
	}
}