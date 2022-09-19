package me.lagbug.xprotect.spigot.events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import me.lagbug.xprotect.global.ProtectionValues;
import me.lagbug.xprotect.global.ProtectionValues.LockState;
import me.lagbug.xprotect.global.antibot.checks.BotBlocker;
import me.lagbug.xprotect.global.enums.KickReason;
import me.lagbug.xprotect.spigot.XProtect;
import me.lagbug.xprotect.spigot.utils.DataValues;

/**
 * Used to deal with the handshake packet in order to prevent bot attacks
 *
 * @version 1.0
 */
public class PacketHandshake {

	// Getting the instance of the main class
	private final XProtect plugin = XProtect.getPlugin(XProtect.class);

	// The listener
	private static PacketAdapter packetAdapter = null;

	// Listening to the packet
	public PacketHandshake() {
		packetAdapter = new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Handshake.Client.SET_PROTOCOL) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
				// If only the LockState is not open
				if (ProtectionValues.LOCK_STATE != LockState.OPEN) {

					KickReason reason = BotBlocker.getResult(null, null, e.getPlayer().getAddress().getAddress());
					// If the player is not blocked we return here
					if (reason == KickReason.NOT_BLOCKED) {
						return;
					}

					// Otherwise we cancel the event
					e.setCancelled(true);
					// Add one to the total blocked players
					DataValues.BLOCKED_BOTS++;
				}
			}
		};

		ProtocolLibrary.getProtocolManager().addPacketListener(packetAdapter);
	}

	// Unregister the listener from ProtocolLib
	public static void unregister() {
		ProtocolLibrary.getProtocolManager().removePacketListener(packetAdapter);
	}

}