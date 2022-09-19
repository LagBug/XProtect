package me.lagbug.xprotect.spigot.utils;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.lagbug.xprotect.api.events.PlayerCompleteCaptchaEvent;
import me.lagbug.xprotect.api.events.PlayerFailCaptchaEvent;
import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.utils.communication.Actionbar;
import me.lagbug.xprotect.spigot.common.utils.communication.Title;
import me.lagbug.xprotect.spigot.common.utils.util.ActionUtil;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;

public class Utils {

	// The instance of the main class
	private static final XProtect plugin = XProtect.getPlugin(XProtect.class);

	public static void sendInstructions(final Player player, final String path, final boolean forever) {
		// How should the message be sent through
		String through = plugin.getCaptchasFile().getString("settings.sendThrough").toUpperCase();
		
		// Switch through the possible types
		switch (through) {
		case "ACTIONBAR":
			if (forever) {
				Actionbar.sendForever(player, path);
			} else {
				Actionbar.cancel(player);
				Actionbar.send(player, path);
			}
			break;
		case "TITLES":
			if (forever) {
				Title.sendForever(player, "", path);
			} else {
				Title.cancel(player);
				Title.send(player, 40, "", path);
			}
			break;
		default:
			// Otherwise we send them through the chat
			player.sendMessage(path);
			break;
		}
	}

	public static int getServerVersion() {
		return Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);
	}

	public static void removeMap(final Player player) {
		// Looping through all items in the player's inventory
		for (ItemStack item : player.getInventory().getContents()) {
			// If it's not a valid item, we go with the next one
			if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()
					|| !item.getItemMeta().hasDisplayName()) {
				continue;
			}

			// If it's display name is not equal to the one specified, we go with the next one
			if (!item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&',
					plugin.getCaptchasFile().getString("captchaItem.name")))) {
				continue;
			}

			// Otherwise we remove the item
			player.getInventory().remove(item);
		}
	}

	@SuppressWarnings("deprecation")
	public static void captchaSuccess(Player player) {
		Bukkit.getScheduler().runTask(plugin, () -> {
			PlayerCompleteCaptchaEvent pcce = new PlayerCompleteCaptchaEvent(player);

			if (pcce.isCancelled()) {
				return;
			}

			DataValues.SUCCESSFUL_CAPTCHAS++;
			plugin.getToVerify().remove(player);
			new Checker(player).forceVerify();

			sendInstructions(player, plugin.getMessage("general.success"), false);
			removeMap(player);

			if (plugin.getLastItem().containsKey(player)) {
				player.getInventory().addItem(plugin.getLastItem().get(player));
				plugin.getLastItem().remove(player);
			}

			ActionUtil.execute(player, plugin.getCaptchasFile().getStringList("onCaptchaSuccess"));
			CommonUtils.log(player.getName() + " (" + player.getUniqueId() + ") has successfully completed the captcha");
			if (plugin.getCaptchasFile().getBoolean("whileOnCaptcha.hidePlayer")) {
				Bukkit.getOnlinePlayers().forEach(p -> {
					p.showPlayer(player);
					player.showPlayer(p);
				});
			}

			Map<UUID, Integer> failedCaptchas = plugin.getFailedCaptchas();
			if (failedCaptchas.containsKey(player.getUniqueId())) {
				failedCaptchas.remove(player.getUniqueId());
			} 
			
			Bukkit.getOnlinePlayers().forEach(pl -> {
				if (pl.hasPermission(Permissions.NOTIFY) && plugin.getCaptchasFile().getBoolean("notifyOn.complete")) {
					pl.sendMessage(plugin.getMessage("general.notifications.complete").replace("%player%", player.getName()));
				}
			});
		});
	}

	public static void captchaFail(Player player) {
		Bukkit.getScheduler().runTask(plugin, () -> {
			PlayerFailCaptchaEvent gee = new PlayerFailCaptchaEvent(player);
			Bukkit.getPluginManager().callEvent(gee);

			if (gee.isCancelled()) {
				return;
			}

			DataValues.FAILED_CAPTCHAS++;
			ActionUtil.execute(player, plugin.getCaptchasFile().getStringList("onCaptchaFail"));
			CommonUtils.log(player.getName() + " (" + player.getUniqueId() + ") has failed to complete the captcha");

			Map<UUID, Integer> failedCaptchas = plugin.getFailedCaptchas();
			if (failedCaptchas.containsKey(player.getUniqueId())) {
				failedCaptchas.put(player.getUniqueId(), failedCaptchas.get(player.getUniqueId()) + 1);
			} else {
				failedCaptchas.put(player.getUniqueId(), 1);
			}
			
			if (failedCaptchas.get(player.getUniqueId()) >= plugin.getCaptchasFile().getInt("settings.maxCaptchaFail")) {
				ActionUtil.execute(player, plugin.getCaptchasFile().getStringList("onMultipleCaptchaFail"));
				failedCaptchas.remove(player.getUniqueId());
			}
			
			Bukkit.getOnlinePlayers().forEach(pl -> {
				if (pl.hasPermission(Permissions.NOTIFY) && plugin.getConfigFile().getBoolean("notifyOn.fail")) {
					pl.sendMessage(plugin.getMessage("general.notifications.fail").replace("%player%", player.getName()));
				}
			});
		});
	}

}