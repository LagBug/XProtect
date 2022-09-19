package me.lagbug.xprotect.spigot.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.commands.SubCommand;

public class Add extends SubCommand {

	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	public Add() {
		super("add:verify:a", Permissions.ADD);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (args.length <= 1) {
			sender.sendMessage(plugin.getMessage("errors.wrongUsage").replace("%usage%", "/protect add <player>"));
			return;
		}
		
		OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
		
		// PlayerDataCache.save(true); COULD BE THE SOLUTION
		Checker checker = new Checker(p);
			
		if (checker.isVerified()) {
			sender.sendMessage(plugin.getMessage("commands.verify.add.already").replace("%player%", p.getName()));
			return;
		}

		checker.forceVerify();
		sender.sendMessage(plugin.getMessage("commands.verify.add.success").replace("%player%", p.getName()));		
	}
}