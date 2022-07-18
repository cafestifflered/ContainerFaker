package com.stifflered.containerfaker.pool;

import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.concurrent.CompletableFuture;

public enum PoolType {

    // DONT CHANGE THESE NAMES unless needed!
    JUNK(new Vector(104, 46, -19), new Vector(118, 36, -10)),
    WEAPONS(new Vector(104, 46, -31), new Vector(118, 36, -22)),
    DRINKS_HEALTH(new Vector(122, 36, -22), new Vector(136, 46, -31)),
    ARMOR(new Vector(122, 36, -10), new Vector(136, 46, -19)),
    FOOD(new Vector(122, 36, 2), new Vector(136, 46, -7));

    private final Vector min;
    private final Vector max;

    PoolType(Vector pos1, Vector pos2) {
        this.min = new Vector(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
        this.max = new Vector(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
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
