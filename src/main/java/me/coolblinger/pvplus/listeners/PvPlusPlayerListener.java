package me.coolblinger.pvplus.listeners;

import me.coolblinger.pvplus.components.groups.GroupListeners;
import me.coolblinger.pvplus.components.outposts.OutpostListeners;
import org.bukkit.event.player.*;

public class PvPlusPlayerListener extends PlayerListener {

	public void onPlayerChat(PlayerChatEvent event) {
		GroupListeners.onPlayerChat(event);
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		OutpostListeners.onPlayerInteract(event);
	}

	public void onPlayerMove(PlayerMoveEvent event) {
		OutpostListeners.onPlayerMove(event);
	}

	public void onPlayerQuit (PlayerQuitEvent event) {
		OutpostListeners.onPlayerQuit(event);
	}
}
