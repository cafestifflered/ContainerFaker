package com.stifflered.containerfaker.pool.container.inventory;

import com.stifflered.containerfaker.pool.container.callback.OpenCallback;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

public class RunnableInventorySource implements InventorySource {

    private final InventorySource inventorySource;
    private final OpenCallback runnable;

    public RunnableInventorySource(InventorySource inventorySource, OpenCallback runnable) {
        this.inventorySource = inventorySource;
        this.runnable = runnable;
    }

    @Override
    public @Nullable Inventory get(Player player, Location location) {
        this.runnable.onOpen(player, location);
        return this.inventorySource.get(player, location);
    }

    @Override
    public String toString() {
        return "RunnableInventorySource{" +
                "inventorySource=" + this.inventorySource +
                ", runnable=" + this.runnable +
                '}';
    }
}
