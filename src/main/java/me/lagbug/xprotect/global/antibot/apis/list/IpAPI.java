package me.lagbug.xprotect.global.antibot.apis.list;

import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.antibot.apis.DetectionAPI;

public class IpAPI extends DetectionAPI {

	public IpAPI() {
		super(true);
	}


	@SuppressWarnings("unchecked")
	@Override
	public JSONObject runDetection(String address) {
		JSONObject query = readResponse("http://ip-api.com/json/" + address	 + "?fields=proxy,country,countryCode,regionName,city,org");
		
		if (query == null) {
			return query;
		}
		
		JSONObject result = new JSONObject();
		if (query.containsKey("proxy")) {
			result.put("vpn", query.get("proxy"));
		}
		
		if (query.containsKey("country")) {
			result.put("country", query.get("country"));
		}
		
		if (query.containsKey("countryCode")) {
			result.put("countryCode", query.get("countryCode"));
		}
		
		if (query.containsKey("regionName")) {
			result.put("region", query.get("regionName"));
		}
		
		if (query.containsKey("city")) {
			result.put("city", query.get("city"));
		}
		
		if (query.containsKey("org")) {
			result.put("organisation", query.get("org"));
		}
		
		return result;
	}

}
