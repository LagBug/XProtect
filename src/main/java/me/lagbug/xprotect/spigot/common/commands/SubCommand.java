package me.lagbug.xprotect.spigot.common.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
	
	private final String[] names;
	private final String permission;
	public CommandSender sender;

	public SubCommand(String names, String permission) {
		this.names = names.split(":");
		this.permission = permission;
	}

	public final List<String> getNames() {
		return Arrays.asList(this.names);
	}

	public final String getPermission() {
		return this.permission;
	}

	public abstract void onCommand(CommandSender sender, String[] args);
}
