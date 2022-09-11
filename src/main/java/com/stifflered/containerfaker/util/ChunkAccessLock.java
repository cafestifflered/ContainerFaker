package com.stifflered.containerfaker.util;

import com.stifflered.containerfaker.ContainerFaker;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChunkAccessLock implements Closeable {

    private final World world;
    private final List<ChunkPos> lockedChunkPositions;

    private boolean closed = false;
    private final List<Chunk> loadedChunks = new ArrayList<>();

    public static ChunkAccessLock squared(Location min, Location max, World world) {
        List<ChunkPos> positions = new ArrayList<>();
        int minX = Math.min(min.getBlockX(), max.getBlockX());
        int maxX = Math.max(min.getBlockX(), max.getBlockX());
        int minZ = Math.min(min.getBlockZ(), max.getBlockZ());
        int maxZ = Math.max(min.getBlockZ(), max.getBlockZ());

        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                positions.add(ChunkPos.fromCoordinates(x, z));
            }
        }
        return new ChunkAccessLock(world, positions);
    }

    ChunkAccessLock(World world, List<ChunkPos> pos) {
        this.world = world;
        this.lockedChunkPositions = pos;
    }

    public CompletableFuture<Void> load() {
        if (this.closed) {
            throw new IllegalStateException("Cannot load closed chunk access!");
        }

        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for (ChunkPos pos : this.lockedChunkPositions) {
            int x = pos.x();
            int z = pos.z();

            completableFutures.add(
                    this.world.getChunkAtAsync(x, z).thenAccept((chunk) -> {
                        if (this.closed) {
                            return;
                        }

                        this.loadedChunks.add(chunk);
                        chunk.addPluginChunkTicket(ContainerFaker.INSTANCE);
                    })
            );
        }

        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
    }


    @Override
    public void close() {
        this.closed = true;
        for (Chunk chunk : this.loadedChunks) {
            chunk.removePluginChunkTicket(ContainerFaker.INSTANCE);
        }
        this.loadedChunks.clear();
    }
}
