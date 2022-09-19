package me.lagbug.xprotect.bungee.events;

import me.lagbug.xprotect.bungee.XProtectBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Chat implements Listener {

	private final XProtectBungee plugin = XProtectBungee.getInstance(); 
	
	@EventHandler
	public void onChat(ChatEvent e) {
		if (!(e.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		
		if (plugin.getConfigFile().getBoolean("settings.disableCaptchas")) {
			return;
		}

		//ProxiedPlayer player = (ProxiedPlayer) e.getSender();
		boolean r = false;

		if (!plugin.getConfigFile().getStringList("whitelistedCommands").isEmpty()) {
			for (String cmd : plugin.getConfigFile().getStringList("whitelistedCommands")) {
				if (e.getMessage().startsWith(cmd)) {
					r = true;
					break;
				}
			}
		}

		if (r || !e.isCommand()/* || new Checker(player.getUniqueId()).isVerified() */) {
			return;
		}

		e.setCancelled(true);
	}

}
