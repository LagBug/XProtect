package me.lagbug.xprotect.spigot.common.builders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;

public abstract class CustomInventory implements Listener {

	private FileConfiguration config;
	private Inventory inventory;
	private final Map<String, String> toReplace = new HashMap<>();
	private BukkitTask task;
	private OfflinePlayer player;
	
	private boolean autoDestroy = true;

	public CustomInventory(FileConfiguration config) {
		initiate(config);
	}

	public CustomInventory(FileConfiguration config, boolean autoDestroy) {
		this(config);
		this.autoDestroy = autoDestroy;
	}

	private void initiate(FileConfiguration config) {
		Bukkit.getPluginManager().registerEvents(this, CommonUtils.getPlugin());

		this.setConfig(config);
		String title = config.getString("title");

		for (String key : toReplace.keySet()) {
			if (title.contains(key)) {
				title = title.replace(key, toReplace.get(key));
			}
		}
		
		inventory = Bukkit.createInventory(null, config.getInt("slots"), ChatColor.translateAlternateColorCodes('&', title));
		
		task = new BukkitRunnable() {
			@Override
			public void run() {
				update();
			}
		}.runTaskTimer(CommonUtils.getPlugin(), 0, config.getInt("refresh"));
	}

	public void update() {
		Player player = (Player) inventory.getViewers().get(0);
		onUpdate(player);		
		
		for (String key : config.getConfigurationSection("contents").getKeys(false)) {
			String stackData[] = config.getString("contents." + key + ".item").split(";"),
					name = config.getString("contents." + key + ".name");
			List<String> lore = config.getStringList("contents." + key + ".lore");

			for (String subKey : toReplace.keySet()) {
				if (name.contains(subKey)) {
					name = name.replace(subKey, toReplace.get(subKey));
				}

				if (CommonUtils.isPluginEnabled("PlaceholderAPI")) {
					name = PlaceholderAPI.setPlaceholders(getPlayer(), name);
				}

				for (int i = 0; i < lore.size(); i++) {
					if (lore.get(i).contains(subKey)) {
						lore.set(i, lore.get(i).replace(subKey, toReplace.get(subKey)));
					}
				}
			}

			if (CommonUtils.isPluginEnabled("PlaceholderAPI")) {
				lore.replaceAll(text -> PlaceholderAPI.setPlaceholders(getPlayer(), text));
			}

			ItemStack item = new ItemBuilder(Material.valueOf(stackData[0]), Integer.valueOf(stackData[1]),
					Byte.valueOf(stackData[2])).setDisplayName(name).setLore(lore).build();

			inventory.setItem(Integer.valueOf(key), item);
		}
	}

	public abstract void onClick(Player player, String action, ItemStack item, int slot, ClickType click);

	public abstract void onUpdate(Player player);

	public abstract void onClose(Player player);

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null || e.getClickedInventory() == null || e.getInventory() == null
				|| e.getCurrentItem().getType().equals(Material.AIR) || !(e.getWhoClicked() instanceof Player)
				|| !e.getClickedInventory().equals(inventory)) {
			return;
		}

		String action = "NONE";
		Player player = (Player) e.getWhoClicked();

		for (String key : config.getConfigurationSection("contents").getKeys(false)) {
			if (e.getSlot() == Integer.parseInt(key) && config.contains("contents." + key + ".action")) {
				action = config.getString("contents." + key + ".action").toUpperCase();
				break;
			}
		}

		if (action.equals("CLOSE_INVENTORY")) {
			player.closeInventory();
		}

		e.setCancelled(true);
		onClick(player, action, e.getCurrentItem(), e.getSlot(), e.getClick());
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (e.getInventory() == null || !e.getInventory().equals(inventory)) {
			return;
		}

		if (autoDestroy) {
			destroy();
		}
		Player player = (Player) inventory.getViewers().get(0);
		onClose(player);
	}

	public CustomInventory replace(String key, String value) {
		if (toReplace.containsKey(key)) {
			toReplace.remove(key);
		}

		toReplace.put(key, value);
		return this;
	}

	public CustomInventory replace(String key, int value) {
		replace(key, String.valueOf(value));
		return this;
	}

	public CustomInventory openInventory(Player player) {
		player.openInventory(inventory);
		return this;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void destroy() {
		task.cancel();
		HandlerList.unregisterAll(this);
	}

	public CustomInventory setConfig(FileConfiguration config) {
		this.config = config;
		return this;
	}

	public OfflinePlayer getPlayer() {
		return player;
	}

	public void setPlayer(OfflinePlayer player) {
		this.player = player;
	}
}
