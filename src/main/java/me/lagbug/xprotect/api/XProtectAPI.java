package me.lagbug.xprotect.api;

import java.util.UUID;

import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.global.antibot.apis.IpInfo;

/**
 * Class used to access the API of the program
 *
 * @version 1.0
 */
public class XProtectAPI {

	/**
	 * Returns whether a UUID (player) is verified using the Checker class
	 *
	 * @return the verification status of a UUID
	 */
	public static boolean isVerified(UUID uuid) {
		return new Checker(uuid).isVerified();
	}

	/**
	 * Checks if a UUID is blacklisted
	 *
	 * @return the blacklist status of a UUID
	 */
	public static boolean isBlacklisted(UUID uuid) {
		return new Checker(uuid).isVerified();
	}


	/**
	 * Fetches all the data available for an IP (vpn status, location, etc.) and
	 * returns it as a JSONObject
	 *
	 * @return JSONObject with connnection data of an IP
	 */
	public static JSONObject getConnectionData(String address) {
		return IpInfo.getInformation(address);
	}

}
