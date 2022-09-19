package me.lagbug.xprotect.spigot.utils;

import org.bukkit.configuration.file.FileConfiguration;

import me.lagbug.xprotect.global.antibot.apis.list.GetIPIntel;
import me.lagbug.xprotect.global.antibot.apis.list.IpAPI;
import me.lagbug.xprotect.global.antibot.apis.list.IpAPICo;
import me.lagbug.xprotect.global.antibot.apis.list.IpQualityScore;
import me.lagbug.xprotect.global.antibot.apis.list.IpStack;
import me.lagbug.xprotect.global.antibot.apis.list.Proxycheck;
import me.lagbug.xprotect.global.antibot.apis.list.TeohIO;
import me.lagbug.xprotect.global.antibot.checks.list.AntiBadConnection;
import me.lagbug.xprotect.global.antibot.checks.list.CountryChecking;
import me.lagbug.xprotect.global.antibot.checks.list.IpLimiter;
import me.lagbug.xprotect.global.antibot.checks.list.JoinLimiter;
import me.lagbug.xprotect.global.antibot.checks.list.NameChecking;
import me.lagbug.xprotect.global.antibot.checks.list.VerificationCheck;
import me.lagbug.xprotect.global.enums.KickReason.KickMessage;
import me.lagbug.xprotect.spigot.XProtect;

public class SpigotGlobalSettings {

	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	public SpigotGlobalSettings() {
	
		// Settings for the JoinLimiter class
		FileConfiguration config = plugin.getAntibotFile();
		JoinLimiter.setEnabled(config.getBoolean("antibot.joinLimiter.enabled"));
		JoinLimiter.PLAYERS = config.getInt("antibot.joinLimiter.players");
		JoinLimiter.SECONDS = config.getInt("antibot.joinLimiter.seconds");
		
		// Settings for the NameChecking class
		NameChecking.setEnabled(config.getBoolean("antibot.nameChecking.enabled"));
		NameChecking.NAMES = config.getStringList("antibot.nameChecking.contains").toArray(new String[0]);
		
		// Settings for the IpLimiter class
		IpLimiter.setEnabled(config.getBoolean("antibot.ipLimiter.enabled"));
		IpLimiter.AMOUNT = config.getInt("antibot.ipLimiter.limit");

		// Settings for the AntiBadConnection class
		AntiBadConnection.setEnabled(config.getBoolean("antibot.antiBadConnection.enabled"));
		AntiBadConnection.BLOCK_ABUSE = config.getBoolean("antibot.antiBadConnection.blocks.abuse");
		AntiBadConnection.BLOCK_VPNS = config.getBoolean("antibot.antiBadConnection.blocks.vpn");
		AntiBadConnection.BLOCK_BOTS = config.getBoolean("antibot.antiBadConnection.blocks.bot");
		AntiBadConnection.FRAUD_SCORE = config.getInt("antibot.antiBadConnection.blocks.fraudScore");
		
		// Settings for the CountryChecking class
		CountryChecking.setEnabled(config.getBoolean("antibot.countryChecking.enabled"));
		CountryChecking.MODE = config.getString("antibot.countryChecking.type");
		CountryChecking.LIST = config.getStringList("antibot.countryChecking.list").toArray(new String[0]);
		
		// Settings for VerificationCheck
		VerificationCheck.PERSISTENT = config.getBoolean("settings.antibot");
		
		// Settings for the KickMessage class
		KickMessage.BLACKLISTED = plugin.getMessage("kick.blacklisted");
		KickMessage.NAME = plugin.getMessage("kick.name");
		KickMessage.IP = plugin.getMessage("kick.ip");
		KickMessage.LOCKED = plugin.getMessage("kick.locked");
		KickMessage.BOT = plugin.getMessage("kick.bot");
		KickMessage.ABUSE = plugin.getMessage("kick.abuse");
		KickMessage.RISK = plugin.getMessage("kick.risk");
		KickMessage.COUNTRY = plugin.getMessage("kick.country");
		KickMessage.VPN = plugin.getMessage("kick.vpn");
		
		// Settings for IpQualityScore
		IpQualityScore.setEnabled(config.getBoolean("antibot.apis.ipqualityscore.enabled"));
		IpQualityScore.API_KEY = config.getString("antibot.apis.ipqualityscore.apiKey");
		
		// Settings for Proxycheck
		Proxycheck.setEnabled(config.getBoolean("antibot.apis.proxycheck.enabled"));
		Proxycheck.API_KEY = config.getString("antibot.apis.proxycheck.apiKey");
		
		// Settings for IpStack
		IpStack.setEnabled(config.getBoolean("antibot.apis.ipstack.enabled"));
		IpStack.API_KEY = config.getString("antibot.apis.ipstack.apiKey");
		
		// Settings for GetIPIntel
		GetIPIntel.setEnabled(config.getBoolean("antibot.apis.getIpIntel.enabled"));
		GetIPIntel.EMAIL = config.getString("antibot.apis.getIpIntel.email");
		
		// Settings for IpAPI
		IpAPI.setEnabled(config.getBoolean("antibot.apis.ipApi.enabled"));
		
		// Settings for IpAPICo
		IpAPICo.setEnabled(config.getBoolean("antibot.apis.ipApiCo.enabled"));
		
		// Settings for TeohIO
		TeohIO.setEnabled(config.getBoolean("antibot.apis.teohIO.enabled"));
	}

}