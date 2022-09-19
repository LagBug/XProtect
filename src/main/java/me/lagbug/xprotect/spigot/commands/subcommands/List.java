package me.lagbug.xprotect.spigot.commands.subcommands;

import org.bukkit.command.CommandSender;

import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.commands.SubCommand;

public class List extends SubCommand {
	
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
		
	public List() {
		super("list:l", Permissions.LIST);
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		sender.sendMessage(plugin.getMessage("commands.list.success"));

		for (String key : plugin.getCaptchasFile().getConfigurationSection("captchas").getKeys(false)) {
			boolean enabled = plugin.getCaptchasFile().getBoolean("captchas." + key + ".enabled");
			String name = key.substring(0, 1).toUpperCase() + key.substring(1);
			
			sender.sendMessage(plugin.getMessage("commands.list.format").replace("%type%", name).replace("%status%", enabled ? "On" : "Off"));
		}
	}
}