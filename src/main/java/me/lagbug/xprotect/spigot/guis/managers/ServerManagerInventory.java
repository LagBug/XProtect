package me.lagbug.xprotect.spigot.guis.managers;

import me.lagbug.xprotect.spigot.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.builders.CustomInventory;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;
import me.lagbug.xprotect.spigot.guis.HomeInventory;
import me.lagbug.xprotect.spigot.utils.PlaceholderManager;

public class ServerManagerInventory extends CustomInventory {

	private static final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	public ServerManagerInventory() {
		super(plugin.getFile("guis/managers/server_manager.yml"));
	}

	@Override
	public void onClick(Player player, String action, ItemStack item, int slot, ClickType click) {
		switch (action) {
		case "SWITCH_WHITELIST":
			Bukkit.setWhitelist(!Bukkit.hasWhitelist());
			player.sendMessage(plugin.getMessage("actions.whitelist." + (Bukkit.hasWhitelist() ? "enabled" : "disabled")));
			break;
		case "SWITCH_IDLE_TIMEOUT":
			int idleTimeout = click.isLeftClick() ? Bukkit.getIdleTimeout() + 5 : Bukkit.getIdleTimeout() - 5;
			Bukkit.setIdleTimeout(idleTimeout);
			player.sendMessage(plugin.getMessage("actions.idleTimeout").replace("%idleTimeout%", String.valueOf(idleTimeout)));
			break;
		case "SAVE_PLAYER_DATA":
			Bukkit.savePlayers();
			player.sendMessage(plugin.getMessage("actions.savePlayers"));
			break;
		case "RELOAD_DATA":
			if (Utils.getServerVersion() >= 14) {
				Bukkit.reloadData();
				player.sendMessage(plugin.getMessage("actions.reloadData.success"));
			} else {
				player.sendMessage(plugin.getMessage("actions.reloadData.fail"));
			}
			break;
		case "SWITCH_MAX_SLOTS":
			int maxSlots = click.isLeftClick() ? Bukkit.getMaxPlayers() + 5 : Bukkit.getMaxPlayers() - 5;
			CommonUtils.setMaxPlayers(maxSlots);
			player.sendMessage(plugin.getMessage("actions.maxSlots").replace("%maxSlots%", String.valueOf(maxSlots)));
			break;
		case "CLEAR_CHAT":
			Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(StringUtils.repeat(" \n", 100)));
			player.sendMessage(plugin.getMessage("actions.clearChat.global"));
			break;
		case "GO_BACK":
			new HomeInventory().openInventory(player);
		}
	}

	@Override
	public void onUpdate(Player player) {
		//Updating all the placeholders on every GUI update
		PlaceholderManager.get().forEach(key -> replace(key.getKey(), key.getValue()));
	}

	@Override
	public void onClose(Player player) {
		//Empty as the GUI will auto-destroy itself.
	}

}
