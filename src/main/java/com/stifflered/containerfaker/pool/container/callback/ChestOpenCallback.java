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
        Inventory inventory = this.inventorySource.get(player, location);
        if (inventory == null) {
            return;
        }

        player.openInventory(inventory);
        OpenedChestManager.INSTANCE.addOpen(location);
    }
}
