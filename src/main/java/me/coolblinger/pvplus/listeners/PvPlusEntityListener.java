package me.coolblinger.pvplus.listeners;

import me.coolblinger.pvplus.components.groups.GroupListeners;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class PvPlusEntityListener extends EntityListener {
	public void onEntityDamage(EntityDamageEvent event) {
		GroupListeners.onEntityDamage(event);
	}
}
