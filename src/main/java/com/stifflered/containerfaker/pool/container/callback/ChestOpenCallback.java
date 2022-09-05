package com.stifflered.containerfaker.pool.container.callback;

import com.stifflered.containerfaker.pool.OpenedChestManager;
import com.stifflered.containerfaker.pool.container.inventory.InventorySource;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ChestOpenCallback implements OpenCallback {

    private final InventorySource inventorySource;

    public ChestOpenCallback(InventorySource inventorySource) {
        this.inventorySource = inventorySource;
    }

    @Override
    public void onOpen(Player player, Location location) {
        Location blockLocation = location.toBlockLocation();
        if (OpenedChestManager.INSTANCE.isOpen(blockLocation)) {
            return;
        }

        Inventory inventory = this.inventorySource.get(player, location);

        player.openInventory(inventory);
        OpenedChestManager.INSTANCE.addOpen(location);
    }
}
