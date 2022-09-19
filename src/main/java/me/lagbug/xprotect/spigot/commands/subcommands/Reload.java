package me.lagbug.xprotect.spigot.commands.subcommands;

import org.bukkit.command.CommandSender;

import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.global.antibot.apis.IpInfo;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.commands.SubCommand;

public class Reload extends SubCommand {

	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	public Reload() {
		super("reload:rl", Permissions.RELOAD);
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		plugin.reloadFiles();
		plugin.initiate(true);
		
		IpInfo.resetCache();
		
		sender.sendMessage(plugin.getMessage("commands.reload.success"));
	}
}