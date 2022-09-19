package me.lagbug.xprotect.spigot.captchas;

import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

/**
 * This class is used to specify what happens in the reverse captcha
 *
 * @version 1.0
 */
public class Reverse extends Captcha {

	//Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@Override
	public void execute(Player player) {
		//Generating a random 6 digit code
		String code = CommonUtils.randomString(6), reversed = "";

		//Reversing the code
		for (int i = code.length() - 1; i >= 0; i--) {
			reversed += code.charAt(i);
		}

		//Sending the instructions
		Utils.sendInstructions(player, plugin.getMessage("instructions.reverse").replace("%code%", code), true);
		plugin.getToVerify().put(player, reversed);
	}
}