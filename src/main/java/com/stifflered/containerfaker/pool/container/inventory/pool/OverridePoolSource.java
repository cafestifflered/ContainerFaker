package com.stifflered.containerfaker.pool.container.inventory.pool;

import com.stifflered.containerfaker.pool.PoolContainerOverrideHandler;
import com.stifflered.containerfaker.pool.PoolType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class OverridePoolSource extends PooledInventorySource {

    @Override
    public @Nullable PoolType getType(Player player, Location location) {
        return PoolContainerOverrideHandler.getOverrideAtLocation(location.getBlock());
    }

    @Override
    public String toString() {
        return "OverrideInventorySource";
    }

}
