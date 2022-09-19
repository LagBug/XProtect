package me.lagbug.xprotect.spigot.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.commands.SubCommand;

public class Blacklist extends SubCommand {

	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	public Blacklist() {
		super("blacklist:block:drop", Permissions.BLACKLIST);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (args.length <= 1) {
			sender.sendMessage(plugin.getMessage("errors.wrongUsage").replace("%usage%", "/protect blacklist <player>"));
			return;
		}
		
		OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
		Checker checker = new Checker(p);
		
		if (checker.isBlacklisted()) {
			sender.sendMessage(plugin.getMessage("commands.blacklist.add.already").replace("%player%", p.getName()));
			return;
		}

		checker.forceBlacklist();
		sender.sendMessage(plugin.getMessage("commands.blacklist.add.success").replace("%player%", p.getName()));		
	}
}