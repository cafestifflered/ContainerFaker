package com.stifflered.containerfaker.listeners;

import com.stifflered.containerfaker.pool.OpenedChestManager;
import com.stifflered.containerfaker.pool.PoolStore;
import com.stifflered.containerfaker.pool.PoolType;
import com.stifflered.containerfaker.pool.container.inventory.cache.InventoryCacheStorage;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlockInventoryHolder;

public class PlayerCloseInventoryListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        OpenedChestManager.INSTANCE.closeChest(event);

        if (event.getView().getTopInventory().getHolder() instanceof BlockInventoryHolder inventoryHolder) {
            Location location = inventoryHolder.getBlock().getLocation();

            PoolStore store = PoolStore.INSTANCE;
            if (store.isPoolChest(location)) {
                PoolType type = store.getPoolFromLocation(location);
                type.refreshPool();
                InventoryCacheStorage.INSTANCE.clear();
            }
        }

    }
}
