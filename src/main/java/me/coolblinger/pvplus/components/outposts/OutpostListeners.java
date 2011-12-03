package me.coolblinger.pvplus.components.outposts;

import me.coolblinger.pvplus.PvPlus;
import me.coolblinger.pvplus.PvPlusUtils;
import me.coolblinger.pvplus.components.outposts.doors.OutpostDoor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class OutpostListeners {
	public static void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		//TODO: Cancel the events

		//Signs
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			if (block.getType() == Material.WALL_SIGN) {
				Sign sign = (Sign) block.getState();
				if (sign.getLine(0).equalsIgnoreCase("[PvPlus Door]")) {
					if (!PvPlus.om.doors.containsKey(block.getLocation())) {
						PvPlus.om.doors.put(block.getLocation(), new OutpostDoor(block, player));
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		//Defining
		String outpost = PvPlus.om.getDefining(player);
		if (outpost != null) {
			if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
				PvPlus.om.outposts.get(outpost).x1 = event.getClickedBlock().getX();
				PvPlus.om.outposts.get(outpost).z1 = event.getClickedBlock().getZ();
				player.sendMessage(ChatColor.GREEN + "Corner one set to " + ChatColor.WHITE + event.getClickedBlock().getX() + "," + event.getClickedBlock().getZ() + ChatColor.GREEN + ".");
				event.setCancelled(true);
				return;
			} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				PvPlus.om.outposts.get(outpost).x2 = event.getClickedBlock().getX();
				PvPlus.om.outposts.get(outpost).z2 = event.getClickedBlock().getZ();
				player.sendMessage(ChatColor.GREEN + "Corner two set to " + ChatColor.WHITE + event.getClickedBlock().getX() + "," + event.getClickedBlock().getZ() + ChatColor.GREEN + ".");
				event.setCancelled(true);
				return;
			}
		}
	}

	public static void onBlockBreak(BlockBreakEvent event) {
		//TODO: Separate check for fire breaking.
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
