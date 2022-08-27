package com.stifflered.containerfaker.pool;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Supplier;

public class OpenedChestManager {

    public static final OpenedChestManager INSTANCE = new OpenedChestManager();

    private final Map<Location, PoolEntry> entryMap = new HashMap<>();
    private final Set<Location> openChests = new HashSet<>();

    public boolean isOpen(Location location) {
        return this.openChests.contains(location);
    }

    public void addOpen(Location location) {
        this.openChests.add(location);
    }

    public Inventory getValidStoredInventoryOrCreate(Location location, Supplier<Inventory> onNew) {
        if (this.entryMap.containsKey(location)) {
            OpenedChestManager.PoolEntry poolEntry = this.entryMap.get(location);

            if (Instant.now().isBefore(poolEntry.time)) {
                return poolEntry.inventory;
            }
        }

        Inventory inventory = onNew.get();
        this.entryMap.put(location, new OpenedChestManager.PoolEntry(Instant.now().plus(10, ChronoUnit.MINUTES), inventory));

        return inventory;
    }

    public void closeChest(InventoryCloseEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof BlockInventoryHolder inventoryHolder) {
            Location location = inventoryHolder.getBlock().getLocation();

            this.openChests.remove(location);
            PoolStore store = PoolStore.INSTANCE;
            if (store.isPoolChest(location)) {
                PoolType type = store.getPoolFromLocation(location);
                type.refreshPool(location.getWorld());
            }
        }
    }

    public boolean isChestMirror(Location location) {
        return this.entryMap.containsKey(location);
    }

    public void removeChest(Location location) {
        this.entryMap.remove(location);
    }

    public Inventory getInventory(Location location) {
        return this.entryMap.get(location).inventory;
    }

    private record PoolEntry(Instant time, Inventory inventory) {
    }

}
