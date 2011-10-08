package me.coolblinger.pvplus;

import me.coolblinger.pvplus.commands.GroupsCommand;
import me.coolblinger.pvplus.commands.PvPlusCommand;
import me.coolblinger.pvplus.components.groups.GroupManager;
import me.coolblinger.pvplus.listeners.PvPlusEntityListener;
import me.coolblinger.pvplus.listeners.PvPlusPlayerListener;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPlus extends JavaPlugin {
	public static GroupManager gm = new GroupManager();

	public void onDisable() {
		gm.save();
		PvPlusUtils.log.info("PvPlus has been disabled!");
	}

	public void onEnable() {
		PluginDescriptionFile pdf = getDescription();
		PluginManager pm = getServer().getPluginManager();
		getCommand("pvplus").setExecutor(new PvPlusCommand());
		getCommand("groups").setExecutor(new GroupsCommand());
		pm.registerEvent(Event.Type.PLAYER_CHAT, new PvPlusPlayerListener(), Event.Priority.Highest, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, new PvPlusEntityListener(), Event.Priority.Normal, this);
		PvPlusUtils.log.info("PvPlus version " + pdf.getVersion() + " has been enabled!");
	}
}
