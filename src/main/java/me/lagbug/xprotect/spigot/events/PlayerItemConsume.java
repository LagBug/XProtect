package me.lagbug.xprotect.spigot.events;

import me.lagbug.xprotect.spigot.XProtect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

/**
 * This class is used to block eating if the player is under the verification process
 *
 * @version 1.0
 */
public class PlayerItemConsume implements Listener {

	//Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
		Player player = e.getPlayer();
		
		//If the player is in the verification process we cancel the event
		if (plugin.getToVerify().containsKey(player)) {
			e.setCancelled(true);
		}
    }
}