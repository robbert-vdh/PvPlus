package me.coolblinger.pvplus.components.outposts;

import me.coolblinger.pvplus.PvPlus;
import me.coolblinger.pvplus.PvPlusUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class OutpostCore implements Runnable {

	//TODO: Teleporting to core

	public boolean isCanceled = false;
	public boolean isSucceeded = false;
	public Location signBlockLocation;
	public Player capturer;
	public String outpost;
	public String capturingGroup;
	public String owningGroup;
	public int stage = 10;
	public int decreaseIn = 2; //Every run tick will cause this to decrease this by one, and the stage will lower if this is 0.

	public OutpostCore(Block sign, Player capturer) {
		signBlockLocation = sign.getLocation();
		outpost = PvPlusUtils.getOutpost(signBlockLocation);
		if (outpost == null) {
			capturer.sendMessage(ChatColor.RED + "This sign is not located in an outpost.");
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

		PvPlus.gm.sendMessage(capturingGroup, ChatColor.GREEN + "[PvP] Your group is trying to capture '" + ChatColor.GOLD + outpost + ChatColor.GREEN + "'.");
		if (!owningGroup.equals("///")) {
			PvPlus.gm.sendMessage(owningGroup, ChatColor.RED + "[PvP] " + ChatColor.GRAY + capturingGroup + ChatColor.RED + " is trying to capture '" + ChatColor.GOLD + outpost + ChatColor.RED + "'.");
		}
		Sign signSign = (Sign) sign.getState();
		signSign.setLine(1, "§4||||||||||");
		signSign.setLine(2, capturer.getName());
		signSign.update();
	}

	private Block getSignBlock() {
		return signBlockLocation.getBlock();
	}

	public void remove() {
		if (!isCanceled && !isSucceeded) {
			PvPlus.gm.sendMessage(capturingGroup, ChatColor.RED + "[PvP] Your group has failed to capture '" + ChatColor.GOLD + outpost + ChatColor.RED + "'.");
			if (!owningGroup.equals("///")) {
				PvPlus.gm.sendMessage(owningGroup, ChatColor.GREEN + "[PvP] " + ChatColor.GRAY + capturingGroup + ChatColor.GREEN + " has failed to capture '" + ChatColor.GOLD + outpost + ChatColor.GREEN + "'.");
			}
		}
		Block signBlock = getSignBlock();
		if (signBlock.getState() instanceof Sign) {
			Sign signSign = (Sign) signBlock.getState();
			signSign.setLine(1, "");
			signSign.setLine(2, "");
			signSign.update();
		}
		PvPlus.om.cores.remove(signBlockLocation);
	}

	public void capture() {
		isSucceeded = true;
		PvPlus.gm.sendMessage(capturingGroup, ChatColor.GREEN + "[PvP] Your group has successfully captured '" + ChatColor.GOLD + outpost + ChatColor.GREEN + "'.");
		if (!owningGroup.equals("///")) {
			PvPlus.gm.sendMessage(owningGroup, ChatColor.RED + "[PvP] You have lost control of '" + ChatColor.GOLD + outpost + ChatColor.RED + "' to " + ChatColor.GRAY + capturingGroup + ChatColor.RED + ".");
		}
		PvPlus.om.setOwner(outpost, capturingGroup);
		remove();
	}

	public void run() {
		if (isCanceled) {
			remove();
			return;
		}
		Block signBlock = getSignBlock();
		if (signBlock.getState() instanceof Sign) {
			if (signBlock.getLocation().distance(capturer.getLocation()) > PvPlus.getInt("doors.range")) {
				capturer.sendMessage(ChatColor.RED + "You've moved out of range of the core!");
				remove();
				return;
			}
			if (stage < 1) {
				capture();
				return;
			}
			if (decreaseIn > 0) {
				decreaseIn--;
				return;
			} else {
				decreaseIn = 2;
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
