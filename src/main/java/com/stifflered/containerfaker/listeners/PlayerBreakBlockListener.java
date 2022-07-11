package com.stifflered.containerfaker.listeners;

import com.stifflered.containerfaker.pool.OpenedChestManager;
import com.stifflered.containerfaker.pool.PoolStore;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBreakBlockListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();
        if (PoolStore.INSTANCE.isPoolChest(location)) {
            PoolStore.INSTANCE.removePoolChest(location);
            return;
        }

        if (OpenedChestManager.INSTANCE.isChestMirror(location)) {
            OpenedChestManager.INSTANCE.removeChest(location);
        }
    }
}
