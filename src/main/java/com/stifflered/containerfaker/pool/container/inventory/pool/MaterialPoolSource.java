package com.stifflered.containerfaker.pool.container.inventory.pool;

import com.stifflered.containerfaker.pool.PoolType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class MaterialPoolSource extends PooledInventorySource {

    @Override
    public @Nullable PoolType getType(Player player, Location location) {
        return PoolType.getType(location.getBlock().getType());
    }

    @Override
    public String toString() {
        return "MaterialInventorySource";
    }
}
