	package me.lagbug.xprotect.spigot.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.lagbug.xprotect.global.Checker;
import me.lagbug.xprotect.spigot.XProtect;

/**
 * This class is used to handle the things that should be blocked when a player
 * is under the captcha test
 *
 * @version 1.0
 */
public class BlockingListener implements Listener {

	// Getting the instance of the plugin
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	/**
	 * Decides if we shouldn't cancel an event
	 *
	 * @param p   - the player
	 * @param key - the key of the config to search at
	 * @return - if we should return
	 */
	private boolean shouldReturn(Player p, String key) {
		if (p == null) return false;
		if (!plugin.getCaptchasFile().getBoolean("settings.enabled")) return true;
		return !plugin.getCaptchasFile().getBoolean("whileOnCaptcha.block" + key) || new Checker(p).isVerified();
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (shouldReturn(e.getPlayer(), "Movement")) {
			return;
		}

		if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
			e.getPlayer().teleport(e.getFrom().setDirection(e.getTo().getDirection()));
		}
	}		

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (shouldReturn(e.getPlayer(), "Interaction")) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (shouldReturn(e.getPlayer(), "BlockPlacement")) {
			return;
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (shouldReturn(e.getPlayer(), "BlockBreaking")) {
			return;
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (shouldReturn(e.getPlayer(), "ItemDrop")) {
			return;
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		
		if (!(entity instanceof Player) || entity.hasMetadata("NPC")) {
			return;
		}
		
		if (shouldReturn((Player) entity, "Damage")) {
			return;
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Entity entity = e.getEntity();
		
		if (!(entity instanceof Player) || entity.hasMetadata("NPC")) {
			return;
		}
		
		if (shouldReturn((Player) entity, "Damage")) {
			return;
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		Entity entity = e.getWhoClicked();
		
		if (!(entity instanceof Player) || entity.hasMetadata("NPC")) {
			return;
		}
		
		if (shouldReturn((Player) entity, "InventoryMovement")) {
			return;
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		boolean r = false;
		
		for (String s : plugin.getCaptchasFile().getStringList("whitelistedCommands")) {
			if (e.getMessage().replace("/", "").startsWith(s)) {
				r = true;
				break;
			}
		}

		if (r || shouldReturn(e.getPlayer(), "Commands")) {
			return;
		}

		e.setCancelled(true);
	}

}