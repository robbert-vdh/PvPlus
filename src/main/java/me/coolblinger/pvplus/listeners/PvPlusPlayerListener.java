package me.coolblinger.pvplus.listeners;

import me.coolblinger.pvplus.components.groups.GroupListeners;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class PvPlusPlayerListener extends PlayerListener {

	public void onPlayerChat(PlayerChatEvent event) {
		GroupListeners.onPlayerChat(event);
	}
}
