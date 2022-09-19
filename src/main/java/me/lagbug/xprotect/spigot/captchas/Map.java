package me.lagbug.xprotect.spigot.captchas;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.builders.MapBuilder;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

public class Map extends Captcha {

	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	@Override
	public void execute(Player player) {
		String code = CommonUtils.randomString(6);

		if (player.getInventory().getItem(plugin.getCaptchasFile().getInt("captchaItem.slot")) != null) {
			plugin.getLastItem().put(player, player.getInventory().getItem(plugin.getCaptchasFile().getInt("captchaItem.slot")));
		}
		
		ItemStack item = new MapBuilder().setRenderOnce(true).setImage(generateCaptcha(code)).build();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getCaptchasFile().getString("captchaItem.name")));
		item.setItemMeta(meta);
		
		player.getInventory().setItem(plugin.getCaptchasFile().getInt("captchaItem.slot"), item);

		Utils.sendInstructions(player, plugin.getMessage("instructions.map"), true);
		plugin.getToVerify().put(player, code);
	}

	private BufferedImage generateCaptcha(String code) {
		BufferedImage image = new BufferedImage(130, 130, BufferedImage.TYPE_INT_BGR);
		Random random = new Random();
		Graphics g = image.getGraphics();

		g.fillRect(0, 0, 130, 130);
		g.setColor(CommonUtils.colorFromString(plugin.getCaptchasFile().getString("captchas.map.textColor")));

		int margin = 130 / code.length();
		for (int i = 0; i < code.length(); ++i) {
			g.setFont(new Font("Fixedsys", Font.CENTER_BASELINE, 17));
			g.translate(random.nextInt(3), random.nextInt(3));
			g.drawString(String.valueOf(code.charAt(i)), margin * i, CommonUtils.randomInteger(40, 90));
		}

		g.setColor(CommonUtils.colorFromString(plugin.getCaptchasFile().getString("captchas.map.linesColor")));
		for (int i = 0; i <= plugin.getCaptchasFile().getInt("captchas.map.lines"); i++) {
			int x = random.nextInt(130), y = random.nextInt(130), xl = random.nextInt(13), yl = random.nextInt(15);
			g.drawLine(x, y, x + xl, y + yl);
		}

		g.dispose();
		return image;
	}

}