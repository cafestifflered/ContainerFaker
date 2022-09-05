package com.stifflered.containerfaker.pool.container.inventory;

import com.stifflered.containerfaker.pool.PoolType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

public interface InventorySource {

    @Nullable
    Inventory get(Player player, Location location);


    static void debug(Player player, InventorySource source) {
        if (player.hasPermission("containerfaker.sourcecheck")) {
            player.sendMessage(Component.text("Fetching inventory from " + source, NamedTextColor.GRAY));
        }
    }

    static void debug(Player player, InventorySource source, PoolType type) {
        if (player.hasPermission("containerfaker.sourcecheck")) {
            player.sendMessage(Component.text("Fetching inventory (pooltype: %s) from %s".formatted(type, source), NamedTextColor.GRAY));
        }
    }
}
