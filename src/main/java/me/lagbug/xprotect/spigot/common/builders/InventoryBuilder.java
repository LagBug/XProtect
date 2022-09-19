package me.lagbug.xprotect.spigot.common.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.lagbug.xprotect.spigot.XProtect;

public class InventoryBuilder {

	private YamlConfiguration config = null;

	private final Map<String, String> toReplace = new HashMap<>();
	private final Map<Integer, ItemStack> toAdd = new HashMap<>();
	private Inventory inventory;

	private int slots;
	private String title;

	public InventoryBuilder(YamlConfiguration config) {
		this.config = config;
	}

	public InventoryBuilder() {
	}

	public InventoryBuilder setSlots(int slots) {
		this.slots = slots;
		return this;
	}

	public InventoryBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public InventoryBuilder addItem(ItemStack item, int slot) {
		toAdd.put(slot, item);
		return this;
	}

	public ItemStack[] getItems() {
		return toAdd.values().toArray(new ItemStack[toAdd.size()]);
	}

	public InventoryBuilder replace(String key, String value) {
		toReplace.put(key, value);
		return this;
	}

	public Inventory build() {
		if (config != null) {
			String title = config.getString("title");
			
			for (String key : toReplace.keySet()) {
				if (title.contains(key)) {
					title = title.replace(key, toReplace.get(key));
				}
			}
			
			inventory = Bukkit.createInventory(null, config.getInt("slots"),
					ChatColor.translateAlternateColorCodes('&', title));
			
			for (String key : config.getConfigurationSection("contents").getKeys(false)) {
				String stackData[] = config.getString("contents." + key + ".item").split(";"), name = config.getString("contents." + key + ".name");
				List<String> lore = config.getStringList("contents." + key + ".lore");
				
				for (String key1 : toReplace.keySet()) {
					if (name.contains(key1)) {
						name = name.replace(key1, toReplace.get(key1));
					}
					
				    for (int i = 0; i < lore.size(); i++) {
				        if (lore.get(i).contains(key1)) {
				            lore.set(i, lore.get(i).replace(key1, toReplace.get(key1)));
				        } 
				    }
				}
				
				ItemStack item = new ItemBuilder(
						stackData[0].equals("FLASHING_GLASS") ? Material.valueOf("STAINED_GLASS_PANE")
								: Material.valueOf(stackData[0]),
						Integer.valueOf(stackData[1]),
						stackData[0].equals("FLASHING_GLASS") ? (byte) new Random().nextInt(10)
								: Byte.valueOf(stackData[2])).setDisplayName(name).setLore(lore).build();

				InventoryFill invf = new InventoryFill(inventory);
				new BukkitRunnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						if (inventory.getViewers().isEmpty()) {
							cancel();
						}

						if (stackData[0].equals("FLASHING_GLASS")) {
							item.setDurability((byte) new Random().nextInt(10));
						}

						switch (key.toLowerCase()) {
						case "border":
							invf.fillSidesWithItem(item);
							break;
						case "air":
							break;
						default:
							inventory.setItem(Integer.valueOf(key), item);
							break;
						}

					}
				}.runTaskTimer(XProtect.getPlugin(XProtect.class), 0, config.getInt("animationDelay"));
			}
			return inventory;
		}

		inventory = Bukkit.createInventory(null, slots, ChatColor.translateAlternateColorCodes('&', this.title));
		for (Integer i : toAdd.keySet()) {
			inventory.setItem(i, toAdd.get(i));
		}

		return inventory;
	}
	
	public class InventoryFill {

	    private final Inventory inv;
	    private final List<Integer> sideSlots = new ArrayList<>();

	    public InventoryFill(Inventory inv) {
	        this.inv = inv;
	    }

	    public InventoryFill fillSidesWithItem(ItemStack item) {
	        int size = inv.getSize();
	        int rows = size / 9;

	        if(rows >= 3) {
	            for (int i = 0; i <= 8; i++) {
	                this.inv.setItem(i, item);
	                sideSlots.add(i);
	            }

	            for(int s = 8; s < (this.inv.getSize() - 9); s += 9) {
	                int lastSlot = s + 1;
	                this.inv.setItem(s, item);
	                this.inv.setItem(lastSlot, item);

	                sideSlots.add(s);
	                sideSlots.add(lastSlot);
	            }

	            for (int lr = (this.inv.getSize() - 9); lr < this.inv.getSize(); lr++) {
	                this.inv.setItem(lr, item);

	                sideSlots.add(lr);
	            }
	        }
	        return this;
	    }

	    public List<Integer> getNonSideSlots() {
	        List<Integer> availableSlots = new ArrayList<>();

	        for (int i = 0; i < this.inv.getSize(); i++) {
	            if(!this.sideSlots.contains(i)) {
	                availableSlots.add(i);
	            }
	        }

	        return availableSlots;
	    }
	}
}