package com.stifflered.containerfaker.listeners;

import com.stifflered.containerfaker.pool.OpenedChestManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class PlayerCloseInventoryListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        OpenedChestManager.INSTANCE.closeChest(event);
    }
}
