package me.coolblinger.pvplus.components.groups;

import me.coolblinger.pvplus.PvPlus;
import me.coolblinger.pvplus.PvPlusUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class GroupListeners {
	public static void onPlayerChat(PlayerChatEvent event) {
		String group = PvPlus.gm.getGroup(event.getPlayer().getName());
		if (group != null && PvPlus.getBoolean("prefixChatWithGroup")) {
			event.setFormat("[" + ChatColor.GRAY + group + ChatColor.WHITE + "]" + event.getFormat());
		}
		if (event.getMessage().startsWith("@")) {
			String message = event.getMessage().substring(1);
			if (group != null) {
				PvPlus.gm.sendMessage(group, ChatColor.BLUE + event.getPlayer().getDisplayName() + ": " + message);
				PvPlusUtils.log.info("@[" + group + "]" + event.getPlayer().getDisplayName() + ": " + message);
			} else {
				event.getPlayer().sendMessage(ChatColor.RED + "You're not in a group.");
			}
			event.setCancelled(true);
		}
	}

	public static void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) event;
			if (edbe.getEntity() instanceof Player) {
				Player damaged = (Player) edbe.getEntity();
				if (edbe.getDamager() instanceof Player) {
					Player damager = (Player) edbe.getDamager();
					String damagedGroup = PvPlus.gm.getGroup(damaged.getName());
					String damagerGroup = PvPlus.gm.getGroup(damager.getName());
					if (damagedGroup != null && damagerGroup != null) {
						if (damagedGroup.equals(damagerGroup)) {
							damager.sendMessage(ChatColor.RED + "You can't hurt people in your own group.");
							event.setCancelled(true);
						}
					}
				} else if (edbe.getDamager() instanceof Arrow) {
					if (((Arrow) edbe.getDamager()).getShooter() instanceof Player) {
						Player damager = (Player) ((Arrow) edbe.getDamager()).getShooter();
						if (PvPlus.gm.getGroup(damaged.getName()).equals(PvPlus.gm.getGroup(damager.getName()))) {
							damager.sendMessage(ChatColor.RED + "You can't hurt people in your own group.");
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
}
