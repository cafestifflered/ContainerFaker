package com.stifflered.containerfaker.pool.container.inventory.cache;

import com.stifflered.containerfaker.pool.container.inventory.InventorySource;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

public class InventoryCacheSource implements InventorySource {

    private final InventorySource cacheSource;

    public InventoryCacheSource(InventorySource cacheSource) {
        this.cacheSource = cacheSource;
    }

    @Override
    public @Nullable Inventory get(Player player, Location location) {
        return InventoryCacheStorage.INSTANCE.getInventory(player, location, this.cacheSource);
    }


    @Override
    public String toString() {
        return "InventoryCacheSource{" +
                "cacheSource=" + this.cacheSource +
                '}';
    }
}
