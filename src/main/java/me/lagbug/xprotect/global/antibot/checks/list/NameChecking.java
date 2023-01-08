package me.lagbug.xprotect.global.antibot.checks.list;

import me.lagbug.xprotect.global.antibot.checks.BotCheck;
import me.lagbug.xprotect.global.enums.KickReason;

import java.util.UUID;

public class NameChecking extends BotCheck {

	public NameChecking() {
		super(true);
	}

	public static String[] NAMES = null;
	
	@Override
	protected KickReason runCheck(UUID uuid, String name, String address) {
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
