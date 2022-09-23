package me.lagbug.xprotect.global.enums;

/**
 * Enum used to list the reason a player could be blocked 
 *
 * @version 1.0
 */
public enum KickReason {
	
	NOT_BLOCKED("Not Blocked"),
	VERIFIED("Not Blocked"),
	BLACKLISTED(KickMessage.BLACKLISTED),
	NAME(KickMessage.NAME),
	IP(KickMessage.IP),
	LOCKED(KickMessage.LOCKED),
	BOT(KickMessage.BOT),
	ABUSE(KickMessage.ABUSE),
	RISK(KickMessage.RISK),
	COUNTRY(KickMessage.COUNTRY),
	VPN(KickMessage.VPN);
	
	
	private String message = "N/A";
	
	private KickReason(String message) {
		this.message = message;
	}
	
	public String getKickMessage() {
		return message;
	}

	// A list of the kick messages used by the plugin
	public static class KickMessage {
		public static String BLACKLISTED = "You have been blacklisted from the server";
		public static String NAME = "Your name indicates that you're a potential bot";
		public static String IP = "You can only have online up to 3 accounts at the same time";
		public static String LOCKED = "Too many players are joining at the same time";
		public static String BOT = "Your IP is linked to bot activity so we can not allow you to join";
		public static String ABUSE = "Your IP is linked to recent abuse, hence why you can not join";
		public static String RISK = "Your IPs fraud score is too risky, hence why you can not join";
		public static String COUNTRY = "Your country is not allowed to join our server";
		public static String VPN = "VPNs or proxies are not allowed in this server";
	}
}
