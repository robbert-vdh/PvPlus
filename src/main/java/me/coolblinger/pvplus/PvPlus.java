package me.coolblinger.pvplus;

import me.coolblinger.pvplus.commands.GroupsCommand;
import me.coolblinger.pvplus.commands.OutpostsCommand;
import me.coolblinger.pvplus.commands.PvPlusCommand;
import me.coolblinger.pvplus.components.groups.GroupManager;
import me.coolblinger.pvplus.components.outposts.OutpostManager;
import me.coolblinger.pvplus.listeners.PvPlusBlockListener;
import me.coolblinger.pvplus.listeners.PvPlusEntityListener;
import me.coolblinger.pvplus.listeners.PvPlusPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPlus extends JavaPlugin {
	private int taskId;
	public static GroupManager gm = new GroupManager();
	public static OutpostManager om = new OutpostManager();

	public void onDisable() {
		om.removeDoors();
		om.removeCores();
		getServer().getScheduler().cancelTask(taskId);
		gm.save();
		om.save();
		PvPlusUtils.log.info("PvPlus has been disabled!");
	}

	public void onEnable() {
		PluginDescriptionFile pdf = getDescription();
		PluginManager pm = getServer().getPluginManager();
		if (!pm.isPluginEnabled("Spout")) {
			PvPlusUtils.log.severe("Spout could not be found, PvPlus will disable itself.");
			setEnabled(false);
			return;
		}
		getCommand("pvplus").setExecutor(new PvPlusCommand());
		getCommand("groups").setExecutor(new GroupsCommand());
		getCommand("outposts").setExecutor(new OutpostsCommand());
		pm.registerEvent(Event.Type.BLOCK_BREAK, new PvPlusBlockListener(), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, new PvPlusBlockListener(), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, new PvPlusEntityListener(), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, new PvPlusPlayerListener(), Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, new PvPlusPlayerListener(), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, new PvPlusPlayerListener(), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, new PvPlusPlayerListener(), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.REDSTONE_CHANGE, new PvPlusBlockListener(), Event.Priority.Normal, this);
		initConfig();
		taskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			public void run() {
				om.runDoors();
				om.runCores();
				om.handleMoney();
			}
		}, 0, getInt("tickSpeed"));
		if (Bukkit.getPluginManager().getPlugin("Register") == null) {
			PvPlusUtils.log.warning("PvPlus could not find Register. Economy features have been disabled.");
		}
		PvPlusUtils.log.info("PvPlus version " + pdf.getVersion() + " has been enabled!");
	}

	private void initConfig() {
		YamlConfiguration config = (YamlConfiguration) getConfig();
		config.addDefault("tickSpeed", 40); //This controls the speed of things like door capturing. 20 ticks = 1 second.");
		config.addDefault("moneyPerTick", 0); //This controls the speed of things like door capturing. 20 ticks = 1 second.");
		config.addDefault("moneyOnCapture", 0); //This is the amount of money you'd get when you capture an outpost
		config.addDefault("range", 5); //The maximum distance you're allowed to be from a sign while capturing.");
		config.options().copyDefaults(true);
		saveConfig();
	}

	public static int getInt(String path) {
		YamlConfiguration config = (YamlConfiguration) Bukkit.getPluginManager().getPlugin("PvPlus").getConfig();
		return config.getInt(path);
	}

	public static double getDouble(String path) {
		YamlConfiguration config = (YamlConfiguration) Bukkit.getPluginManager().getPlugin("PvPlus").getConfig();
		return config.getDouble(path);
	}
}
