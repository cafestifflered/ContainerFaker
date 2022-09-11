package com.stifflered.containerfaker.util;

public record ChunkPos(int x, int z) {
    public static ChunkPos fromCoordinates(int x, int z) {
        return new ChunkPos(x >> 4, z >> 4);
    }
}
