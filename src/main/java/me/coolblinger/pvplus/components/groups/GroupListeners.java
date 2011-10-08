package me.coolblinger.pvplus.components.groups;

import me.coolblinger.pvplus.PvPlus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class GroupListeners {
	public static void onPlayerChat(PlayerChatEvent event) {
		String group = PvPlus.gm.getGroup(event.getPlayer().getName());
		if (group != null) {
			event.setFormat("[" + ChatColor.GRAY + group + ChatColor.WHITE + "]" + event.getFormat());
		}
	}

	public static boolean onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) event;
			if (edbe.getEntity() instanceof Player) {
				Player damaged = (Player) edbe.getEntity();
				if (edbe.getDamager() instanceof Player) {
					Player damager = (Player) edbe.getDamager();
					if (PvPlus.gm.getGroup(damaged.getName()).equals(PvPlus.gm.getGroup(damager.getName()))) {
						damager.sendMessage(ChatColor.RED + "You can't hurt people in your own group.");
						return false;
					}
				} else if (edbe.getDamager() instanceof Arrow) {
					if (((Arrow) edbe.getDamager()).getShooter() instanceof Player) {
						Player damager = (Player) ((Arrow) edbe.getDamager()).getShooter();
						if (PvPlus.gm.getGroup(damaged.getName()).equals(PvPlus.gm.getGroup(damager.getName()))) {
							damager.sendMessage(ChatColor.RED + "You can't hurt people in your own group.");
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
