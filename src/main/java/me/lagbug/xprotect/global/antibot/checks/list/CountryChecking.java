package me.lagbug.xprotect.global.antibot.checks.list;

import me.lagbug.xprotect.global.antibot.apis.IpInfo;
import me.lagbug.xprotect.global.antibot.checks.BotCheck;
import me.lagbug.xprotect.global.enums.KickReason;
import org.json.simple.JSONObject;

import java.util.UUID;

public class CountryChecking extends BotCheck {

	public CountryChecking() {
		super(true);
	}

	public static String MODE = "WHITELIST";
	public static String[] LIST = null;

	@Override
	protected KickReason runCheck(UUID uuid, String name, String address) {
		JSONObject ipInfo = IpInfo.getInformation(address);
		String country = (String) ipInfo.get("countryCode");

		if (MODE.equals("WHITELIST") && !contains(country)) {
			return KickReason.COUNTRY;
		}
		
		if (MODE.equals("BLACKLIST") && contains(country)) {
			return KickReason.COUNTRY;
		}

		return KickReason.NOT_BLOCKED;
	}
	
	private boolean contains(String country) {
		for (String current : LIST) {
			if (current.equalsIgnoreCase(country)) {
				return true;
			}
		}
		return false;
	}

}
