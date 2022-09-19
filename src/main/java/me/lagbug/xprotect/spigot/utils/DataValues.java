package me.lagbug.xprotect.spigot.utils;

/**
 * Stores protection values about the server.
 *
 * @version 1.0
 */
public class DataValues {

	// The amount of players joined every x seconds
	public static int BLOCKED_BOTS = 0;

	// The total amount of players who attempted to join before a real player joined
	public static int CHECKED_CONNECTIONS = 0;

	// The amount of times the server got soft locked. Used to make server hard locked if above x times
	public static int SUCCESSFUL_CAPTCHAS = 0;

	// The amount of times the server got soft locked. Used to make server hard locked if above x times
	public static int FAILED_CAPTCHAS = 0;
}