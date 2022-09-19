package me.lagbug.xprotect.spigot.events;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.utils.communication.Title;
import me.lagbug.xprotect.spigot.common.utils.util.ActionUtil;
import me.lagbug.xprotect.spigot.common.utils.util.StringUtils;
import me.lagbug.xprotect.spigot.guis.managers.PlayerManagerInventory;
import me.lagbug.xprotect.spigot.utils.Utils;

/**
 * Used to handle player chat while in a variety of conditions.
 *
 * @version 1.0
 */
public class AsyncPlayerChat implements Listener {

	// The instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		String message = e.getMessage();

		if (plugin.getEditing().containsKey(player)) {
			if (message.equalsIgnoreCase("cancel") || message.equalsIgnoreCase("quit")) {
				player.sendMessage(plugin.getMessage("actions.cancel"));
			} else {
				plugin.getEditing().put(player, Bukkit.getOfflinePlayer(message));
				Bukkit.getScheduler().runTask(plugin, () -> new PlayerManagerInventory().openInventory(player));
			}

			Title.cancel(player);
			e.setCancelled(true);
			return;
		}

		// If the player isn't under the verification process we run the swear check and then return
		if (plugin.getChatFile().getBoolean("settings.enabled") && message.length() > 3 && !player.hasPermission(Permissions.BYPASS) && !plugin.getToVerify().containsKey(player)) {
			// Looping through all the regex filters specified in the config
			for (String key : plugin.getChatFile().getConfigurationSection("regexFilters").getKeys(false)) {
				// Looping through all the actual filters
				for (String current : plugin.getChatFile().getStringList("regexFilters." + key + ".expressions")) {
					// If it matches or is a bad word, we act accordingly
					boolean found = false;
					for (String c : plugin.getChatFile().getStringList("whitelist")) {
						if (message.toLowerCase().contains(c)) {
							found = true;
							break;
						}
					}
					
					if (found) {
						return;
					}
					
					if ((current.equalsIgnoreCase("BAD_WORDS_FILE") && StringUtils.containsBadWords(message))
							|| Pattern.compile(current).matcher(message).find()) {
						// Execute the actions specified in the config
						if (ActionUtil.execute(player, plugin.getChatFile().getStringList("regexFilters." + key + ".actions"))) {
							// Cancel the event
							e.setCancelled(true);
						}
						break;
					}
				}
			}
			return;
		}

		// If the player is not under the captcha test we return
		if (!plugin.getToVerify().containsKey(player)) {
			return;
		}

		// Removing the player from the recipients
		if (plugin.getCaptchasFile().getBoolean("whileOnCaptcha.blockChatReceiving")
				&& e.getRecipients().contains(player)) {
			e.getRecipients().remove(player);
		}
		
		// Prevent the player from chating if they're under a captcha which does not require chat
		if (plugin.getToVerify().get(player).equals(plugin.getCode())) {
			e.setCancelled(true);
			return;
		}

		// The code was correct
		if (plugin.getToVerify().get(player).equals(message)) {
			Utils.captchaSuccess(player);
		} else {
			// Otherwise the player has failed the captcha
			Utils.captchaFail(player);	
		}

		e.setCancelled(true);
		
	}
}