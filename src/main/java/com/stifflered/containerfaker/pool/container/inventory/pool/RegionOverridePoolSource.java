package com.stifflered.containerfaker.pool.container.inventory.pool;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.stifflered.containerfaker.ContainerFaker;
import com.stifflered.containerfaker.pool.PoolType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RegionOverridePoolSource extends PooledInventorySource {

    public static final RegionOverridePoolSource INSTANCE = new RegionOverridePoolSource();

    private static final Map<String, Map<Material, PoolType>> POOL_OVERRIDES = new HashMap<>();

    public static void boostrap() {
        ConfigurationSection overrides = ContainerFaker.INSTANCE.getConfig().getConfigurationSection("region-overrides");
        if (overrides == null) {
            return;
        }

        for (String region : overrides.getKeys(false)) {
            Map<Material, PoolType> poolMaterialInstanceMap = new HashMap<>();
            ConfigurationSection regionConfig = overrides.getConfigurationSection(region);
            for (String materialName : regionConfig.getKeys(true)) {
                Material material = Material.getMaterial(materialName);
                PoolType type = PoolType.get(regionConfig.getString(materialName));

                poolMaterialInstanceMap.put(material, type);
            }

            POOL_OVERRIDES.put(region, poolMaterialInstanceMap);
        }
    }


    @Override
    public @Nullable PoolType getType(Player player, Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        BlockVector3 blockVector3 = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        ApplicableRegionSet region = container.get(BukkitAdapter.adapt(location.getWorld())).getApplicableRegions(blockVector3);

        for (ProtectedRegion protectedRegion : region.getRegions()) {
            Map<Material, PoolType> override = POOL_OVERRIDES.get(protectedRegion.getId());
            if (override == null) {
                continue;
            }

            return override.get(location.getBlock().getType());
        }

        return null;
    }

    @Override
    public String toString() {
        return "ConfiguredRegionInventorySource{" +
                "pool_override_count=" + this.POOL_OVERRIDES.size() +
                '}';
    }

}
