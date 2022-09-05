package com.stifflered.containerfaker.pool.source;

import com.stifflered.containerfaker.pool.container.callback.OpenCallback;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class ContainerSource {

    private static final ContainerSourceRetriever[] CONTAINER_SOURCE_RETRIEVERS = {
            new MaterialInventorySource(),
    };

    @Nullable
    public static OpenCallback getPool(Player player, Location location) {
        for (ContainerSourceRetriever source : CONTAINER_SOURCE_RETRIEVERS) {
            OpenCallback openCallback = source.getCallback(player, location);
            if (openCallback != null) {
                return openCallback;
            }
        }

        return null;
    }
}
