package me.lagbug.xprotect.global.antibot.checks.list;

import me.lagbug.xprotect.global.antibot.checks.BotCheck;
import me.lagbug.xprotect.global.enums.KickReason;
import org.bukkit.Bukkit;

import java.util.UUID;

public class IpLimiter extends BotCheck {

	public IpLimiter() {
		super(true);
	}

	public static int AMOUNT = 0;
	
	@Override
	protected KickReason runCheck(UUID uuid, String name, String address) {
		if (Bukkit.getOnlinePlayers().stream()
				.filter(player -> player.getAddress().getAddress().getHostAddress().equalsIgnoreCase(address)
						&& !player.getUniqueId().toString().equals(uuid.toString()))
				.count() >= AMOUNT) {
			return KickReason.IP;
		}
		
		return KickReason.NOT_BLOCKED;
	}
}
