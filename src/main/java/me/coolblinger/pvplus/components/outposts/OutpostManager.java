package me.coolblinger.pvplus.components.outposts;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OutpostManager {
	public HashMap<String, Outpost> outposts = new HashMap<String, Outpost>();
	public ConcurrentHashMap<Location, OutpostDoor> doors = new ConcurrentHashMap<Location, OutpostDoor>();
	private HashMap<String, Player> defining = new HashMap<String, Player>();

	//TODO: Teleporting to cores?
	//TODO: Info signs

	public OutpostManager() {
		load();
	}

	public Set<String> list() {
		return outposts.keySet();
	}

	public String getOwner (String outpost) {
		if (outposts.containsKey(outpost)) {
			return outposts.get(outpost).owner;
		} else {
			return null;
		}
	}

	public boolean setOwner(String name, String owner) {
		if (outposts.containsKey(name)) {
			outposts.get(name).owner = owner;
			return true;
		} else {
			return false;
		}
	}

	public boolean createOutpost(String name) {
		if (!outposts.containsKey(name)) {
			outposts.put(name, new Outpost());
			outposts.get(name).name = name;
			return true;
		} else {
			return false;
		}
	}

	public boolean removeOutpost(String name) {
		if (outposts.containsKey(name)) {
			outposts.remove(name);
			return true;
		} else {
			return false;
		}
	}

	public String getDefining(Player player) {
		Set<String> keys = defining.keySet();
		for (String key : keys) {
			if (defining.get(key) == player) {
				return key;
			}
		}
		return null;
	}
	
	public int toggleDefining(String outpost, Player player) {
		if (!defining.containsValue(player)) {
			if (outposts.containsKey(outpost)) {
				if (!defining.containsKey(outpost)) {
					defining.put(outpost, player);
					return 3;
				} else {
					return 1;
				}
			} else {
				return 0;
			}
		} else {
			Set<String> keys = defining.keySet();
			for (String key : keys) {
				if (defining.get(key) == player) {
					defining.remove(key);
				}
			}
			return 2;
		}
	}

	public synchronized void runDoors() {
		for (OutpostDoor door : doors.values()) {
			door.run();
		}
	}

	public synchronized void removeDoors() {
		for (OutpostDoor door : doors.values()) {
			door.remove();
		}
	}

	public synchronized void load() {
		File file = new File("plugins" + File.separator + "PvPlus" + File.separator + "outposts.yml");
		if (!file.exists()) {
			return;
		}
		outposts = new HashMap<String, Outpost>();
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		Set<String> keys = config.getKeys(false);
		for (String key : keys) {
			outposts.put(key, new Outpost());
			outposts.get(key).name = key;
			outposts.get(key).owner = config.getString(key + ".owner");
			outposts.get(key).x1 = config.getDouble(key + ".x1", 0);
			outposts.get(key).z1 = config.getDouble(key + ".z1", 0);
			outposts.get(key).x2 = config.getDouble(key + ".x2", 0);
			outposts.get(key).z2 = config.getDouble(key + ".z2", 0);
		}
	}

	public synchronized void save() {
		File file = new File("plugins" + File.separator + "PvPlus" + File.separator + "outposts.yml");
		file.getParentFile().mkdir();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		YamlConfiguration config = new YamlConfiguration();
		Set<String> keys = outposts.keySet();
		for (String key : keys) {
			config.set(key + ".owner", outposts.get(key).owner);
			config.set(key + ".x1", outposts.get(key).x1);
			config.set(key + ".z1", outposts.get(key).z1);
			config.set(key + ".x2", outposts.get(key).x2);
			config.set(key + ".z2", outposts.get(key).z2);
		}
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
