package me.lagbug.xprotect.spigot.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.commands.SubCommand;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;

public class Debug extends SubCommand {
	
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
		
	public Debug() {
		super("debug:d", Permissions.DEBUG);
	}
	
	@Override
	public void onCommand(CommandSender sender, String[] args) {
		Player target = null;
		
		if (args.length > 1) {
			target = Bukkit.getPlayer(args[1]);
		}
		
		if (target == null && sender instanceof Player) {
			target = (Player) sender;
		}
		
		List<String> pluginNames = new ArrayList<>(), permissions = new ArrayList<>();
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			pluginNames.add(plugin.getDescription().getName());
		}

		send(target, 
				"&7&m--------------------------------------------------",
				" &6XProtect &fdebug report",
				"&7&m--------------------------------------------------",
				" &6Plugin details",
				"  &fVersion: &6" + plugin.getDescription().getVersion() + " &7(" + (plugin.getUser().isEmpty() || plugin.getUser().equals("666")) + ")",
				"  &fLicense: &6" + plugin.getUser(),
				"  &fAuthMe/ProtocolLib: &6" + plugin.authme + "&7/&6" +plugin.protocol,
				"  &fStorage: &6" + plugin.getConfigFile().getString("storage.type"),
				"  &fBungeecord: &6" + CommonUtils.isBungee(),
				"  &fCaptcha types: &6" + plugin.getCaptchas().stream().map(c -> c.getClass().getSimpleName().toLowerCase()).collect(Collectors.toList()),
				"&7&m--------------------------------------------------",
				" &6Server details",
				"  &fSpigot version: &6" + Bukkit.getVersion(),
				"  &fBukkit version: &6" + Bukkit.getBukkitVersion(),
				"  &fPlugins: &6" + pluginNames + " [" + plugin.getUser() + "]",
				"  &fPlayers: &6" + Bukkit.getServer().getOnlinePlayers().size() + "&7/&6" + Bukkit.getMaxPlayers(),
				"&7&m--------------------------------------------------");
		
		if (target != null) {
			for (String perm : Permissions.ALL_PERMISSIONS) {
				if (target.hasPermission(perm)) {
					permissions.add(perm);
				}
			}
		
			Checker checker = new Checker(target);
			
			send(target, " &6Player details",
					"  &fName (UUID): &6" + target.getName() + "&7(&6" + target.getUniqueId() + "&7)",
					"  &fWorld: &6" + target.getWorld().getName(),
					"  &fOP: &6" + target.isOp(),
					"  &fDead: &6" + target.isDead(),
					"  &fVerified/Blacklisted: &6 " + checker.isVerified() + "&7/&6" + checker.isBlacklisted(),
					"  &fPermissions: &6" + permissions,
					"&7&m--------------------------------------------------");
		}
	}

	private void send(Player sender, String... messages) {
		for (String msg : messages) {
			if (sender != null) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));	
			}
			
			System.out.println(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', msg)));
		}
	}
}