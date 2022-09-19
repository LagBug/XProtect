package me.lagbug.xprotect.global.antibot.apis.list;

import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.antibot.apis.DetectionAPI;

public class IpAPICo extends DetectionAPI {

	public IpAPICo() {
		super(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject runDetection(String address) {
		JSONObject query = readResponse("https://ipapi.co/" + address + "/json/");
		
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
		
		if (query.containsKey("region")) {
			result.put("region", query.get("region"));
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
