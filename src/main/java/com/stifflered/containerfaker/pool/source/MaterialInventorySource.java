package com.stifflered.containerfaker.pool.source;

import com.stifflered.containerfaker.pool.PoolType;
import com.stifflered.containerfaker.pool.container.callback.OpenCallback;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class MaterialInventorySource implements ContainerSourceRetriever {

    @Override
    public @Nullable OpenCallback getCallback(Player player, Location location) {
        return PoolType.getInstance(location.getBlock().getType());
    }

    @Override
    public String toString() {
        return "MaterialInventorySource";
    }
}
