package me.lagbug.xprotect.spigot.guis.managers;

import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.builders.CustomInventory;
import me.lagbug.xprotect.spigot.guis.HomeInventory;
import me.lagbug.xprotect.spigot.utils.PlaceholderManager;
import me.lagbug.xprotect.spigot.utils.captcha.CaptchaExecutor;

public class PlayerManagerInventory extends CustomInventory {

	private static final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	public PlayerManagerInventory() {
		super(plugin.getFile("guis/managers/player_manager.yml"));
	}

	@Override
	public void onClick(Player player, String action, ItemStack item, int slot, ClickType click) {
		OfflinePlayer target = plugin.getEditing().get(player);
		Checker checker = new Checker(target);
		
		switch (action) {
		case "SWITCH_VERIFY":
			if (checker.isVerified()) {
				checker.unVerify();
				player.sendMessage(plugin.getMessage("commands.verify.remove.success").replace("%player%", target.getName()));
			} else {
				player.sendMessage(plugin.getMessage("commands.verify.add.success").replace("%player%", target.getName()));
				checker.forceVerify();
			}
			break;
		case "SWITCH_BLACKLIST":
			if (checker.isBlacklisted()) {
				checker.unBlacklist();
				player.sendMessage(plugin.getMessage("commands.blacklist.remove.success").replace("%player%", target.getName()).replace("%player%", target.getName()));
			} else {
				player.sendMessage(plugin.getMessage("commands.blacklist.add.success").replace("%player%", target.getName()).replace("%player%", target.getName()));
				checker.forceBlacklist();
			}
			break;
		case "TELEPORT_HERE":
			if (target.isOnline()) {
				target.getPlayer().teleport(player);
				sendMessage(player, target, "actions.teleport.here");
			} else {
				player.sendMessage(plugin.getMessage("errors.playerNull"));
			}
			break;
		case "TELEPORT_THERE":
			if (target.isOnline()) {
				player.teleport(target.getPlayer().getLocation());
				sendMessage(player, target, "actions.teleport.there");
			} else {
				sendMessage(player, target, "errors.playerNull");
			}
			break;
		case "SET_ON_FIRE":
			if (target.isOnline()) {
				target.getPlayer().setFireTicks(200);
				sendMessage(player, target, "actions.fire");
			} else {
				sendMessage(player, target, "errors.playerNull");
			}
			break;
		case "SWITCH_OP":
			if (target.isOp()) {
				target.setOp(false);
				sendMessage(player, target, "actions.op.removed");
			} else {
				target.setOp(true);
				sendMessage(player, target, "actions.op.added");
			}
			break;
		case "CLEAR_CHAT":
			if (target.isOnline()) {
				target.getPlayer().sendMessage(StringUtils.repeat(" \n", 100));
				sendMessage(player, target, "actions.clearChat.player");
			} else {
				sendMessage(player, target, "errors.playerNull");
			}
			break;
		case "HEAL_PLAYER":
			if (target.isOnline()) {
				player.setHealth(20.0);
				sendMessage(player, target, "actions.health.heal");
			} else {
				sendMessage(player, target, "errors.playerNull");
			}
			break;
		case "FEED_PLAYER":
			if (target.isOnline()) {
				player.setFoodLevel(20);
				sendMessage(player, target, "actions.health.feed");
			} else {
				sendMessage(player, target, "errors.playerNull");
			}
			break;
		case "CAPTCHA_TEST":
			if (target.isOnline()) {
				target.getPlayer().closeInventory();
				new CaptchaExecutor(target.getPlayer(), "withEnabled");
				sendMessage(player, target, "commands.test.success");
			} else {
				sendMessage(player, target, "errors.playerNull");
			}
			break;
		case "GO_BACK":
			new HomeInventory().openInventory(player);
		}
	}

	@Override
	public void onUpdate(Player player) {
		OfflinePlayer target = plugin.getEditing().get(player);
		
		PlaceholderManager.get(target).forEach(key -> replace(key.getKey(), key.getValue()));
		setPlayer(target);
	}

	@Override
	public void onClose(Player player) {
		plugin.getEditing().remove(player);
	}
	
	private void sendMessage(Player player, OfflinePlayer target, String path) {
		player.sendMessage(plugin.getMessage(path).replace("%player%", target.getName()));
	}
}
