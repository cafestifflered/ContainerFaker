package com.stifflered.containerfaker.pool.container.inventory.cache;

import com.stifflered.containerfaker.ContainerFaker;
import com.stifflered.containerfaker.pool.container.inventory.InventorySource;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class InventoryCacheStorage {

    public static final InventoryCacheStorage INSTANCE = new InventoryCacheStorage();

    private final EntryCache entryCache;
    private final int amount = ContainerFaker.INSTANCE.getConfig().getInt("expire-time");
    private final ChronoUnit unit = ChronoUnit.valueOf(ContainerFaker.INSTANCE.getConfig().getString("expire-time-unit").toUpperCase());

    public InventoryCacheStorage() {
        boolean perPlayerPools = ContainerFaker.INSTANCE.getConfig().getBoolean("per-player-pools");
        this.entryCache = perPlayerPools ? new PerPlayerEntryCache() : new GlobalEntryCache();
    }

    public Inventory getInventory(Player player, Location location, InventorySource cacheSource) {
        if (this.isChestMirror(location, player)) {
            GlobalEntryCache.PoolEntry poolEntry = this.entryCache.getEntry(location, player);

            if (poolEntry.time() == null || Instant.now().isBefore(poolEntry.time())) {
                InventorySource.debug(player, cacheSource, "Using cached inventory %s %s".formatted(Instant.now(), poolEntry.time()));
                return poolEntry.inventory();
            }
        }

        Inventory inventory = cacheSource.get(player, location);
        if (inventory == null) {
            InventorySource.debug(player, cacheSource, "Inventory source has returned null");
            return null;
        }

        Instant instant;
        if (this.amount == -1) {
            instant = null;
        } else {
            instant = Instant.now().plus(this.amount, this.unit);
        }

        this.entryCache.cache(location, player, new GlobalEntryCache.PoolEntry(instant, inventory));
        InventorySource.debug(player, cacheSource, "Caching new inventory until %s".formatted(instant));
        return inventory;
    }

    public boolean isChestMirror(Location location, Player player) {
        return this.entryCache.isCached(location, player);
    }

    public void remove(Location location) {
        this.entryCache.invalidate(location);
    }

    public void clear() {
        this.entryCache.clear();
    }


}
