package me.lagbug.xprotect.spigot.captchas;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.builders.ItemBuilder;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

/**
 * This class is used to specify what happens in the drop captcha
 *
 * @version 1.0
 */
public class Drop extends Captcha {

	// Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	@Override
	public void execute(Player player) {
		// Generation a new item to use
		ItemStack item = new ItemBuilder(Material.valueOf(plugin.getCaptchasFile().getString("captchas.drop.item")), 1,
				(byte) 0).setDisplayName(plugin.getCaptchasFile().getString("captchaItem.name")).build();

		//If the player already has an item in the specific slot, we deal accordingly
		if (player.getInventory().getItem(plugin.getCaptchasFile().getInt("captchaItem.slot")) != null) {
			plugin.getLastItem().put(player, player.getInventory().getItem(plugin.getCaptchasFile().getInt("captchaItem.slot")));
		}

		//We set the item in the player's inventory
		player.getInventory().setItem(plugin.getCaptchasFile().getInt("captchaItem.slot"), item);

		//And lastly we send instructions
		Utils.sendInstructions(player, plugin.getMessage("instructions.drop"), true);
		plugin.getToVerify().put(player, plugin.getCode() + "drop");
	}
}