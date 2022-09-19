package me.lagbug.xprotect.global.antibot.apis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.antibot.apis.list.GetIPIntel;
import me.lagbug.xprotect.global.antibot.apis.list.IpAPI;
import me.lagbug.xprotect.global.antibot.apis.list.IpAPICo;
import me.lagbug.xprotect.global.antibot.apis.list.IpQualityScore;
import me.lagbug.xprotect.global.antibot.apis.list.IpStack;
import me.lagbug.xprotect.global.antibot.apis.list.Proxycheck;
import me.lagbug.xprotect.global.antibot.apis.list.TeohIO;

public class IpInfo {

	private static final List<DetectionAPI> APIS = Arrays.asList(new IpQualityScore(), new Proxycheck(), new IpAPI(),
			new TeohIO(), new IpAPICo(), new IpStack(), new GetIPIntel());
	private static final List<String> CHECKS = Arrays.asList("vpn", "bot", "country", "countryCode", "city", "region",
			"organisation", "fraudScore", "abusive");

	private static final Map<String, JSONObject> CACHE = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static JSONObject getInformation(String address) {
		if (CACHE.containsKey(address)) {
			return CACHE.get(address);
		}

		JSONObject result = new JSONObject();
		if (address == null) {
			for (String check : CHECKS) {
				result.put(check, "N/A");
			}
			return result;
		}

		for (DetectionAPI api : APIS) {
			if (api.isEnabled()) {
				JSONObject apiResult = api.runDetection(address);

				if (apiResult != null) {
					for (Object key : apiResult.keySet()) {
						if (result.keySet().size() >= CHECKS.size() - 2) {
							CACHE.put(address, result);
							return result;
						}

						if (!result.containsKey(key)) {
							result.put(key, apiResult.get(key));
						}

					}
				}

			}
		}

		for (String check : CHECKS) {
			if (!result.containsKey(check)) {
				result.put(check, "N/A");
			}
		}

		CACHE.put(address, result);
		return result;
	}
	
	public static void resetCache() {
		CACHE.clear();
	}
}