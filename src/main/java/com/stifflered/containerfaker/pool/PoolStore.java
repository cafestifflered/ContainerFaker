package com.stifflered.containerfaker.pool;

import com.stifflered.containerfaker.ContainerFaker;
import com.stifflered.containerfaker.util.BlockLocationIterator;
import com.stifflered.containerfaker.util.ChunkAccessLock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PoolStore {

    public static final PoolStore INSTANCE = new PoolStore();

    private final Map<PoolType, List<Inventory>> poolStorage = new HashMap<>();
    private final Map<Location, PoolType> poolChests = new HashMap<>();

    public PoolStore() {
        for (PoolType type : PoolType.values()) {
            this.poolStorage.put(type, new ArrayList<>());
        }
    }

    public CompletableFuture<Void> loadPool(World world, PoolType type) {
        Location min = type.getMin().toLocation(world);
        Location max = type.getMax().toLocation(world);
        ChunkAccessLock chunkAccessLock = ChunkAccessLock.squared(min, max, min.getWorld());
        return chunkAccessLock.load().thenAccept((v) -> {
            List<Inventory> inventories = new ArrayList<>();
            this.poolStorage.put(type, inventories);
            this.poolChests.values().removeIf((typeToRemove) -> typeToRemove == type);

            for (Location location : new BlockLocationIterator(min, max)) {
                Block block = location.getBlock();
                if (block.getType() == Material.CHEST && block.getState() instanceof Chest chest) {
                    inventories.add(chest.getBlockInventory());
                    this.poolChests.put(chest.getLocation(), type);
                }
            }

            chunkAccessLock.close();
        });
    }

    public boolean isPoolEmpty() {
        return this.poolStorage.isEmpty();
    }

    @Nullable
    public List<Inventory> getPool(PoolType type) {
        return this.poolStorage.get(type);
    }

    public void removePoolChest(Location location) {
        PoolType type = this.poolChests.remove(location);
        if (type != null) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    type.refreshPool(location.getWorld());
                }
            }.runTaskLater(ContainerFaker.INSTANCE, 1);
        }
    }

    public void addPossiblePoolChest(Location location) {
        for (PoolType type : PoolType.values()) {
            if (this.isWithinArea(type.getMin(), type.getMax(), location)) {
                type.refreshPool(location.getWorld());
                break;
            }
        }

    }

    public boolean isPoolChest(Location location) {
        return this.poolChests.containsKey(location);
    }

    public PoolType getPoolFromLocation(Location location) {
        return this.poolChests.get(location);
    }

    private boolean isWithinArea(Vector min, Vector max, Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= min.getX() && x <= max.getX() && y >= min.getY() && y <= max.getY() && z >= min.getZ() && z <= max.getZ();
    }
}
