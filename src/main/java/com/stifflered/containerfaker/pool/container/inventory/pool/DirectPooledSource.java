package com.stifflered.containerfaker.pool.container.inventory.pool;

import com.stifflered.containerfaker.pool.PoolType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class DirectPooledSource extends PooledInventorySource {

    private final PoolType poolType;

    public DirectPooledSource(PoolType poolType) {
        this.poolType = poolType;
    }

    @Override
    public @Nullable PoolType getType(Player player, Location location) {
        return this.poolType;
    }

    @Override
    public String toString() {
        return "DirectPooledSource{" +
                "poolType=" + this.poolType +
                '}';
    }
}
