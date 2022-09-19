package me.lagbug.xprotect.spigot.captchas;

import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

/**
 * This class is used to specify what happens in the equation captcha
 *
 * @version 1.0
 */
public class Equation extends Captcha {

	// Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@Override
	public void execute(Player player) {
		// Getting the values range from the config
		int min = plugin.getCaptchasFile().getInt("captchas.equation.range.min");
		int max = plugin.getCaptchasFile().getInt("captchas.equation.range.max");

		String text = "";
		int firstNumber = CommonUtils.randomInteger(min, max), secondNumber = CommonUtils.randomInteger(min, max),
				sum = 0;

		// If the first number is more than the second, we subtract
		if (secondNumber < firstNumber) {
			sum = firstNumber - secondNumber;
			text = firstNumber + " - " + secondNumber;
		} else {
			// Otherwise we sum up
			sum = firstNumber + secondNumber;
			text = firstNumber + " + " + secondNumber;
		}

		// We send the instructions
		Utils.sendInstructions(player, plugin.getMessage("instructions.equation").replace("%equation%", text), true);
		plugin.getToVerify().put(player, String.valueOf(sum));
	}
}