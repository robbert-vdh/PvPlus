package me.coolblinger.pvplus.components.outposts.doors;

import me.coolblinger.pvplus.PvPlus;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.block.SpoutBlock;

public class OutpostDoor implements Runnable {
	private boolean isCanceled = false;
	public Location signBlockLocation;
	public Location doorBlockLocation;
	public Player capturer;
	public int stage = 10;

	public OutpostDoor(Block sign, Player capturer) {
		signBlockLocation = sign.getLocation();
		doorBlockLocation = doorCheck();
		if (doorBlockLocation == null) {
			capturer.sendMessage(ChatColor.RED + "There is no adjacent door.");
			isCanceled = true;
			return;
		}
		Sign signSign = (Sign) sign.getState();
		signSign.setLine(1, "§4||||||||||");
		signSign.setLine(2, capturer.getDisplayName());
		signSign.update();
	}

	public Location doorCheck() {
		Block signBlock = signBlockLocation.getBlock();
		if (signBlock.getData() == 0x2) { //Facing north
			if (signBlock.getRelative(1, -1, 1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(1, -1, 1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(1, -1, 1).getLocation();
			}
			if (signBlock.getRelative(1, -1, -1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(1, -1, -1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(1, -1, -1).getLocation();
			}
		} else if (signBlock.getData() == 0x3) { //Facing south
			if (signBlock.getRelative(-1, -1, 1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(-1, -1, 1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(-1, -1, 1).getLocation();
			}
			if (signBlock.getRelative(-1, -1, -1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(-1, -1, -1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(-1, -1, -1).getLocation();
			}
		} else if (signBlock.getData() == 0x4) { //Facing west
			if (signBlock.getRelative(1, -1, 1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(1, -1, 1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(1, -1, 1).getLocation();
			}
			if (signBlock.getRelative(-1, -1, 1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(-1, -1, 1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(-1, -1, 1).getLocation();
			}
		} else if (signBlock.getData() == 0x5) { //Facing east
			if (signBlock.getRelative(1, -1, -1).getType() == Material.WOODEN_DOOR || signBlock.getRelative(1, -1, -1).getType() == Material.IRON_DOOR_BLOCK) {
				return signBlock.getRelative(1, -1, -1).getLocation();
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
		Block signBlock = getSignBlock();
		if (signBlock.getState() instanceof Sign) {
			Sign signSign = (Sign) signBlock.getState();
			signSign.setLine(1, "");
			signSign.setLine(2, "");
			signSign.update();
		}
		PvPlus.om.doors.remove(signBlockLocation);
	}

	public void unlock() {
		Block doorBlock = doorBlockLocation.getBlock();
		if (doorBlock.getType() == Material.WOODEN_DOOR || doorBlock.getType() == Material.IRON_DOOR_BLOCK) {
			SpoutBlock door = (SpoutBlock) getSignBlock();
			//door.setBlock;
		}
		remove();
	}

	public void run() {
		if (isCanceled) {
			remove();
			return;
		}
		//TODO: Customizable range
		//TODO: Customizable speed
		Block signBlock = getSignBlock();
		if (stage < 1) {
			unlock();
			return;
		}
		if (signBlock.getState() instanceof Sign) {
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
