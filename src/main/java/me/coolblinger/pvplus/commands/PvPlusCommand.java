package me.coolblinger.pvplus.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class PvPlusCommand extends BaseCommand {
	@Override
	String getPermission() {
		return "none";
	}

	@Override
	void command(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(ChatColor.DARK_GREEN + "====PvPlus====");
		if (sender.hasPermission("pvplus.groups.join")) {
			sender.sendMessage(ChatColor.GOLD + "/groups" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Manage and join groups");
		}
		if (sender.hasPermission("pvplus.outposts.manage")) {
			sender.sendMessage(ChatColor.GOLD + "/outposts" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Manage and outposts");
		}
	}
}
