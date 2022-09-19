package me.lagbug.xprotect.spigot.common.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.guis.HomeInventory;
import me.lagbug.xprotect.spigot.guis.managers.PlayerManagerInventory;

public abstract class SpigotCommand implements CommandExecutor {

	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	private final String permission;
	private String noPermissions;
	private String usage;
	private final int reqArgs;
	private final List<SubCommand> subCommands = new ArrayList<>();

	public SpigotCommand(String permission, int reqArgs, SubCommand... subCommands) {
		this.permission = permission;
		this.reqArgs = reqArgs - 1;

		for (SubCommand sc : subCommands) {
			this.subCommands.add(sc);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (!sender.hasPermission(permission)) {
			sender.sendMessage(plugin.getMessage("errors.noPermissions"));
			return false;
		}

		if (reqArgs <= -1) {
			onCommand(sender, args);
			return false;
		}

		if (args.length <= reqArgs) {
			if (sender instanceof Player) {
				new HomeInventory().openInventory((Player) sender);
				return false;
			}
			
			try {
				sender.sendMessage(plugin.getMessage("errors.wrongUsage").replace("%usage%", "/" + usage + " <" + getSubCommandNames() + ">"));
			} catch (NullPointerException ex) {
				sender.sendMessage(plugin.getMessage("errors.wrongUsage").replace("%usage%", usage));
			}
			return false;
		}

		boolean found = false;

		try {
			for (SubCommand subCommand : this.subCommands) {
				if (subCommand.getNames().contains(args[0].toLowerCase())) {
					found = true;

					if (!sender.hasPermission(subCommand.getPermission()) || !sender.hasPermission(permission)) {
						sender.sendMessage(noPermissions);
						return false;
					}

					subCommand.sender = sender;
					subCommand.onCommand(sender, args);
					return false;
				}
			}

			if (!found) {
				if (sender instanceof Player) {
					Player target = Bukkit.getPlayer(args[0]);
					Player player = (Player) sender;
					if (target != null) {
						plugin.getEditing().put(player, target);
						new PlayerManagerInventory().openInventory(player);
					} else {
						new HomeInventory().openInventory(player);	
					}
					
				} else {
					sender.sendMessage(plugin.getMessage("errors.wrongUsage").replace("%usage%",
							"/" + usage + " <" + getSubCommandNames() + ">"));
				}
			}
		} catch (NullPointerException ex) {
			onCommand(sender, args);

		}

		return false;
	}

	public abstract void onCommand(CommandSender sender, String[] args);

	protected String getSubCommandNames() {
		String result = "";
		for (SubCommand cmd : subCommands) {
			result += "/" + cmd.getNames().get(0);
		}

		return result.substring(1, result.length());
	}

	protected void setUsage(String usage) {
		this.usage = usage;
	}
}