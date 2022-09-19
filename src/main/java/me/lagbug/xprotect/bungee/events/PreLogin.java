package me.lagbug.xprotect.bungee.events;

import me.lagbug.xprotect.global.antibot.checks.BotBlocker;
import me.lagbug.xprotect.global.enums.KickReason;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PreLogin implements Listener {

	@EventHandler
	public void onPreLogin(PreLoginEvent e) {
		PendingConnection connection = e.getConnection();
		KickReason reason = BotBlocker.getResult(connection.getUniqueId(), connection.getName(), connection.getAddress().getAddress());

		if (reason == KickReason.NOT_BLOCKED) {
			return;
		}

		e.setCancelReason(new TextComponent(reason.getKickMessage()));
		e.setCancelled(true);
	}

}
