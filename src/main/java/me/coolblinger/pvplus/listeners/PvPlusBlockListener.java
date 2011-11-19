package me.coolblinger.pvplus.listeners;

import me.coolblinger.pvplus.components.outposts.OutpostListeners;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PvPlusBlockListener extends BlockListener {
	public void onBlockBreak(BlockBreakEvent event) {
		OutpostListeners.onBlockBreak(event);
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		OutpostListeners.onBlockPlace(event);
	}
}
