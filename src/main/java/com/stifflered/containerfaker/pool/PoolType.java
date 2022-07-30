package com.stifflered.containerfaker.pool;

import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.concurrent.CompletableFuture;

public enum PoolType {

    // DONT CHANGE THESE NAMES unless needed!
    JUNK(new Vector(100004, 26, 99994), new Vector(99990, 16, 99985)),
    WEAPONS(new Vector(99990, 26, 99973), new Vector(100004, 16, 99982)),
    DRINKS_HEALTH(new Vector(100008, 16, 99982), new Vector(100022, 26, 99973)),
    ARMOR(new Vector(100008, 26, 99985), new Vector(100002, 16, 99994)),
    FOOD(new Vector(100008, 26, 99997), new Vector(100022, 16, 100006)),
    RANDOM(new Vector(99992, 26, 100009), new Vector(100022, 16, 100023)),
    EPIC(new Vector(99999, -14, 99995), new Vector(100014, 5, 100006));

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
