package com.stifflered.containerfaker.listeners;

import com.destroystokyo.paper.MaterialSetTag;
import com.stifflered.containerfaker.Main;
import com.stifflered.containerfaker.pool.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Level;

public class PlayerInteractEventListener implements Listener {

    private final MaterialSetTag OPENABLES = new MaterialSetTag(NamespacedKey.fromString("openables", Main.INSTANCE))
            .add(Material.CHEST, Material.TRAPPED_CHEST, Material.BARREL, Material.HOPPER, Material.OBSERVER, Material.DROPPER, Material.DISPENSER, Material.BLAST_FURNACE, Material.FURNACE, Material.FLETCHING_TABLE, Material.LOOM, Material.SMITHING_TABLE, Material.SMOKER, Material.DEAD_BRAIN_CORAL, Material.DEAD_TUBE_CORAL, Material.DEAD_HORN_CORAL)
            .add(MaterialSetTag.SHULKER_BOXES.getValues())
            .lock();

    private final MaterialSetTag DISALLOW_BLOCKS = new MaterialSetTag(NamespacedKey.fromString("disallow_blocks", Main.INSTANCE))
            .add(Material.COMPOSTER, Material.BREWING_STAND)
            .add(MaterialSetTag.BEDS.getValues())
            .lock();

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getAction().isLeftClick()) {
            return;
        }

        Block block = event.getClickedBlock();
        if (OPENABLES.isTagged(block.getType())) {
            Location location = block.getLocation();
            if (PoolStore.INSTANCE.isPoolChest(location)) {
                return;
            }

            event.setUseInteractedBlock(Event.Result.DENY);
            PoolType type = PoolContainerOverrideHandler.getPoolType(block);
            if (type == null) {
                Main.INSTANCE.getLogger().log(Level.WARNING, "Missing pool override for block type: " + block.getType());
                return;
            }

            OpenedChestManager.INSTANCE.openChest(event.getPlayer(), location, type);
            event.getPlayer().playSound(block.getLocation(), Sound.BLOCK_CHEST_OPEN, 2, 1);
        } else if (DISALLOW_BLOCKS.isTagged(block.getType())) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }
    }

}
