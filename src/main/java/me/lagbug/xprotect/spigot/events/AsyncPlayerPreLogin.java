package me.lagbug.xprotect.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import me.lagbug.xprotect.global.antibot.checks.BotBlocker;
import me.lagbug.xprotect.global.enums.KickReason;
import me.lagbug.xprotect.spigot.utils.DataValues;

public class AsyncPlayerPreLogin implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
		// We get the KickReason from the BotBlocker class
		KickReason reason = BotBlocker.getResult(e.getUniqueId(), Bukkit.getOfflinePlayer(e.getUniqueId()).getName(),
				e.getAddress().getHostAddress());
		// If the player is not blocked, we simply return here
		if (reason == KickReason.NOT_BLOCKED) {
			return;
		}

		// Otherwise we disallow join access to the player
		e.disallow(Result.KICK_OTHER, reason.getKickMessage());
		// And add 1 to the total blocked bots
		DataValues.BLOCKED_BOTS++;
	}
}