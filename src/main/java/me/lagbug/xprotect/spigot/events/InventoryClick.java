package me.lagbug.xprotect.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.Utils;

public class InventoryClick implements Listener {

	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null || e.getClickedInventory() == null || e.getInventory() == null || e.getCurrentItem().getType().equals(Material.AIR) || !(e.getWhoClicked() instanceof Player)) {
			return;
		}

		Player player = (Player) e.getWhoClicked();
		
		if (!plugin.getToVerify().containsKey(player)) {
			return;
		}

		if (plugin.getToVerify().get(player).equals(plugin.getCode() + e.getCurrentItem().getType().name())) {
			e.setCancelled(true);
			Bukkit.getScheduler().runTaskLater(plugin, player::closeInventory, 2);
			Utils.captchaSuccess(player);
			return;
		}

		e.setCancelled(true);
		player.closeInventory();
		Utils.captchaFail(player);
	}
}