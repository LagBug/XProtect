package me.lagbug.xprotect.global.antibot.checks;

import me.lagbug.xprotect.global.ProtectionValues;
import me.lagbug.xprotect.global.antibot.checks.list.*;
import me.lagbug.xprotect.global.enums.KickReason;
import me.lagbug.xprotect.spigot.utils.DataValues;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This class is used in order to block bots
 *
 * @version 1.1
 */
public class BotBlocker {

	// All the checks done by the plugin
	private static final List<BotCheck> botChecks = Arrays.asList(new VerificationCheck(), new JoinLimiter(), new NameChecking(),
			new AntiBadConnection(), new CountryChecking(), new IpLimiter());

	/*
	 * Returns the result of each check. Used to block
	 * bots efficiently.
	 * 
	 * @return - the reason the player should be kicked
	 */
	public static KickReason getResult(UUID uuid, String name, String address) {
		DataValues.CHECKED_CONNECTIONS++;
		ProtectionValues.TOTAL_PLAYERS++;
		ProtectionValues.PLAYERS++;
		ProtectionValues.LAST_PLAYERS++;

		// Looping through every check
		for (BotCheck check : botChecks) {
			// If only the check is enabled
			if (check.isEnabled()) {
				KickReason kickReason = check.runCheck(uuid, name, address);
				// If the player is verified we return here
				if (kickReason == KickReason.VERIFIED) {
					return KickReason.NOT_BLOCKED;
				}
				
				// If a test is failed we return here
				if (kickReason != KickReason.NOT_BLOCKED) {
					return kickReason;
				}
			}
		}

		return KickReason.NOT_BLOCKED;
	}
}
