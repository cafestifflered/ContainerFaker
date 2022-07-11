package com.stifflered.containerfaker.listeners;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerItemFrameListener implements Listener {

    @EventHandler
    public void itemFrameEvent(PlayerItemFrameChangeEvent event) {
        if (!event.getPlayer().hasPermission("itemframe.interact")) {
            event.setCancelled(true);
        }
    }
}
