package me.lagbug.xprotect.spigot.common.utils.util;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

public class CommonUtils {

	private static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static JavaPlugin plugin = null;
	private static boolean debug;

	public static String randomString(int length) {
		String result = "";
		Random r = new Random();

		for (int i = 0; i < length; i++) {
			result = result + alphabet.charAt(r.nextInt(alphabet.length()));
		}
		return result;
	}

	public static String getDate() {
		return DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
	}
	
	public static String formatNumber(int number) {
	    char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
	    int value = (int) Math.floor(Math.log10(number));
	    int base = value / 3;
	    
	    if (value >= 3 && base < suffix.length) {
	        return new DecimalFormat("#0.0").format(number / Math.pow(10, base * 3)) + suffix[base];
	    } else {
	        return new DecimalFormat("#,##0").format(number);
	    }
	}

	public static void setMaxPlayers(int slots) {
		try {
			Method serverGetHandle = Bukkit.getServer().getClass().getDeclaredMethod("getHandle");

			Object playerList = serverGetHandle.invoke(Bukkit.getServer());
			Field maxPlayersField = playerList.getClass().getSuperclass().getDeclaredField("maxPlayers");

			maxPlayersField.setAccessible(true);
			maxPlayersField.set(playerList, slots);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
				| InvocationTargetException | NoSuchMethodException ex) {
			log("Could not set the max players.");
		}
	}

	public static int randomInteger(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		return new Random().nextInt((max - min) + 1) + min;
	}

	public static String listToString(List<?> list) {
		return list.toString().replace("[", "").replace("]", "").replace(",", "");
	}

	public static String getTPS() {
		DecimalFormat format = new DecimalFormat("##.##");
		String name = Bukkit.getServer().getClass().getPackage().getName();

		Object serverInstance = null;
		Field tpsField = null;

		try {
			serverInstance = Class.forName(
					"net.minecraft.server." + name.substring(name.lastIndexOf('.') + 1) + "." + "MinecraftServer")
					.getMethod("getServer").invoke(null);
			tpsField = serverInstance.getClass().getField("recentTps");

			double tps = ((double[]) tpsField.get(serverInstance))[0];
			return tps < 20.0 ? format.format(tps) : "*20.00";
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
			return "N/A";
		}
	}

	public static Color colorFromString(String colorName) {
		try {
			return (Color) Class.forName("java.awt.Color").getField(colorName).get(null);
		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException | IllegalArgumentException
				| IllegalAccessException e) {
			return Color.BLACK;
		}
	}

	public static String materialToString(Material material) {
		String result = "";

		for (String s : material.name().split("_")) {
			result += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() + " ";
		}
		return result.substring(0, result.length() - 1);
	}

	public static boolean isPluginEnabled(String plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin) != null
				&& Bukkit.getPluginManager().getPlugin(plugin).isEnabled();
	}

	public static boolean isBungee() {
		return SpigotConfig.bungee && (!(Bukkit.getServer().getOnlineMode()));
	}

	public static void log(String... text) {
		if (debug) {
			for (String current : text) {
				Bukkit.getConsoleSender().sendMessage(getPluginName() + current + ".");
			}
		}
	}

	public static void forceLog(String... text) {
		for (String current : text) {
			Bukkit.getConsoleSender().sendMessage(getPluginName() + current + ".");
		}
	}
	
	public static String getPluginName() {
		return plugin.getName() + " -> ";
	}

	public static void initiate(JavaPlugin pluginN, boolean debugN) {
		plugin = pluginN;
		debug = debugN;
		StringUtils.initiate();
	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}
}