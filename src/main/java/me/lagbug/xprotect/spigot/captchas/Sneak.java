package me.lagbug.xprotect.spigot.captchas;

import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

/**
 * This class is used to specify what happens in the sneak captcha
 *
 * @version 1.0
 */
public class Sneak extends Captcha {

	// Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@Override
	public void execute(Player player) {
		// Sending the instructions
		Utils.sendInstructions(player, plugin.getMessage("instructions.sneak"), true);
		plugin.getToVerify().put(player, plugin.getCode() + "sneak");
	}
}