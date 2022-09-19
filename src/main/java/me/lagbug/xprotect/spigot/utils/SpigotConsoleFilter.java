package me.lagbug.xprotect.spigot.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import me.lagbug.xprotect.global.ProtectionValues;
import me.lagbug.xprotect.global.ProtectionValues.LockState;

/**	
 * This class is used to filter the output of the console during bot attacks in
 * order to keep it clean.
 *
 * @version 1.0
 */
public class SpigotConsoleFilter implements Filter {

	// Array of words sent when a player leaves the server
	private static final String[] toCheck = { "lost connection", "disconnected", "disconnecting", "handleDisconnection", "timed out" };

	@Override
	public Result filter(LogEvent event) {
		// The result to be given in the filter
		Result result = Result.ACCEPT;

		// If only the LockState is not open
		if (ProtectionValues.LOCK_STATE == LockState.OPEN) {
			return result;
		}

		// Getting the message
		String message = event.getMessage().getFormattedMessage().toLowerCase();

		// Looping through each word from above
		for (String current : toCheck) {
			// If it's contained in the message, we change the result to deny
			if (message.contains(current)) {
				result = Result.DENY;
				break;
			}
		}

		return result;
	}

	@Override
	public Result getOnMatch() {
		return null;
	}

	@Override
	public Result getOnMismatch() {
		return null;
	}

	@Override
	public State getState() {
		return null;
	}

	@Override
	public void initialize() {
	}

	@Override
	public boolean isStarted() {
		return false;
	}

	@Override
	public boolean isStopped() {
		return false;
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object... arg4) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, Object arg3, Throwable arg4) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, Message arg3, Throwable arg4) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8, Object arg9) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8, Object arg9, Object arg10) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8, Object arg9, Object arg10, Object arg11) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
		return null;
	}

	@Override
	public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
			Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
		return null;
	}
}