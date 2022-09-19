package me.lagbug.xprotect.spigot.common.utils.communication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;

public class Actionbar {

	private static String nmsver;
	private static boolean useOldMethods = false;
	private static final Map<Player, Integer> toSend = new HashMap<>();

	static {		
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		
		if ((nmsver.equalsIgnoreCase("v1_8_R1")) || (nmsver.startsWith("v1_7_"))) {
			useOldMethods = true;
		}
	}
	
	public static void send(Player player, String message) {
		if (player == null || !player.isOnline()) {
			return;
		}

		try {
			Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
			Object craftPlayer = craftPlayerClass.cast(player);

			Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
			Class<?> packetClass = Class.forName("net.minecraft.server." + nmsver + ".Packet");
			Object packet;
			if (useOldMethods) {
				Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
				Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
				Method m3 = chatSerializerClass.getDeclaredMethod("a", String.class);
				Object cbc = iChatBaseComponentClass.cast(m3.invoke(chatSerializerClass, "{\"text\": \"" + message + "\"}"));
				packet = packetPlayOutChatClass.getConstructor(new Class[] { iChatBaseComponentClass, Byte.TYPE }).newInstance(cbc, (byte) 2);
			} else {
				Class<?> chatComponentTextClass = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
				Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
				try {
					Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsver + ".ChatMessageType");
					Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
					Object chatMessageType = null;
					for (Object obj : chatMessageTypes) {
						if (obj.toString().equals("GAME_INFO")) {
							chatMessageType = obj;
						}
					}
					Object chatCompontentText = chatComponentTextClass.getConstructor(new Class[] { String.class }).newInstance(message);
					packet = packetPlayOutChatClass.getConstructor(new Class[] { iChatBaseComponentClass, chatMessageTypeClass }).newInstance(chatCompontentText, chatMessageType);
				} catch (ClassNotFoundException cnfex) {
					Object chatCompontentText = chatComponentTextClass.getConstructor(new Class[] { String.class }).newInstance(message);
					packet = packetPlayOutChatClass.getConstructor(new Class[] { iChatBaseComponentClass, Byte.TYPE }).newInstance(chatCompontentText, (byte) 2);
				}
			}
			Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
			Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
			Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
			Object playerConnection = playerConnectionField.get(craftPlayerHandle);
			Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
			sendPacketMethod.invoke(playerConnection, packet);
		} catch (Exception ignored) { }
	}

	public static void cancel(Player player) {
		if (toSend.containsKey(player)) {
			Bukkit.getScheduler().cancelTask(toSend.get(player));
			toSend.remove(player);
		}
	}
	
	public static void sendForever(Player player, String message) {
		if (toSend.containsKey(player)) {
			cancel(player);
		}
		
		toSend.put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(CommonUtils.getPlugin(), () -> send(player, message), 0L, 40L));
    }
}