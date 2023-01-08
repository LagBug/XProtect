package me.lagbug.xprotect.global.antibot.checks.list;

import me.lagbug.xprotect.global.ProtectionValues;
import me.lagbug.xprotect.global.ProtectionValues.LockState;
import me.lagbug.xprotect.global.antibot.checks.BotCheck;
import me.lagbug.xprotect.global.enums.KickReason;

import java.util.UUID;

public class JoinLimiter extends BotCheck {

	public JoinLimiter() {
		super(true);
	}

	public static int SECONDS = 1;
	public static int PLAYERS = 2;
	
	@Override
	protected KickReason runCheck(UUID uuid, String name, String address) {
		if (ProtectionValues.PLAYERS > PLAYERS) {
			ProtectionValues.LOCK_STATE = LockState.SOFT;
			return KickReason.LOCKED;
		}
		
		return KickReason.NOT_BLOCKED;
	}
}
