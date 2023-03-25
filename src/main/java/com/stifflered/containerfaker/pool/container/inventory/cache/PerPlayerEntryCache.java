package com.stifflered.containerfaker.pool.container.inventory.cache;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class PerPlayerEntryCache implements EntryCache {

    private final Map<Player, Map<Location, PoolEntry>> entryMap = new WeakHashMap();

    @Override
    public void clear() {
        this.entryMap.clear();
    }

    @Override
    public void invalidate(Location location) {
        for (Map<Location, PoolEntry> mapEntry : this.entryMap.values()) {
            mapEntry.remove(location);
        }
    }

    @Override
    public PoolEntry getEntry(Location location, Player player) {
        return this.entryMap.getOrDefault(player, Map.of()).get(location);
    }

    @Override
    public void cache(Location location, Player player, PoolEntry poolEntry) {
        this.entryMap.putIfAbsent(player, new HashMap<>());
        this.entryMap.get(player).put(location, poolEntry);
    }
}
