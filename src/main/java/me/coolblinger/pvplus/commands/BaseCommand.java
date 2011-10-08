package me.coolblinger.pvplus.commands;

import me.coolblinger.pvplus.PvPlusUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand implements CommandExecutor {
	abstract String getPermission();

	public final boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
		if (sender.hasPermission(getPermission()) || getPermission().equals("none")) {
			if (sender instanceof Player) {
				command(sender, command, s, strings);
			} else {
				PvPlusUtils.noPlayer(sender);
			}
		} else {
			PvPlusUtils.noPermission(sender);
		}
		return true;
	}

	abstract void command(CommandSender sender, Command command, String label, String[] args);
}
