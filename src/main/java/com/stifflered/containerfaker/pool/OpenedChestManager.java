package com.stifflered.containerfaker.pool;

import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlockInventoryHolder;

import java.util.HashSet;
import java.util.Set;

// Track opening chests so players don't access the same chest
// at once.
public class OpenedChestManager {

    public static final OpenedChestManager INSTANCE = new OpenedChestManager();

    private final Set<Location> openChests = new HashSet<>();

    public boolean isOpen(Location location) {
        return this.openChests.contains(location);
    }

    public void remove(Location location) {
        this.openChests.remove(location);
    }

    public void addOpen(Location location) {
        this.openChests.add(location);
    }

    public void closeChest(InventoryCloseEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof BlockInventoryHolder inventoryHolder) {
            Location location = inventoryHolder.getBlock().getLocation();

            this.openChests.remove(location);
        }
    }

}
