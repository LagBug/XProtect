package me.lagbug.xprotect.spigot.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lagbug.xprotect.global.Permissions;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.commands.SubCommand;
import me.lagbug.xprotect.spigot.utils.captcha.CaptchaExecutor;

public class Test extends SubCommand {
	
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
		
	public Test() {
		super("test:t:prompt", Permissions.TEST);
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (args.length <= 1) {
			sender.sendMessage(plugin.getMessage("errors.wrongUsage").replace("%usage%", "/protect test <player> [type]"));
			return;
		}
		Player p = Bukkit.getPlayer(args[1]);
		String type = "withEnabled";
		
		if (p == null) {
			sender.sendMessage(plugin.getMessage("errors.playerNull"));
			return;
		}

		if (args.length == 3) {
			type = args[2];
		}
		
		sender.sendMessage(plugin.getMessage("commands.test.success").replace("%player%", p.getName()));
		new CaptchaExecutor(p, type);
	}
}