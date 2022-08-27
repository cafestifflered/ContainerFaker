package com.stifflered.containerfaker.pool.container;

import com.stifflered.containerfaker.pool.PoolType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PoolMaterialInstance implements OpenCallback {

    private final Material material;

    private final OpenCallback[] openCallbacks;
    private final PoolType type;

    public PoolMaterialInstance(PoolType type, String materialIdentifier, ConfigurationSection configurationSection) {
        Material material = Material.getMaterial(materialIdentifier);
        if (material.isItem()) {
            throw new IllegalStateException("Invalid block type: " + materialIdentifier);
        }

        this.type = type;
        this.openCallbacks = new OpenCallback[]{
                new ChestOpenCallback(type, false),
                new CommandConfiguration(configurationSection)
        };
        this.material = material;
    }


    @Override
    public void onOpen(Player player, Location location) {
        for (OpenCallback callback : this.openCallbacks) {
            callback.onOpen(player, location);
        }
    }

    public Material getInstance() {
        return this.material;
    }
}
