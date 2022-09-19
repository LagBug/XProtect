package me.lagbug.xprotect.spigot.captchas;

import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

/**
 * This class is used to specify what happens in the jump captcha
 *
 * @version 1.0
 */
public class Jump extends Captcha {

	//Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	@Override
	public void execute(Player player) {
		//We send the instructions
		Utils.sendInstructions(player, plugin.getMessage("instructions.jump"), true);
		plugin.getToVerify().put(player, plugin.getCode() + "jump");
	}
}