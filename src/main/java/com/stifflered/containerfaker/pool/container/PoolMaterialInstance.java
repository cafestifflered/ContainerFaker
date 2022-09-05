package com.stifflered.containerfaker.pool.container;

import com.stifflered.containerfaker.pool.PoolType;
import com.stifflered.containerfaker.pool.container.callback.*;
import com.stifflered.containerfaker.pool.container.inventory.CompoundInventorySource;
import com.stifflered.containerfaker.pool.container.inventory.InventoryCacheSource;
import com.stifflered.containerfaker.pool.container.inventory.RunnableInventorySource;
import com.stifflered.containerfaker.pool.container.inventory.pool.DirectPooledSource;
import com.stifflered.containerfaker.pool.container.inventory.pool.OverridePoolSource;
import com.stifflered.containerfaker.pool.container.inventory.pool.RegionOverridePoolSource;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PoolMaterialInstance implements OpenCallback {

    private final Material material;

    private final OpenCallback callback;

    public PoolMaterialInstance(PoolType type, Material material, ConfigurationSection configurationSection) {
        this.callback = new ChestOpenCallback(
                new RunnableInventorySource(
                        new InventoryCacheSource(new CompoundInventorySource(
                                new OverridePoolSource(),
                                new RegionOverridePoolSource(),
                                new DirectPooledSource(type)
                        )),
                        new CompoundOpenCallback(
                                new CommandConfiguration(configurationSection)
                        )
                )
        );
        this.material = material;
    }


    @Override
    public void onOpen(Player player, Location location) {
        this.callback.onOpen(player, location);
    }

    public Material getInstance() {
        return this.material;
    }
}
