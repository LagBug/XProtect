package me.lagbug.xprotect.global.antibot.apis.list;

import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.antibot.apis.DetectionAPI;

public class IpStack extends DetectionAPI {

	public IpStack() {
		super(true);
	}

	public static String API_KEY = "";
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject runDetection(String address) {
		if (API_KEY.isEmpty()) {
			return null;
		}
		
		JSONObject query = readResponse("http://api.ipstack.com/" + address + "?access_key=" + API_KEY);
		
		if (query == null) {
			return query;
		}
		
		JSONObject result = new JSONObject();
		
		if (query.containsKey("country")) {
			result.put("country", query.get("country"));
		}
		
		if (query.containsKey("country_code")) {
			result.put("countryCode", query.get("country_code"));
		}
		
		if (query.containsKey("region_name")) {
			result.put("region", query.get("region_name"));
		}
		
		if (query.containsKey("city")) {
			result.put("city", query.get("city"));
		}
		
		if (query.containsKey("security.is_proxy")) {
			result.put("vpn", query.get("security.is_proxy"));
		}
		
		return result;
	}

}
