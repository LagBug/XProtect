package me.lagbug.xprotect.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player fails to complete the captcha. Exension of the Spigot Event class
 *
 * @version 1.0
 */
public class PlayerFailCaptchaEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private boolean isCancelled;
	private final Player player;
	
	public PlayerFailCaptchaEvent(Player player) {
		this.player = player;
	}
	
	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
		
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public Player getPlayer() {
		return player;
	}
}
