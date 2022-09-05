package com.stifflered.containerfaker.pool.container.callback;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class EmptyOpenCallback implements OpenCallback {

    public static final EmptyOpenCallback INSTANCE = new EmptyOpenCallback();

    @Override
    public void onOpen(Player player, Location location) {
    }
}
