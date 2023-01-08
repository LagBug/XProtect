package me.lagbug.xprotect.global.antibot.checks.list;

import me.lagbug.xprotect.global.antibot.apis.IpInfo;
import me.lagbug.xprotect.global.antibot.checks.BotCheck;
import me.lagbug.xprotect.global.enums.KickReason;
import org.json.simple.JSONObject;

import java.net.SocketAddress;
import java.util.UUID;

public class AntiBadConnection extends BotCheck {

	public AntiBadConnection() {
		super(true);
	}

	public static boolean BLOCK_BOTS = true;
	public static boolean BLOCK_VPNS = true;
	public static boolean BLOCK_ABUSE = true;
	public static int FRAUD_SCORE = 85;

	@Override
	protected KickReason runCheck(UUID uuid, String name, String address) {
		if (address != null) {
			JSONObject ipInfo = IpInfo.getInformation(address);
			
			try {
				if (BLOCK_VPNS && ipInfo.get("vpn").equals(true)) {
					return KickReason.VPN;
				} else if (BLOCK_BOTS && ipInfo.get("bot").equals(true)) {
					return KickReason.BOT;
				} else if (BLOCK_ABUSE && ipInfo.get("abusive").equals(true)) {
					return KickReason.ABUSE;
				} else if ((int) ipInfo.get("fraudScore") >= FRAUD_SCORE) {
					return KickReason.RISK;
				}
			} catch (NullPointerException | ClassCastException ex) {
				return KickReason.NOT_BLOCKED;
			}
		}
		
		return KickReason.NOT_BLOCKED;
	}
}