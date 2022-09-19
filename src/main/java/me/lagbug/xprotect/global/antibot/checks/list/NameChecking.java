package me.lagbug.xprotect.global.antibot.checks.list;

import java.net.InetAddress;
import java.util.UUID;

import me.lagbug.xprotect.global.antibot.checks.BotCheck;
import me.lagbug.xprotect.global.enums.KickReason;

public class NameChecking extends BotCheck {

	public NameChecking() {
		super(true);
	}

	public static String[] NAMES = null;
	
	@Override
	protected KickReason runCheck(UUID uuid, String name, InetAddress address) {
		if (name != null) {
			for (String current : NAMES) {
				if (name.toLowerCase().contains(current.toLowerCase())) {
					return KickReason.NAME;
				}
			}
		}
		
		return KickReason.NOT_BLOCKED;
	}		
}
