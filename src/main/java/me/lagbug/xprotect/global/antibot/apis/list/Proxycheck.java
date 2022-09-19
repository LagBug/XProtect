package me.lagbug.xprotect.global.antibot.apis.list;

import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.antibot.apis.DetectionAPI;

public class Proxycheck extends DetectionAPI {

	public Proxycheck() {
		super(true);
	}

	public static String API_KEY = "111111-222222-333333-444444";
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject runDetection(String address) {
		JSONObject query = (JSONObject) readResponse("http://proxycheck.io/v2/" + address + "?key=" + API_KEY + "&risk=1&vpn=1&asn=1").get(address);
		
		if (query == null) {
			return query;
		}
		
		JSONObject result = (JSONObject) new JSONObject();
		
		if (query.containsKey("proxy")) {
			result.put("vpn", query.get("proxy").equals("yes"));
		}

		if (query.containsKey("risk")) {
			result.put("fraudScore", query.get("risk"));
		}
		
		if (query.containsKey("country")) {
			result.put("country", query.get("country"));
		}
		
		if (query.containsKey("isocode")) {
			result.put("countryCode", query.get("isocode"));
		}
		
		if (query.containsKey("city")) {
			result.put("city", query.get("city"));
		}
		
		if (query.containsKey("region")) {
			result.put("region", query.get("region"));
		}
		
		if (query.containsKey("provider")) {
			result.put("organisation", query.get("provider"));
		}
		
		return result;
	}

}
