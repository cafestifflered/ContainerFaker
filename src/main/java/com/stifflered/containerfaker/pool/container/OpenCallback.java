package com.stifflered.containerfaker.pool.container;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface OpenCallback {

    void onOpen(Player player, Location location);
}
