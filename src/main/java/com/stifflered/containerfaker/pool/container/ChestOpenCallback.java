package com.stifflered.containerfaker.pool.container;

import com.stifflered.containerfaker.pool.OpenedChestManager;
import com.stifflered.containerfaker.pool.PoolStore;
import com.stifflered.containerfaker.pool.PoolType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ChestOpenCallback implements OpenCallback {

    private final OpenCallback[] openCallbacks;

    private final PoolType poolType;
    private final boolean isOverride;

    public ChestOpenCallback(PoolType poolType, boolean isOverride, OpenCallback... openCallbacks) {
        this.openCallbacks = openCallbacks;
        this.poolType = poolType;
        this.isOverride = isOverride;
    }

    @Override
    public void onOpen(Player player, Location location) {
        Location blockLocation = location.toBlockLocation();
        if (OpenedChestManager.INSTANCE.isOpen(blockLocation)) {
            return;
        }

        Inventory inventory = OpenedChestManager.INSTANCE.getValidStoredInventoryOrCreate(location, () -> PoolStore.INSTANCE.randomFromPool(location, poolType));

        for (OpenCallback callback : openCallbacks) {
            callback.onOpen(player, location);
        }

        if (player.hasPermission("containerfaker.sourcecheck")) {
            player.sendMessage(Component.text("Getting inventory from pool at region: " + poolType + ", override: " + isOverride, NamedTextColor.GRAY));
        }

        player.openInventory(inventory);
        OpenedChestManager.INSTANCE.addOpen(location);
    }
}
