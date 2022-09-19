package me.lagbug.xprotect.spigot.captchas;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

/**
 * This class is used to specify what happens in the color captcha
 *
 * @version 1.0
 */
public class Color extends Captcha {

	// Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@Override
	public void execute(Player player) {
		// A map of colors and their name
		java.util.Map<ChatColor, String> colors = new HashMap<>();

		for (ChatColor c : ChatColor.values()) {
			// If only it's a color
			if (c.isColor()) {
				colors.put(c, c.name().toLowerCase().replace("_", " ").replace("dark ", "").replace("light ", "")
						.replace("gold", "orange").replace("aqua", "blue"));
			}
		}

		// Get a random color
		Object key = colors.keySet().toArray()[new Random().nextInt(colors.keySet().toArray().length)];

		plugin.getToVerify().put(player, colors.get(key));
		Utils.sendInstructions(player, plugin.getMessage("instructions.color").replace("%color%", key.toString()), true);
	}
}