package me.lagbug.xprotect.spigot.common.utils.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.messaging.Messenger;

import me.clip.placeholderapi.PlaceholderAPI;

public class ActionUtil {
	
	public static boolean execute(OfflinePlayer player, List<String> actions) {
		boolean containsCancel = false;
		
		if (player == null || player.isBanned()) {
			return false;
		}
		
		List<String> actionsCopy = new ArrayList<>(Arrays.asList(actions.toArray(new String[0]).clone()));
		
		for (int i = 0; i < actions.size(); i++) {
			String action = ChatColor.translateAlternateColorCodes('&', actions.get(i).replace("%player%", player.getName())
					.replace("%player_name%", player.getName())
					.replace("%player_uuid%", player.getUniqueId().toString()));
			
			if (player.isOnline()) {
				action.replace("%player_ip%", player.getPlayer().getAddress().getAddress().getHostAddress());
			}
			
			if (CommonUtils.isPluginEnabled("PlaceholderAPI")) {
				action = PlaceholderAPI.setPlaceholders(player, action);
			}
			
			if (action.equals("CANCEL")) {
				containsCancel = true;
				continue;
			}
			
			String[] actionSplited = action.split(";;");
			String type = null, arguments = null;
			
			try {
				type = actionSplited[0];
				arguments = actionSplited[1];
			} catch (ArrayIndexOutOfBoundsException ex) {
				CommonUtils.log("Could not execute action because the syntax is wrong.");
				return false;
			}
			
	        Messenger messenger = null;
	        ByteArrayOutputStream byteArray = null;
	        DataOutputStream out = null;
	        
	        if (CommonUtils.isBungee()) {
	        	messenger = Bukkit.getMessenger();
	        	
		        if (!messenger.isOutgoingChannelRegistered(CommonUtils.getPlugin(), "BungeeCord")) {
		        	messenger.registerOutgoingPluginChannel(CommonUtils.getPlugin(), "BungeeCord");
		        }
		        
		        byteArray = new ByteArrayOutputStream();
		        out = new DataOutputStream(byteArray);
	        }
			
	        if (type.equals("DELAY")) {
	    		actionsCopy.remove(i);
	        }
	        
			switch (type) {
			case "DELAY":
				Bukkit.getScheduler().runTaskLater(CommonUtils.getPlugin(), () -> execute(player, actionsCopy), Integer.parseInt(arguments));
				return containsCancel;
			case "MESSAGE":
				if (player.isOnline()) {
					player.getPlayer().sendMessage(arguments);
				}
				break;
			case "BROADCAST":
				Bukkit.broadcastMessage(arguments);
				break;
			case "KICK":
				if (!player.isOnline()) {
					break;
				}
				
				if (!CommonUtils.isBungee()) {
					player.getPlayer().kickPlayer(arguments);	
				} else {
			        try {
			        	out.writeUTF("KickPlayer");
			        	out.writeUTF(player.getName());
			        	out.writeUTF(arguments);
						
						player.getPlayer().sendPluginMessage(CommonUtils.getPlugin(), "BungeeCord", byteArray.toByteArray());
					} catch (IOException e) {
						CommonUtils.log("Could kick player from bungeecord: " + e.getLocalizedMessage());
					}	
				}
				
				break;
			case "BAN":
				Bukkit.getBanList(Type.NAME).addBan(player.getName(), arguments, null, null);
				execute(player, "KICK");
				break;
			case "TEMPBAN":		
			    Calendar date = Calendar.getInstance();
			    date.add(Calendar.SECOND, Integer.parseInt(actionSplited[2]));
			    
				Bukkit.getBanList(Type.NAME).addBan(player.getName(), arguments, date.getTime(), null);
				execute(player, "KICK");
				break;
			case "BAN_IP":
				if (player.isOnline()) {
					Bukkit.banIP(player.getPlayer().getAddress().getAddress().getHostAddress());
					execute(player, "KICK");
				}
				break;
			case "CONSOLE_COMMAND":
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), arguments);
				break;
			case "PLAYER_COMMAND":
				if (player.isOnline()) {
					player.getPlayer().performCommand(arguments);	
				}
				break;
			case "CLEAR_CHAT":
				if (player.isOnline()) {
					player.getPlayer().sendMessage(StringUtils.repeat(" \n", 100));
				}
				break;
			case "BUNGEECORD":
				if (!player.isOnline() || !CommonUtils.isBungee()) {
					break;
				}
		        
		        try {
					out.writeUTF("Connect");
					out.writeUTF(arguments);
					
					player.getPlayer().sendPluginMessage(CommonUtils.getPlugin(), "BungeeCord", byteArray.toByteArray());
				} catch (IOException e) {
					CommonUtils.log("Could not send player to bungeecord: " + e.getLocalizedMessage());
				}
		        break;
			}
			
		}
		
		return containsCancel;
	}

	public static boolean execute(OfflinePlayer player, String action) {
		return execute(player, Arrays.asList(action));
	}
	
}
