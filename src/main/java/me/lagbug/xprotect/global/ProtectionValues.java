package me.lagbug.xprotect.global;

/**
* Temporarily stores protection values about the server.
*
* @version 1.0
*/
public class ProtectionValues {

	//The amount of players joined every x seconds
	public static int PLAYERS = 0;

	//The amount of last players joined
	public static int LAST_PLAYERS = 0;
	
	//The total amount of players who attempted to join before a real player joined
	public static int TOTAL_PLAYERS = 0;
	
	//The amount of times the server got soft locked. Used to make server hard locked if above x times
	public static int TIMES_LOCKED = 0;
	
	//This is the current state of the server. Used to soft & hard lock the server if a bot attack is happening
	public static LockState LOCK_STATE = LockState.OPEN;
	
	//Resets some values to their default state
	public static void resetTask() {
		PLAYERS = 0;
		
		if (LOCK_STATE == LockState.SOFT) {
			LOCK_STATE = LockState.OPEN;
		}
	}
	
	public static void lastPlayersTask() {
		LAST_PLAYERS = 0;
	}
	
	public enum LockState {
		//Open if everything is fine and the server is not under attack
		OPEN,
		//Soft locked if more than x players join every y seconds
		SOFT,
		//Hard locked if have been soft locked more than x times in y seconds. Completely blocks unauthorized connections to the server for z seconds. 
		HARD
	}
}
