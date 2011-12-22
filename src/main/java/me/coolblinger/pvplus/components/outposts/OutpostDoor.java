package me.coolblinger.pvplus.components.outposts;

import me.coolblinger.pvplus.PvPlus;
import me.coolblinger.pvplus.PvPlusUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;

public class OutpostDoor implements Runnable {
	public boolean isCanceled = false;
	public boolean isSucceeded = false;
	public Location signBlockLocation;
	public Location doorBlockLocation;
	public Player capturer;
	public String outpost;
	public String capturingGroup;
	public String owningGroup;
	public int stage = 10;

	public OutpostDoor(Block sign, Player capturer) {
		signBlockLocation = sign.getLocation();
		outpost = PvPlusUtils.getOutpost(signBlockLocation);
		if (outpost == null) {
			capturer.sendMessage(ChatColor.RED + "This sign is not located in an outpost.");
			isCanceled = true;
			return;
		}
		doorBlockLocation = doorCheck();
		if (doorBlockLocation == null) {
			capturer.sendMessage(ChatColor.RED + "There is no adjacent door.");
			isCanceled = true;
			return;
		}
		this.capturer = capturer;
		capturingGroup = PvPlus.gm.getGroup(capturer.getName());
		if (capturingGroup == null) {
			capturer.sendMessage(ChatColor.RED + "You have to be in a group in order to do this.");
			isCanceled = true;
			return;
		}
		owningGroup = PvPlus.om.getOwner(outpost);
		if (capturingGroup.equals(owningGroup)) {
			capturer.sendMessage(ChatColor.RED + "Your group owns this outpost.");
			isCanceled = true;
			return;
		}
		
		PvPlus.gm.sendMessage(capturingGroup, ChatColor.GREEN + "[PvP] Your group is trying to breach a door in '" + ChatColor.GOLD + outpost + ChatColor.GREEN + "'.");
		if (!owningGroup.equals("///")) {
			PvPlus.gm.sendMessage(owningGroup, ChatColor.RED + "[PvP] " + ChatColor.GRAY + capturingGroup + ChatColor.RED  + " is trying to breach a door in '" + ChatColor.GOLD + outpost + ChatColor.RED + "'.");
		}
		Sign signSign = (Sign) sign.getState();
		signSign.setLine(1, "§4||||||||||");
		signSign.setLine(2, capturer.getName());
		signSign.update();
	}

	public Location doorCheck() {
		Block signBlock = signBlockLocation.getBlock();
		if (signBlock.getData() == 0x2) { //Facing north
			if (signBlock.getRelative(1, -1, 1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(1, -1, 1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(1, -1, 1).getLocation();
			}
			if (signBlock.getRelative(-1, -1, 1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(-1, -1, 1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(-1, -1, 1).getLocation();
			}
		} else if (signBlock.getData() == 0x3) { //Facing south
			if (signBlock.getRelative(1, -1, -1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(1, -1, -1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(1, -1, -1).getLocation();
			}
			if (signBlock.getRelative(-1, -1, -1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(-1, -1, -1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(-1, -1, -1).getLocation();
			}
		} else if (signBlock.getData() == 0x4) { //Facing west
			if (signBlock.getRelative(1, -1, 1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(1, -1, 1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(1, -1, 1).getLocation();
			}
			if (signBlock.getRelative(1, -1, -1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(1, -1, -1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(-1, -1, -1).getLocation();
			}
		} else if (signBlock.getData() == 0x5) { //Facing east
			if (signBlock.getRelative(-1, -1, 1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(-1, -1, 1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(-1, -1, 1).getLocation();
			}
			if (signBlock.getRelative(-1, -1, -1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(-1, -1, -1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(-1, -1, -1).getLocation();
			}
		}
		return null;
	}

	private Block getSignBlock() {
		return signBlockLocation.getBlock();
	}

	public void remove() {
		if (!isCanceled && !isSucceeded) {
			PvPlus.gm.sendMessage(capturingGroup, ChatColor.RED + "[PvP] Your group has failed to breach a door in '" + ChatColor.GOLD + outpost + ChatColor.RED + "'.");
			if (!owningGroup.equals("///")) {
				PvPlus.gm.sendMessage(owningGroup, ChatColor.GREEN + "[PvP] " + ChatColor.GRAY + capturingGroup + ChatColor.RED + " has failed to breach a door in '" + ChatColor.GOLD + outpost + ChatColor.GREEN + "'.");
			}
		}
		Block signBlock = getSignBlock();
		if (signBlock.getState() instanceof Sign) {
			Sign signSign = (Sign) signBlock.getState();
			signSign.setLine(1, "");
			signSign.setLine(2, "");
			signSign.update();
		}
		PvPlus.om.doors.remove(signBlockLocation);
	}

	public void keepOpen() {
		Block signBlock = getSignBlock();
		if (signBlock.getState() instanceof Sign) {
			Sign signSign = (Sign) signBlock.getState();
			signSign.setLine(1, "");
			signSign.setLine(2, "");
			signSign.update();
		}
		Block doorBlock = doorBlockLocation.getBlock();
		if (doorBlock.getType() == Material.WOODEN_DOOR || doorBlock.getType() == Material.IRON_DOOR_BLOCK) {
			Door door = (Door) doorBlock.getState().getData();
			if (!door.isOpen()) {
				door.setOpen(true);
				doorBlock.setData(door.getData());
				doorBlock = doorBlock.getRelative(BlockFace.UP);
				door = (Door) doorBlock.getState().getData();
				door.setOpen(true);
				doorBlock.setData(door.getData());
			}
		} else {
			remove();
		}
	}

	public void unlock() {
		Block doorBlock = doorBlockLocation.getBlock();
		if (doorBlock.getType() == Material.WOODEN_DOOR || doorBlock.getType() == Material.IRON_DOOR_BLOCK) {
			isSucceeded = true;
			keepOpen();
			PvPlus.gm.sendMessage(capturingGroup, ChatColor.GREEN + "[PvP] Your group has successfully breached a door in '" + ChatColor.GOLD + outpost + ChatColor.GREEN + "'.");
			if (!owningGroup.equals("///")) {
				PvPlus.gm.sendMessage(owningGroup, ChatColor.RED + "[PvP] " + ChatColor.GRAY + capturingGroup + ChatColor.RED + " has breached a door in '" + ChatColor.GOLD + outpost + ChatColor.RED + "'.");
			}
		}
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("PvPlus"), new Runnable() {
			public void run() {
				close();
			}
		}, 100);
	}

	public void close() {
		Block doorBlock = doorBlockLocation.getBlock();
		if (doorBlock.getType() == Material.WOODEN_DOOR || doorBlock.getType() == Material.IRON_DOOR_BLOCK) {
			Door door = (Door) doorBlock.getState().getData();
			if (door.isOpen()) {
				door.setOpen(false);
				doorBlock.setData(door.getData());
				doorBlock = doorBlock.getRelative(BlockFace.UP);
				door = (Door) doorBlock.getState().getData();
				door.setOpen(false);
				doorBlock.setData(door.getData());
			}
		} else {
			remove();
		}
		remove();
	}

	public void run() {
		if (isSucceeded) {
			return;
		}
		if (isCanceled) {
			remove();
			return;
		}
		Block signBlock = getSignBlock();
		if (signBlock.getState() instanceof Sign) {
			if (signBlock.getLocation().distance(capturer.getLocation()) > PvPlus.getInt("doors.range")) {
				capturer.sendMessage(ChatColor.RED + "You've moved out of range of the door!");
				remove();
				return;
			}
			if (stage < 1) {
				unlock();
				return;
			}
			stage--;
			String stageString = "§4";
			for (int i = 0; i <= stage; i++) {
				stageString += "|";
			}
			stageString += "§2";
			for (int i = 10 - stage; i >= 0; i--) {
				stageString += "|";
			}
			Sign signSign = (Sign) signBlock.getState();
			signSign.setLine(1, stageString);
			signSign.update();
		} else {
			remove();
		}
	}
}
