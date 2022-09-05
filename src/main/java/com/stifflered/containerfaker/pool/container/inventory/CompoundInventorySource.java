package com.stifflered.containerfaker.pool.container.inventory;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

// Gets the first not null inventory source
public class CompoundInventorySource implements InventorySource {

    private final InventorySource[] sources;

    public CompoundInventorySource(InventorySource... sources) {
        this.sources = sources;
    }

    @Override
    public @Nullable Inventory get(Player player, Location location) {
        for (InventorySource source : this.sources) {
            Inventory inventory = source.get(player, location);
            if (inventory != null) {
                return inventory;
            }
        }

        return null;
    }
}
