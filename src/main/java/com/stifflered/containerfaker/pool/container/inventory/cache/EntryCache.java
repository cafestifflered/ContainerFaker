package com.stifflered.containerfaker.pool.container.inventory.cache;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public interface EntryCache {

    void clear();

    void invalidate(Location location);

    PoolEntry getEntry(Location location, Player player);

    default boolean isCached(Location location, Player player) {
        return this.getEntry(location, player) != null;
    }

    void cache(Location location, Player player, PoolEntry poolEntry);

    record PoolEntry(@Nullable Instant time, Inventory inventory) {
    }
}
