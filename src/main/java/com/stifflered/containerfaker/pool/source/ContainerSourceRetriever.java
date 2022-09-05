package com.stifflered.containerfaker.pool.source;

import com.stifflered.containerfaker.pool.container.callback.OpenCallback;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface ContainerSourceRetriever {

    @Nullable
    OpenCallback getCallback(Player player, Location location);
}
