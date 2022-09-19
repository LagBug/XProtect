package me.lagbug.xprotect.spigot.captchas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.builders.MapBuilder;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

public class Picture extends Captcha {

	// The instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@Override
	public void execute(Player player) {
		// Some general variables to be used later
		final int itemSlot = plugin.getCaptchasFile().getInt("captchaItem.slot");
		final HashMap<BufferedImage, String> images = getImages();
		final BufferedImage image = images.keySet().toArray(new BufferedImage[images.size()])[new Random()
				.nextInt(images.size())];

		// If the player has another item in the specified slot, we save it
		if (player.getInventory().getItem(itemSlot) != null) {
			plugin.getLastItem().put(player, player.getInventory().getItem(itemSlot));
		}

		// Creation of the ItemStack
		ItemStack item = new MapBuilder().setRenderOnce(true).setImage(image).build();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(
				ChatColor.translateAlternateColorCodes('&', plugin.getCaptchasFile().getString("captchaItem.name")));
		item.setItemMeta(meta);

		// Giving the item to the player
		player.getInventory().setItem(itemSlot, item);

		// Sending instructions to the player
		Utils.sendInstructions(player, plugin.getMessage("instructions.picture").replace("%list%",
				images.values().toString().replace("[", "").replace("]", "").replace(",", "")), true);
		plugin.getToVerify().put(player, images.get(image));
	}

	private HashMap<BufferedImage, String> getImages() {
		// The folder where the images are contained at
		final File folder = new File(plugin.getDataFolder() + File.separator + "pictures");
		// The final Map containing each image, along with it's name
		final HashMap<BufferedImage, String> result = new HashMap<>();

		// Looping through each file in the folder
		for (File file : folder.listFiles()) {
			String fileName = file.getName();

			// If only the file is an image file 
			if (fileName.contains(".jpg") || fileName.contains(".png") || fileName.contains(".jpeg")) {
				fileName = fileName.replace(".jpg", "").replace(".jpeg", "").replace(".png", "");

				try {
					// We put the BufferedImage object and the fileName in the HashMap
					result.put(ImageIO.read(file), fileName);
				} catch (IOException e) {
					// Otherwise, we notify the admins about the error
					CommonUtils.log("Could not add image pictures/" + fileName);
				}
			}
		}

		return result;
	}
}