package me.coolblinger.pvplus.listeners;

import me.coolblinger.pvplus.components.groups.GroupListeners;
import me.coolblinger.pvplus.components.outposts.OutpostListeners;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class PvPlusPlayerListener extends PlayerListener {

	public void onPlayerChat(PlayerChatEvent event) {
		GroupListeners.onPlayerChat(event);
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		OutpostListeners.onPlayerInteract(event);
	}
}
