package me.lagbug.xprotect.global.antibot.apis.list;

import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.antibot.apis.DetectionAPI;

public class IpQualityScore extends DetectionAPI {

	public IpQualityScore() {
		super(true);
	}

	public static String API_KEY = null;

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject runDetection(String address) {
		JSONObject query = readResponse("https://www.ipqualityscore.com/api/json/ip/" + API_KEY + "/" + address
				+ "?strictness=1&fast=true");
		
		if (query == null) {
			return query;
		}
		
		JSONObject result = new JSONObject();
		if (query.containsKey("vpn")) {
			if (query.get("vpn").equals(false)) {
				if (query.containsKey("proxy")) {
					result.put("vpn", query.get("proxy"));	
				}
			} else {
				result.put("vpn", query.get("vpn"));
			}
		}
		
		if (query.containsKey("recent_abuse")) {
			result.put("abusive", query.get("recent_abuse"));
		}
		
		if (query.containsKey("bot_status")) {
			result.put("bot", query.get("bot_status"));
		}
		
		if (query.containsKey("fraud_score")) {
			result.put("fraudScore", query.get("fraud_score"));
		}
		
		if (query.containsKey("country")) {
			result.put("country", query.get("country"));
		}
		
		if (query.containsKey("country_code")) {
			result.put("countryCode", query.get("country_code"));
		}
		
		if (query.containsKey("city")) {
			result.put("city", query.get("city"));
		}
		
		if (query.containsKey("region")) {
			result.put("region", query.get("region"));
		}
		
		if (query.containsKey("organization")) {
			result.put("organisation", query.get("organization"));
		}
		
		return result;
	}

}
