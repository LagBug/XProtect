package me.lagbug.xprotect.global.antibot.checks.list;

import java.net.InetAddress;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.lagbug.xprotect.global.antibot.checks.BotCheck;
import me.lagbug.xprotect.global.enums.KickReason;

public class IpLimiter extends BotCheck {

	public IpLimiter() {
		super(true);
	}

	public static int AMOUNT = 0;
	
	@Override
	protected KickReason runCheck(UUID uuid, String name, InetAddress address) {
		if (Bukkit.getOnlinePlayers().stream()
				.filter(player -> player.getAddress().getAddress().getHostAddress().equalsIgnoreCase(address.getHostAddress())
						&& !player.getUniqueId().toString().equals(uuid.toString()))
				.count() >= AMOUNT) {
			return KickReason.IP;
		}
		
		return KickReason.NOT_BLOCKED;
	}
}
