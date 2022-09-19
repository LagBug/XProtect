package me.lagbug.xprotect.spigot.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.commands.SubCommand;

public class Check extends SubCommand {

	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	public Check() {
		super("check:c", Permissions.CHECK);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (args.length <= 1) {
			sender.sendMessage(plugin.getMessage("errors.wrongUsage").replace("%usage%", "/protect check <player>"));
			return;
		}

		OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
		Checker checker = new Checker(p);

		if (checker.isVerified()) {
			sender.sendMessage(plugin.getMessage("commands.verify.check.found").replace("%player%", p.getName()));
			return;
		}

		sender.sendMessage(plugin.getMessage("commands.verify.check.notFound").replace("%player%", p.getName()));
	}
}