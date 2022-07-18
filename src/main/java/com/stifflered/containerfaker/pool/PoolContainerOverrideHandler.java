package com.stifflered.containerfaker.pool;

import com.stifflered.containerfaker.Main;
import com.stifflered.containerfaker.util.Randoms;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;

public class PoolContainerOverrideHandler {

    private static final String KEY = "block_override%s.%s.%s";

    public static PoolType getPoolType(Block block) {
        Chunk chunk = block.getChunk();

        String data = chunk.getPersistentDataContainer().get(getKey(block), PersistentDataType.STRING);
        if (data == null) {
            return fromMaterial(block.getType());
        } else {
            return PoolType.valueOf(data);
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
        chunk.getPersistentDataContainer().set(getKey(block), PersistentDataType.STRING, type.name());
    }

    private static PoolType fromMaterial(Material material) {
        return switch (material) {
            case BLAST_FURNACE, SMOKER, SMITHING_TABLE -> PoolType.FOOD;
            case WHITE_SHULKER_BOX, LIGHT_BLUE_SHULKER_BOX, LOOM -> PoolType.ARMOR;
            case FLETCHING_TABLE, CRAFTING_TABLE, CHEST -> Randoms.randomIndex(PoolType.values());
            case SHULKER_BOX, BLACK_SHULKER_BOX, BROWN_SHULKER_BOX -> PoolType.DRINKS_HEALTH;
            case BLUE_SHULKER_BOX, YELLOW_SHULKER_BOX, OBSERVER -> PoolType.WEAPONS;
            case DEAD_TUBE_CORAL, DEAD_HORN_CORAL, DEAD_BRAIN_CORAL -> PoolType.JUNK;
            default -> null;
        };
    }

    private static NamespacedKey getKey(Block block) {
        return new NamespacedKey(Main.INSTANCE, KEY.formatted(block.getX(), block.getY(), block.getZ()));
    }
}
