package me.lagbug.xprotect.spigot.captchas;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.builders.InventoryBuilder;
import me.lagbug.xprotect.spigot.common.builders.ItemBuilder;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

public class Different extends Captcha {

	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	@Override
	public void execute(Player player) {
		int slots = plugin.getCaptchasFile().getInt("captchas.different.slots");
		InventoryBuilder inv = new InventoryBuilder().setSlots(slots).setTitle(plugin.getCaptchasFile().getString("captchas.different.title"));
		
		String[] sameData = plugin.getCaptchasFile().getString("captchas.different.same.item").split(";"), diffData = plugin.getCaptchasFile().getString("captchas.different.different.item").split(";");		

		ItemStack same = new ItemBuilder(Material.valueOf(sameData[0]), Integer.parseInt(sameData[1]),
				Byte.parseByte(sameData[2])).setDisplayName(getName("same")).setLore(getLore("same")).build(),
				
				diff = new ItemBuilder(Material.valueOf(diffData[0]), Integer.parseInt(diffData[1]),
						Byte.parseByte(diffData[2])).setDisplayName(getName("different")).setLore(getLore("different")).build();
		
		for (int i = 0; i < slots; i++) {
			inv.addItem(same, i);
		}

		inv.addItem(diff, new Random().nextInt(slots));

		Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inv.build()));
		plugin.getToVerify().put(player, plugin.getCode() + diff.getType().name());

		Utils.sendInstructions(player, plugin.getMessage("instructions.different"), true);
	}
	
	public String getName(String data) {
		return ChatColor.translateAlternateColorCodes('&', plugin.getCaptchasFile().getString("captchas.different." + data + ".name"));
	}
	
	public List<String> getLore(String data) {
		return plugin.getCaptchasFile().getStringList("captchas.different." + data + ".lore");
	}
}