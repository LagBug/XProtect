package me.lagbug.xprotect.spigot.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.commands.subcommands.Add;
import me.lagbug.xprotect.spigot.commands.subcommands.Blacklist;
import me.lagbug.xprotect.spigot.commands.subcommands.Check;
import me.lagbug.xprotect.spigot.commands.subcommands.Debug;
import me.lagbug.xprotect.spigot.commands.subcommands.List;
import me.lagbug.xprotect.spigot.commands.subcommands.Reload;
import me.lagbug.xprotect.spigot.commands.subcommands.Remove;
import me.lagbug.xprotect.spigot.commands.subcommands.Test;
import me.lagbug.xprotect.spigot.common.commands.SpigotCommand;
import me.lagbug.xprotect.spigot.guis.HomeInventory;

public class ProtectCommand extends SpigotCommand {
	
	//The constructor, contains all the sub-commands
	public ProtectCommand() {
		super(Permissions.USE, 1,  new Add(), new Blacklist(), new Remove(), new Check(), new Test(), new List(), new Debug(), new Reload());
		super.setUsage("protect");
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (args.length <= 0 && sender instanceof Player) {
			new HomeInventory().openInventory((Player) sender);	
		}
	}
}
