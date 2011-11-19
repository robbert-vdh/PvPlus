package me.coolblinger.pvplus.components.outposts;

import me.coolblinger.pvplus.PvPlus;
import me.coolblinger.pvplus.PvPlusUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class OutpostListeners {
	public static void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String outpost = PvPlus.om.getDefining(player);
		if (outpost != null) {
			if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
				PvPlus.om.outposts.get(outpost).x1 = event.getClickedBlock().getX();
				PvPlus.om.outposts.get(outpost).z1 = event.getClickedBlock().getZ();
				player.sendMessage(ChatColor.GREEN + "Corner one set to " + ChatColor.WHITE + event.getClickedBlock().getX() + "," + event.getClickedBlock().getZ() + ChatColor.GREEN + ".");
			} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				PvPlus.om.outposts.get(outpost).x2 = event.getClickedBlock().getX();
				PvPlus.om.outposts.get(outpost).z2 = event.getClickedBlock().getZ();
				player.sendMessage(ChatColor.GREEN + "Corner two set to " + ChatColor.WHITE + event.getClickedBlock().getX() + "," + event.getClickedBlock().getZ() + ChatColor.GREEN + ".");
			}
		}
	}

	public static void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("pvplus.outposts.manage")) {
			if (PvPlusUtils.getOutpost(event.getBlock().getLocation().toVector()) != null) {
				event.setCancelled(true);
			}
		}
	}

	public static void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("pvplus.outposts.manage")) {
			if (PvPlusUtils.getOutpost(event.getBlock().getLocation().toVector()) != null) {
				event.setCancelled(true);
			}
		}
	}
}
