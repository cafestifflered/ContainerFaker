package com.stifflered.containerfaker.pool.container.inventory;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class InventoryCacheStorage {

    public static final InventoryCacheStorage INSTANCE = new InventoryCacheStorage();

    private final Map<Location, PoolEntry> entryMap = new HashMap<>();

    public boolean isChestMirror(Location location) {
        return this.entryMap.containsKey(location);
    }

    public void remove(Location location) {
        this.entryMap.remove(location);
    }

    public Inventory getInventory(Player player, Location location, InventorySource cacheSource) {
        if (InventoryCacheStorage.INSTANCE.isChestMirror(location)) {
            PoolEntry poolEntry = this.entryMap.get(location);

            if (Instant.now().isBefore(poolEntry.time)) {
                return poolEntry.inventory;
            }
        }

        Inventory inventory = cacheSource.get(player, location);
        if (inventory == null) {
            return null;
        }

        this.entryMap.put(location, new PoolEntry(Instant.now().plus(10, ChronoUnit.MINUTES), inventory));
        return inventory;
    }

    public void clear() {
        this.entryMap.clear();
    }

    public record PoolEntry(Instant time, Inventory inventory) {
    }

}
