package com.stifflered.containerfaker.pool.container.inventory.cache;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GlobalEntryCache implements EntryCache {

    private final Map<Location, PoolEntry> entryMap = new HashMap<>();

    @Override
    public void clear() {
        this.entryMap.clear();
    }

    @Override
    public void invalidate(Location location) {
        this.entryMap.remove(location);
    }

    @Override
    public PoolEntry getEntry(Location location, Player player) {
        return this.entryMap.get(location);
    }

    @Override
    public void cache(Location location, Player player, PoolEntry poolEntry) {
        this.entryMap.put(location, poolEntry);
    }

}
