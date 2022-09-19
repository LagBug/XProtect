package me.lagbug.xprotect.spigot.common.builders;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemBuilder {

	private final ItemStack item;
	private final ItemMeta meta;

	@SuppressWarnings("deprecation")
	public ItemBuilder(Material material, int amount, byte id) {
		item = new ItemStack(material, amount, id);
		meta = item.getItemMeta();
	}

	public ItemBuilder(Material material) {
		this(material, 1, (byte) 0);
	}

	public ItemBuilder setDisplayName(String name) {
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		return this;
	}

	public ItemBuilder setLore(List<String> newLore) {
		List<String> loreR = new ArrayList<>();
		for (String s : newLore) {
			loreR.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		meta.setLore(loreR);
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		meta.addEnchant(enchantment, level, true);
		return this;
	}

	public ItemBuilder addItemFlags(ItemFlag... flags) {
		meta.addItemFlags(flags);
		return this;
	}

	public void setLeatherArmorColor(Color color) {
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);

		item.setItemMeta(meta);
	}

	public ItemStack build() {
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		return item;
	}
}