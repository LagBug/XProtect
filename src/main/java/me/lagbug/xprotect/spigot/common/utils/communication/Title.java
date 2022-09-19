package me.lagbug.xprotect.spigot.common.utils.communication;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;

public class Title {
	
	private static final Map<Player, Integer> toSend = new HashMap<>();
    
    public static void cancel(Player player) {
    	send(player, 0, "", "");
    	
		if (toSend.containsKey(player)) {
			Bukkit.getScheduler().cancelTask(toSend.get(player));
			toSend.remove(player);
		}
	}
	
	public static void sendForever(Player player, String title, String subtitle) {
		if (toSend.containsKey(player)) {
			cancel(player);
		}
		
		toSend.put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(CommonUtils.getPlugin(), () -> send(player, 17, title, subtitle), 0, 40));
    }

    @SuppressWarnings("deprecation")
	public static void send(Player player, Integer stay, String title, String subtitle) {
		if (player == null || !player.isOnline()) {
			return;
		}
		
		title = ChatColor.translateAlternateColorCodes('&', title);
        title = title.replace("%player%", player.getDisplayName());
        subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
        subtitle = subtitle.replace("%player%", player.getDisplayName());
        
		if (Bukkit.getVersion().contains("1.8")) {
			try {
	            if (title != null) {
	                title = ChatColor.translateAlternateColorCodes('&', title);
	                title = title.replace("%player%", player.getDisplayName());
	                Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
	                Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
	                Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
	                Object titlePacket = subtitleConstructor.newInstance(e, chatTitle, 10, stay, 10);
	                sendPacket(player, titlePacket);
	                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
	                chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
	                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
	                titlePacket = subtitleConstructor.newInstance(e, chatTitle);
	                sendPacket(player, titlePacket);
	            }
	            if (subtitle != null) {
	                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
	                subtitle = subtitle.replace("%player%", player.getDisplayName());
	                Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
	                Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
	                Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
	                Object subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, 10, stay, 10);
	                sendPacket(player, subtitlePacket);
	                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
	                chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
	                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
	                subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, 10, stay, 10);
	                sendPacket(player, subtitlePacket);
	            }
	        } catch (Exception ignored) {}
		} else if (Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")) {
			player.sendTitle(title, subtitle);
		} else {
			player.sendTitle(title, subtitle, 10, stay, 10);
		}
    }
    
    public static void sendPacket(final Player player, final Object packet) {
        try {
            final Object handle = player.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(player, new Object[0]);
            final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Class<?> getNMSClass(final String name) {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}