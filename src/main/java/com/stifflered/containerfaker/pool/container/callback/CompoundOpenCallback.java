package com.stifflered.containerfaker.pool.container.callback;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class CompoundOpenCallback implements OpenCallback {

    private final OpenCallback[] openCallbacks;

    public CompoundOpenCallback(OpenCallback... inventorySources) {
        this.openCallbacks = inventorySources;
    }

    @Override
    public void onOpen(Player player, Location location) {
        for (OpenCallback callback : this.openCallbacks) {
            callback.onOpen(player, location);
        }
    }
}
