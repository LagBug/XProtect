package me.lagbug.xprotect.spigot.guis;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.builders.CustomInventory;
import me.lagbug.xprotect.spigot.common.utils.communication.Title;
import me.lagbug.xprotect.spigot.guis.managers.ServerManagerInventory;
import me.lagbug.xprotect.spigot.utils.PlaceholderManager;

public class HomeInventory extends CustomInventory {

	private static final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	public HomeInventory() {
		super(plugin.getFile("guis/home.yml"));
	}

	@Override
	public void onClick(Player player, String action, ItemStack item, int slot, ClickType click) {
		switch (action) {
		//If we're meant to go to the server manager GUI
		case "GOTO_SERVER_MANAGER":
			//We destroy the current GUI
			destroy();
			//And then open the new GUI
			new ServerManagerInventory().openInventory(player);
			break;
		//If we're meant to go to the player manager
		case "GOTO_PLAYER_MANAGER":
			//We destroy the current GUI
			destroy();
			//Close the player's inventory
			player.closeInventory();
			//Add the player to the searching HashMap
			plugin.getEditing().put(player, player);
			//Send the searching title to notify the player
			Title.sendForever(player, "&cPlayer Search", "&7Enter a player name to continue");
			break;
		default:
			break;
		}
	}

	@Override
	public void onUpdate(Player player) {
		PlaceholderManager.get().forEach(key -> replace(key.getKey(), key.getValue()));
	}

	@Override
	public void onClose(Player player) {
		//Empty as the GUI will auto-destroy itself.
	}
}
