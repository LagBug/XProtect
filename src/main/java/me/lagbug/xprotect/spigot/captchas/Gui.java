package me.lagbug.xprotect.spigot.captchas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.common.builders.InventoryBuilder;
import me.lagbug.xprotect.spigot.common.utils.util.CommonUtils;
import me.lagbug.xprotect.spigot.utils.Utils;
import me.lagbug.xprotect.spigot.utils.captcha.Captcha;

public class Gui extends Captcha {

	private final XProtect plugin = XProtect.getPlugin(XProtect.class);
	
	@Override
	public void execute(Player player) {
		int slots = plugin.getCaptchasFile().getInt("captchas.gui.slots");
		InventoryBuilder inv = new InventoryBuilder().setSlots(slots);

		List<Material> materials = new ArrayList<>();

		for (Material mat : Material.values()) {
			if ((mat.isEdible() || mat.isBlock() || mat.isSolid()) && mat != Material.AIR && !mat.name().contains("FURNACE")) {
				materials.add(mat);
			}
		}

		for (int i = 0; i < slots; i++) {
			inv.addItem(new ItemStack(materials.get(new Random().nextInt(materials.size()))), i);
		}

		String title = plugin.getCaptchasFile().getString("captchas.gui.title"), name = "N/A";

		inv.setTitle(title);
		for (int i = 0; i < inv.build().getSize(); i++) {
			while (inv.build().getItem(i) == null) {
				inv.addItem(new ItemStack(materials.get(new Random().nextInt(materials.size()))), i);
			}
		}

		Material choosen = inv.getItems()[new Random().nextInt(inv.getItems().length)].getType();
		name = CommonUtils.materialToString(choosen);

		title = title.replace("%item%", name);
		if (title.length() > 32) {
			title = title.substring(0, 32);
		}

		inv.setTitle(title);
		Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inv.build()));
		plugin.getToVerify().put(player, plugin.getCode() + choosen.name());

		Utils.sendInstructions(player, plugin.getMessage("instructions.gui").replace("%item%", name), true);
	}
}