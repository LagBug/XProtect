package me.lagbug.xprotect.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.lagbug.xprotect.spigot.XProtect;

/**
 * Used to handle the close of the inventory
 * when a player is under the verification process
 *
 * @version 1.0
 */
public class InventoryClose implements Listener {

	//Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (e.getInventory() == null) {
			return;
		}

		Player player = (Player) e.getPlayer();
		
		//If the player is editing or the player is not under the verification process we return
		if (!plugin.getToVerify().containsKey(player) || plugin.getEditing().containsKey(player)) {
			return;
		}

		Inventory inv = e.getInventory();

		//We re-open the inventory
		for (ItemStack item : inv.getContents()) {
			if (item != null && item.getType() != Material.AIR && plugin.getToVerify().get(player).equals(plugin.getCode() + item.getType().name())) {
				Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inv));
				break;
			}
		}
	}
}