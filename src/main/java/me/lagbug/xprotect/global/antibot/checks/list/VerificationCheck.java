package me.lagbug.xprotect.global.antibot.checks.list;

import java.net.InetAddress;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.ProtectionValues;
import me.lagbug.xprotect.global.ProtectionValues.LockState;
import me.lagbug.xprotect.global.antibot.checks.BotCheck;
import me.lagbug.xprotect.global.enums.KickReason;

public class VerificationCheck extends BotCheck {

	public VerificationCheck() {
		super(true);
	}

	public static boolean PERSISTENT = false;
	
	@Override
	protected KickReason runCheck(UUID uuid, String name, InetAddress address) {
		Checker checker = null;
		if (uuid != null) {
			checker = new Checker(uuid);
			
			OfflinePlayer p = Bukkit.getOfflinePlayer(uuid); 
			if (p.isOp()) {
				return KickReason.VERIFIED;
			}
		} else if (name != null) {
			checker = new Checker(name);
		} else if (address != null) {
			checker = new Checker(address);
		} else {
			return KickReason.NOT_BLOCKED;
		}

		if (checker.isVerified() && !PERSISTENT) {
			return KickReason.VERIFIED;
		}

		if (checker.isBlacklisted()) {
			return KickReason.BLACKLISTED;
		}
		
		if (ProtectionValues.LOCK_STATE != LockState.OPEN) {
			return KickReason.LOCKED;
		}
		
		return KickReason.NOT_BLOCKED;
	}

}
