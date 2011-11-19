package me.coolblinger.pvplus.commands;

import me.coolblinger.pvplus.PvPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OutpostsCommand extends BaseCommand {
	String getPermission() {
		return "pvplus.outposts.manage";
	}

	void command(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length == 0) {
			printCommands(player);
		} else if (args[0].equalsIgnoreCase("create")) {
			create(player, args);
		} else if (args[0].equalsIgnoreCase("set")) {
			set(player, args);
		} else {
			printCommands(player);
		}
	}

	private void printCommands(Player player) {
		player.sendMessage(ChatColor.DARK_GREEN + "====PvPlus====");
		player.sendMessage(ChatColor.GOLD + "/outposts create <name>" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Create a new outpost");
		player.sendMessage(ChatColor.GOLD + "/outposts set <name>" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Set an outpost's bounds");
		player.sendMessage(ChatColor.GOLD + "/outposts remove <name>" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Remove an outpost");
		player.sendMessage(ChatColor.GOLD + "/outposts list" + ChatColor.WHITE + " - " + ChatColor.AQUA + "List all of the outposts");
	}

	private void create(Player player, String[] args) {
		if (args.length >= 2) {
			if (PvPlus.om.createOutpost(args[1])) {
				player.sendMessage(ChatColor.GREEN + "Outpost '" + ChatColor.WHITE + args[1] + ChatColor.GREEN + "' has been created!");
				player.sendMessage(ChatColor.GREEN + "You can set its bounds using " + ChatColor.GOLD + "/outposts set <name>" + ChatColor.GREEN + ".");
			} else {
				player.sendMessage(ChatColor.RED + "An outpost with the specified name already exists.");
			}
		} else {
			player.sendMessage(ChatColor.RED + "You have to specify a name for the new outpost.");
		}
	}

	private void set(Player player, String[] args) {
		if (PvPlus.om.getDefining(player) == null) {
			if (args.length >= 2) {
				int outcome = PvPlus.om.toggleDefining(args[1], player);
				switch (outcome) {
					case 0:
						player.sendMessage(ChatColor.RED + "An outpost with the specified name does not exist.");
						break;
					case 1:
						player.sendMessage(ChatColor.RED + "Somebody's already setting bounds for this outpost.");
						break;
					case 3:
						player.sendMessage(ChatColor.GREEN + "You're now setting " + ChatColor.GOLD + args[1] + ChatColor.GREEN + "'s bounds.");
						player.sendMessage(ChatColor.GREEN + "Left click to set the first corner, and right click to set the second.");
						player.sendMessage(ChatColor.GREEN + "Write " + ChatColor.GOLD + "/outposts set" + ChatColor.GREEN + " again to cancel.");
						break;
				}
			} else {
				player.sendMessage(ChatColor.RED + "You have to specify the name of an outpost.");
			}
		} else {
			PvPlus.om.toggleDefining("herpderp", player);
			player.sendMessage(ChatColor.GREEN + "Setting bounds has been canceled!");
		}
	}
}
