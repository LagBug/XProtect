package me.lagbug.xprotect.global.antibot.checks;

import java.net.InetAddress;
import java.util.UUID;

import me.lagbug.xprotect.global.enums.KickReason;

public abstract class BotCheck {
	
	private static boolean isEnabled = true;
	
	public BotCheck(boolean isEnabled) {
		BotCheck.isEnabled = isEnabled;
	}
	
	protected abstract KickReason runCheck(UUID uuid, String name, InetAddress address);

	protected boolean isEnabled() {
		return isEnabled;
	}
	
	public static void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
}
