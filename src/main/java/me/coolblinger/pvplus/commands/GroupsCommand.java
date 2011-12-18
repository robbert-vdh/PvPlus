package me.coolblinger.pvplus.commands;

import me.coolblinger.pvplus.PvPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class GroupsCommand extends BaseCommand {
	String getPermission() {
		return "pvplus.groups.join";
	}

	void command(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length == 0) {
			printCommands(player);
		} else if (args[0].equalsIgnoreCase("join")) {
			join(player, args);
		} else if (args[0].equalsIgnoreCase("list")) {
			list(player);
		} else if (args[0].equalsIgnoreCase("leave")) {
			leave(player);
		} else if (args[0].equalsIgnoreCase("teleport")) {
			teleport(player, args);
		} else if (args[0].equalsIgnoreCase("create")) {
			create(player, args);
		} else if (args[0].equalsIgnoreCase("remove")) {
			remove(player, args);
		} else if (args[0].equalsIgnoreCase("setowner")) {
			setOwner(player, args);
		} else {
			printCommands(player);
		}
	}

	private void printCommands(Player player) {
		player.sendMessage(ChatColor.DARK_GREEN + "====PvPlus====");
		player.sendMessage(ChatColor.GOLD + "/groups join <name>" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Join the specified group.");
		player.sendMessage(ChatColor.GOLD + "/groups list" + ChatColor.WHITE + " - " + ChatColor.AQUA + "List all of the groups");
		player.sendMessage(ChatColor.GOLD + "/groups leave" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Leave your current group");
		player.sendMessage(ChatColor.GOLD + "/groups teleport" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Teleport to the core of an outpost your group controls");
		if (player.hasPermission("pvplus.groups.manage")) {
			player.sendMessage(ChatColor.GOLD + "/groups create <name>" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Create a new group");
			player.sendMessage(ChatColor.GOLD + "/groups remove <name>" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Remove a group");
			player.sendMessage(ChatColor.GOLD + "/groups setowner <name> <player>" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Set a group's owner");
		}
	}

	private void join(Player player, String[] args) {
		if (args.length >= 2) {
			int outcome = PvPlus.gm.join(args[1], player.getName());
			switch (outcome) {
				case 0:
					player.sendMessage(ChatColor.RED + "You're already in a group!");
					break;
				case 1:
					player.sendMessage(ChatColor.RED + "The specified group does not exist.");
					break;
				case 2:
					player.sendMessage(ChatColor.GREEN + "You've successfully joined group '" + ChatColor.GRAY + args[1] + ChatColor.GREEN + "'.");
					break;
			}
		} else {
			player.sendMessage(ChatColor.RED + "You need to specify the name of the group you want to join.");
		}
	}

	private void list(Player player) {
		Set<String> groups = PvPlus.gm.list();
		player.sendMessage(ChatColor.DARK_GREEN + "====PvPlus====");
		for (String group : groups) {
			player.sendMessage("- " + ChatColor.AQUA + group);
		}
	}

	private void leave(Player player) {
		if (PvPlus.gm.leave(player.getName())) {
			player.sendMessage(ChatColor.GREEN + "You've successfully left your group.");
		} else {
			player.sendMessage(ChatColor.RED + "You're not in a group!");
		}
	}

	private void teleport(Player player, String[] args) {
		if (args.length >= 2) {
			int outcome = PvPlus.om.teleportToCore(args[1], player);
			switch (outcome) {
				case 0:
					player.sendMessage(ChatColor.RED + "The specified outpost does not exist.");
					break;
				case 1:
					player.sendMessage(ChatColor.RED + "Your group does not control the specified outpost.");
					break;
				case 2:
					player.sendMessage(ChatColor.GREEN + "You've successfully teleported to '" + ChatColor.GRAY + args[1] + ChatColor.GREEN + "'.");
					break;
			}
		} else {
			player.sendMessage(ChatColor.RED + "You need to specify the name of the outpost you'd like to teleport to.");
		}
	}

	private void create(Player player, String[] args) {
		if (player.hasPermission("pvplus.groups.manage")) {
			if (args.length >= 2) {
				if (PvPlus.gm.createGroup(args[1])) {
					player.sendMessage(ChatColor.GREEN + "Group '" + ChatColor.GRAY + args[1] + ChatColor.GREEN + "' has been created!");
					player.sendMessage(ChatColor.GREEN + "You can set its owner using " + ChatColor.GOLD + "/groups setowner <group> <name>" + ChatColor.GREEN + ".");
				} else {
					player.sendMessage(ChatColor.RED + "A group with the specified name already exists.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "You need to specify a name.");
			}
		} else {
			printCommands(player);
		}
	}

	private void setOwner(Player player, String[] args) {
		if (player.hasPermission("pvplus.groups.manage")) {
			if (args.length >= 3) {
				if (PvPlus.gm.setOwner(args[1], args[2])) {
					player.sendMessage(ChatColor.GREEN + "The owner of group '" + ChatColor.GRAY + args[1] + ChatColor.GREEN + "' has been set to '" + ChatColor.WHITE + args[2] + ChatColor.GREEN + "'.");
				} else {
					player.sendMessage(ChatColor.RED + "A group with the specified name does not exist.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "You need to specify the name of the group and the name of the new owner.");
			}
		} else {
			printCommands(player);
		}
	}

	private void remove(Player player, String[] args) {
		if (player.hasPermission("pvplus.groups.manage")) {
			if (args.length >= 2) {
				if (PvPlus.gm.remove(args[1])) {
					player.sendMessage(ChatColor.GREEN + "Group '" + ChatColor.GRAY + args[1] + ChatColor.GREEN + "' has been remove successfully!");
				} else {
					player.sendMessage(ChatColor.RED + "A group with the specified name does not exist.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "You need to specify the name of the group you want to remove.");
			}
		} else {
			printCommands(player);
		}
	}
}
