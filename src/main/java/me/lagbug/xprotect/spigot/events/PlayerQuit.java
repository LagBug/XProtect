package me.lagbug.xprotect.spigot.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.Utils;

/**
 * This class is used to handle player leaving
 *
 * @version 1.0
 */
public class PlayerQuit implements Listener {

	//Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
		
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		//If the player is not under the verification process we return
		if (!plugin.getToVerify().containsKey(player)) {
			return;
		}
		
		//We disable the quit message
		e.setQuitMessage(null);
		//Remove the player from the toVerify list
		plugin.getToVerify().remove(player);
		//Remove any items given to the player for the verification process
		Utils.removeMap(player);
		
		//If the player had any last item, we give it back
		if (plugin.getLastItem().containsKey(player)) {
			player.getInventory().addItem(plugin.getLastItem().get(player));
			plugin.getLastItem().remove(player);
		}
    }
}