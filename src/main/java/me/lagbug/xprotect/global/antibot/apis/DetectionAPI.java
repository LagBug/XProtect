package me.lagbug.xprotect.global.antibot.apis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class DetectionAPI {
	
	private static boolean isEnabled = true;
	
	public DetectionAPI(boolean isEnabled) {
		DetectionAPI.isEnabled = isEnabled;
	}
	
	public abstract JSONObject runDetection(String address);

	protected boolean isEnabled() {
		return isEnabled;
	}
	
	public static void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
	
	protected JSONObject readResponse(String url) {
		try {
			URLConnection connection = new URL(url).openConnection();
			StringBuilder response = new StringBuilder();
			String line = "";

			connection.setConnectTimeout(1000);
			connection.setRequestProperty("User-Agent", "VPNDetection Library");

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = in.readLine()) != null) {
				response.append(line);
			}

			in.close();
			return (JSONObject) new JSONParser().parse(response.toString());
		} catch (NullPointerException | ParseException | IOException e) {
			return null;
		}
	}
}
