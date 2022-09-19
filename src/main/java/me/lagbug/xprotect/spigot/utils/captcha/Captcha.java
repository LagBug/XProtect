package me.lagbug.xprotect.spigot.utils.captcha;

import org.bukkit.entity.Player;

/**
* Simple class to extend in order to add a
* new captcha type to this program.
*
* @version 1.0
*/
public abstract class Captcha {
	
	/*
	* Used to specify what this captcha does 
	*/
	public abstract void execute(Player player);
	
}