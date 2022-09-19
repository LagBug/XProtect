package me.lagbug.xprotect.global.antibot.apis.list;

import org.json.simple.JSONObject;

import me.lagbug.xprotect.global.antibot.apis.DetectionAPI;

public class GetIPIntel extends DetectionAPI {

	public GetIPIntel() {
		super(true);
	}


	public static String EMAIL = null;
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject runDetection(String address) {
		if (EMAIL.isEmpty()) {
			return null;
		}
		
		JSONObject query = readResponse("http://check.getipintel.net/check.php?ip=" + address + "&contact=" + EMAIL + "&flags=m&format=json");
		
		if (query == null) {
			return query;
		}
		
		if (query.get("status").equals("error")) {
			return null;
		}
		
		JSONObject result = new JSONObject();
		if (query.containsKey("result")) {
			result.put("fraudScore", Integer.valueOf(query.get("result").toString()) * 100);
		}
		
		return result;
	}

}
