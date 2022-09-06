package com.stifflered.containerfaker.pool.container.inventory;

import com.stifflered.containerfaker.ContainerFaker;
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
    private final int amount = ContainerFaker.INSTANCE.getConfig().getInt("expire-time");
    private final ChronoUnit unit = ChronoUnit.valueOf(ContainerFaker.INSTANCE.getConfig().getString("expire-time-unit").toUpperCase());

    public Inventory getInventory(Player player, Location location, InventorySource cacheSource) {
        if (this.isChestMirror(location)) {
            PoolEntry poolEntry = this.entryMap.get(location);

            if (Instant.now().isBefore(poolEntry.time)) {
                InventorySource.debug(player, cacheSource, "Using cached inventory %s %s".formatted(Instant.now(), poolEntry.time));
                return poolEntry.inventory;
            }
        }

        Inventory inventory = cacheSource.get(player, location);
        if (inventory == null) {
            InventorySource.debug(player, cacheSource, "Inventory source has returned null");
            return null;
        }

        Instant instant = Instant.now().plus(this.amount, this.unit);
        this.entryMap.put(location, new PoolEntry(instant, inventory));
        InventorySource.debug(player, cacheSource, "Caching new inventory until %s".formatted(instant));
        return inventory;
    }

    public boolean isChestMirror(Location location) {
        return this.entryMap.containsKey(location);
    }

    public void remove(Location location) {
        this.entryMap.remove(location);
    }

    public void clear() {
        this.entryMap.clear();
    }

    public record PoolEntry(Instant time, Inventory inventory) {
    }

}
