package com.stifflered.containerfaker.pool;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.stifflered.containerfaker.ContainerFaker;
import com.stifflered.containerfaker.pool.container.PoolMaterialInstance;
import com.stifflered.containerfaker.pool.container.inventory.pool.RegionOverridePoolSource;
import com.stifflered.containerfaker.pool.fill.FillStrategy;
import com.stifflered.containerfaker.pool.fill.FillType;
import com.stifflered.containerfaker.util.Randoms;
import org.bukkit.Bukkit;
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

    public static void init() {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        ConfigurationSection pools = ContainerFaker.INSTANCE.getConfig().getConfigurationSection("pools");
        for (String key : pools.getKeys(false)) {
            ProtectedRegion region = null;
            World selectedWorld = null;
            for (World activeWorld : Bukkit.getWorlds()) {
                region = container.get(BukkitAdapter.adapt(activeWorld)).getRegion(key);
                if (region != null) {
                    selectedWorld = activeWorld;
                    break;
                }
            }
            ConfigurationSection pool = pools.getConfigurationSection(key);

            if (region == null) {
                ContainerFaker.INSTANCE.getLogger().warning("Could not find worldguard region %s!".formatted(key));
            } else {
                PoolType type = new PoolType(selectedWorld, region, pool);
                REGISTRY.put(key, type);

                ConfigurationSection blocks = pool.getConfigurationSection("blocks");
                for (String materialIdentifier : blocks.getKeys(false)) {
                    ConfigurationSection materialConfig = blocks.getConfigurationSection(materialIdentifier);
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

    private final World world;
    private final ProtectedRegion region;
    private final int min;
    private final int max;
    private final FillStrategy strategy;

    PoolType(World world, ProtectedRegion region, ConfigurationSection section) {
        this.world = world;
        this.region = region;
        this.min = section.getInt("min-picked-slots");
        this.max = section.getInt("max-picked-slots");
        this.strategy = FillType.valueOf(section.getString("fill-strategy", FillType.SLOTTED_RANDOM.name())).getStrategy();
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

    public ProtectedRegion getRegion() {
        return this.region;
    }

    public World getWorld() {
        return this.world;
    }

    public CompletableFuture<Void> refreshPool() {
        return PoolStore.INSTANCE.loadPool(this);
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

    public int getRandomCount() {
        return Randoms.randomNumber(this.min, this.max);
    }

    public FillStrategy getStrategy() {
        return this.strategy;
    }
}
