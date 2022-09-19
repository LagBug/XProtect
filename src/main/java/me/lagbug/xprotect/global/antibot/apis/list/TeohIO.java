package me.lagbug.xprotect.global.antibot.apis.list;

import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.antibot.apis.DetectionAPI;

public class TeohIO extends DetectionAPI {

	public TeohIO() {
		super(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject runDetection(String address) {
		JSONObject query = readResponse("https://ip.teoh.io/api/vpn/" + address);
		
		if (query == null) {
			return query;
		}
		
		JSONObject result = new JSONObject();
		if (query.containsKey("vpn_or_proxy")) {
			result.put("vpn", query.get("vpn_or_proxy").equals("yes"));
		}

		if (query.containsKey("risk")) {
			result.put("bot", query.get("risk").equals("high"));
		}
		
		if (query.containsKey("organization")) {
			result.put("organisation", query.get("organization"));
		}
		
		return result;
	}

}
