package com.stifflered.containerfaker.pool;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.stifflered.containerfaker.ContainerFaker;
import com.stifflered.containerfaker.pool.container.PoolMaterialInstance;
import com.stifflered.containerfaker.pool.container.inventory.pool.RegionOverridePoolSource;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PoolType {

    private static final Map<String, PoolType> REGISTRY = new HashMap<>();
    private static final Map<Material, PoolMaterialInstance> MATERIAL_POOL_INSTANCE_TYPE_MAP = new HashMap<>();
    private static final Map<Material, PoolType> MATERIAL_POOL_TYPE_MAP = new HashMap<>();

    public static void init(World world) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        ConfigurationSection pools = ContainerFaker.INSTANCE.getConfig().getConfigurationSection("pools");
        for (String key : pools.getKeys(false)) {
            ProtectedRegion region = container.get(BukkitAdapter.adapt(world)).getRegion(key);
            ConfigurationSection pool = pools.getConfigurationSection(key);

            if (region == null) {
                ContainerFaker.INSTANCE.getLogger().warning("Could not find worldguard region %s!".formatted(key));
            } else {
                PoolType type = new PoolType(region);
                REGISTRY.put(key, type);

                for (String materialIdentifier : pool.getKeys(false)) {
                    ConfigurationSection materialConfig = pool.getConfigurationSection(materialIdentifier);
                    Material material = Material.getMaterial(materialIdentifier);
                    if (!material.isBlock()) {
                        throw new IllegalStateException("Invalid block type: " + materialIdentifier);
                    }

                    PoolMaterialInstance materialInstance = new PoolMaterialInstance(type, material, materialConfig);

                    MATERIAL_POOL_INSTANCE_TYPE_MAP.put(materialInstance.getInstance(), materialInstance);
                    MATERIAL_POOL_TYPE_MAP.put(material, type);
                }
            }
        }

        RegionOverridePoolSource.boostrap();
    }


    private final ProtectedRegion region;

    PoolType(ProtectedRegion region) {
        this.region = region;
    }

    public Vector getMin() {
        Vector3 vector3 = this.region.getMinimumPoint().toVector3();
        return new Vector(vector3.getX(), vector3.getY(), vector3.getZ());
    }

    public Vector getMax() {
        Vector3 vector3 = this.region.getMaximumPoint().toVector3();
        return new Vector(vector3.getX(), vector3.getY(), vector3.getZ());
    }

    public String getName() {
        return this.region.getId();
    }

    public CompletableFuture<Void> refreshPool(World world) {
        return PoolStore.INSTANCE.loadPool(world, this);
    }

    public static Collection<PoolType> values() {
        return REGISTRY.values();
    }

    public static Collection<String> keys() {
        return REGISTRY.keySet();
    }

    @Nullable
    public static PoolType get(String key) {
        return REGISTRY.get(key);
    }

    @Nullable
    public static PoolMaterialInstance getInstance(Material material) {
        return MATERIAL_POOL_INSTANCE_TYPE_MAP.get(material);
    }

    public static PoolType getType(Material material) {
        return MATERIAL_POOL_TYPE_MAP.get(material);
    }

    @Override
    public String toString() {
        return this.region.getId();
    }
}
