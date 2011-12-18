package me.coolblinger.pvplus;

import me.coolblinger.pvplus.components.outposts.Outpost;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PvPlusUtils {
	public static Logger log = Logger.getLogger("Minecraft");

	public static void noPermission(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You don't have permission to do this.");
	}

	public static void noPlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You need to be a player in order to do this.");
	}

	public static Double clamp(Double value, Double compare1, Double compare2) {
		double min;
		double max;
		if (compare2 < compare1) {
			max = compare1;
			min = compare2;
		} else {
			max = compare2;
			min = compare1;
		}
		if (value.compareTo(max) > 0) {
			return max;
		} else if (value.compareTo(min) < 0) {
			return min;
		} else {
			return value;
		}
	}

	public static String getOutpost(Location loc) {
		List<Outpost> outposts = new ArrayList<Outpost>(PvPlus.om.outposts.values());
		for (Outpost outpost : outposts) {
			if (outpost.world.equals(loc.getWorld().getName())) {
				if (clamp(loc.getX(), outpost.x1, outpost.x2) == loc.getX() && clamp(loc.getZ(), outpost.z1, outpost.z2) == loc.getZ()) {
					return outpost.name;
				}
			}
		}
		return null;
	}
}
