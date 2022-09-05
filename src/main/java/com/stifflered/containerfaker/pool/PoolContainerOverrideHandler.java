package com.stifflered.containerfaker.pool;

import com.stifflered.containerfaker.ContainerFaker;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class PoolContainerOverrideHandler {

    private static final String KEY = "block_override%s.%s.%s";

    @Nullable
    public static PoolType getOverrideAtLocation(Block block) {
        Chunk chunk = block.getChunk();

        String data = chunk.getPersistentDataContainer().get(getKey(block), PersistentDataType.STRING);
        if (data == null) {
            return null;
        } else {
            return PoolType.get(data.toLowerCase());
        }
    }

    public static boolean removePoolOverride(Block block) {
        Chunk chunk = block.getChunk();
        NamespacedKey key = getKey(block);
        if (chunk.getPersistentDataContainer().has(key)) {
            chunk.getPersistentDataContainer().remove(key);
            return true;
        }

        return false;
    }

    public static void setPoolOverride(Block block, PoolType type) {
        Chunk chunk = block.getChunk();
        chunk.getPersistentDataContainer().set(getKey(block), PersistentDataType.STRING, type.getName());
    }

    private static NamespacedKey getKey(Block block) {
        return new NamespacedKey(ContainerFaker.INSTANCE, KEY.formatted(block.getX(), block.getY(), block.getZ()));
    }
}
