package com.stifflered.containerfaker.pool;

import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.concurrent.CompletableFuture;

public enum PoolType {

    FOOD(new Vector(106, 36, 5), new Vector(136, 46, 19));


    private final Vector min;
    private final Vector max;

    PoolType(Vector min, Vector max) {
        this.min = min;
        this.max = max;
    }

    public Vector getMin() {
        return min;
    }

    public Vector getMax() {
        return max;
    }

    public CompletableFuture<Void> refreshPool(World world) {
        return PoolStore.INSTANCE.loadPool(world, this);
    }
}
