package me.lagbug.xprotect.spigot.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.Utils;

/**
 * This class is used to verify the player in the drop captcha 
 *
 * @version 1.0
 */
public class PlayerDropItem implements Listener {

	//Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
		
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		
		//If the player is not in the verificatio process
		if (!plugin.getToVerify().containsKey(player)) {
			return;
		}
		
		//If the random generated code from the plugin matches the key we verify the player
		if (plugin.getToVerify().get(player).equals(plugin.getCode() + "drop")) {
			Utils.captchaSuccess(player);
			e.getItemDrop().remove();
		}
    }
}