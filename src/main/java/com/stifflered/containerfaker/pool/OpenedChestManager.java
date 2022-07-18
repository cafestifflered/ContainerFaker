package com.stifflered.containerfaker.pool;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class OpenedChestManager {

    public static final OpenedChestManager INSTANCE = new OpenedChestManager();

    private final Map<Location, PoolEntry> entryMap = new HashMap<>();
    private final Set<Location> openChests = new HashSet<>();

    public boolean openChest(Player player, Location location, PoolType type) {
        Location blockLocation = location.toBlockLocation();
        if (this.openChests.contains(blockLocation)) {
            return false;
        }

        Inventory inventory = null;
        if (this.entryMap.containsKey(blockLocation)) {
            PoolEntry poolEntry = this.entryMap.get(blockLocation);
            if (Instant.now().isBefore(poolEntry.time)) {
                inventory = poolEntry.inventory;
            }
        }

        if (inventory == null) {
            inventory = PoolStore.INSTANCE.randomFromPool(location, type);
            if (inventory == null) {
                player.sendMessage(Component.text("Oh no! Currently no registered chests for that pool.", NamedTextColor.RED));
                return false; // empty pool
            }

            this.entryMap.put(blockLocation, new PoolEntry(Instant.now().plus(10, ChronoUnit.MINUTES), inventory));
        }

        player.openInventory(inventory);
        this.openChests.add(location);
        return true;
    }

    public void closeChest(InventoryCloseEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof BlockInventoryHolder inventoryHolder) {
            Location location = inventoryHolder.getBlock().getLocation();

            this.openChests.remove(location);
            PoolStore store = PoolStore.INSTANCE;
            if (store.isPoolChest(location)) {
                PoolType type = store.getPoolFromLocation(location);
                type.refreshPool(location.getWorld());
            }
        }
    }

    public boolean isChestMirror(Location location) {
        return this.entryMap.containsKey(location);
    }

    public void removeChest(Location location) {
        this.entryMap.remove(location);
    }

    public Inventory getInventory(Location location) {
        return this.entryMap.get(location).inventory;
    }

    private record PoolEntry(Instant time, Inventory inventory) {
    }

}
