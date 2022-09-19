package me.lagbug.xprotect.api;

import java.util.UUID;

import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.antibot.apis.IpInfo;

public class XProtectAPI {
	
	public static boolean isVerified(UUID uuid) {
		return new Checker(uuid).isVerified();
	}
	
	public static boolean isBlacklisted(UUID uuid) {
		return new Checker(uuid).isVerified();
	}
	
	public static JSONObject getConnectionData(String address) {
		return IpInfo.getInformation(address);
	}

}
