package com.stifflered.containerfaker.pool;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Set;

public class RegionAccessStorage {

    private final Set<String> deniedRegions = new HashSet<>();
    private final Set<String> allowedRegions = new HashSet<>();

    public RegionAccessStorage(ConfigurationSection configurationSection) {
        this.deniedRegions.addAll(configurationSection.getStringList("active-regions"));
        this.allowedRegions.addAll(configurationSection.getStringList("inactive-regions"));
    }

    public boolean hasNoPermission(Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        ApplicableRegionSet regions = container.get(BukkitAdapter.adapt(location.getWorld())).getApplicableRegions(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

        for (ProtectedRegion protectedRegion : regions) {
            if (this.deniedRegions.contains(protectedRegion.getId())) {
                return false;
            }
        }

        for (ProtectedRegion protectedRegion : regions) {
            if (this.allowedRegions.contains(protectedRegion.getId())) {
                return true;
            }
        }

        return false;
    }
}
