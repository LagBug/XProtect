package me.lagbug.xprotect.spigot.captchas;

import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

/**
 * This class is used to specify what happens in the slot captcha
 *
 * @version 1.0
 */
public class Slot extends Captcha {

	//Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	@Override
	public void execute(Player player) {
		//Generating a random slot
		String slot = String.valueOf(CommonUtils.randomInteger(1, 8));
		
		//Sending the instructions
		plugin.getToVerify().put(player, plugin.getCode() + slot);
		Utils.sendInstructions(player, plugin.getMessage("instructions.slot").replace("%slot%", slot), true);
	}
}