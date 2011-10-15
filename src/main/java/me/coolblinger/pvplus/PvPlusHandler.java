package me.coolblinger.pvplus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class PvPlusHandler extends Handler {
	@Override
	public void publish(LogRecord record) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(ChatColor.LIGHT_PURPLE + record.getMessage());
		}
	}

	@Override
	public void flush() {

	}

	@Override
	public void close() throws SecurityException {

	}
}
