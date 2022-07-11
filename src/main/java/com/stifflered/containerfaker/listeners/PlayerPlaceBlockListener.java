package com.stifflered.containerfaker.listeners;

import com.stifflered.containerfaker.pool.PoolStore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerPlaceBlockListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();
        if (event.getBlock().getType() == Material.CHEST) {
            PoolStore.INSTANCE.addPossiblePoolChest(location);
        }

    }
}
