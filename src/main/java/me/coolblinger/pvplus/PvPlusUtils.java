package me.coolblinger.pvplus;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

public class PvPlusUtils {
	public static Logger log = Logger.getLogger("Minecraft");

	public static void noPermission(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You don't have permission to do this.");
	}

	public static void noPlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You need to be a player in order to do this.");
	}
}
