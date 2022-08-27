package com.stifflered.containerfaker.listeners;

import com.destroystokyo.paper.MaterialSetTag;
import com.stifflered.containerfaker.ContainerFaker;
import com.stifflered.containerfaker.pool.PoolContainerOverrideHandler;
import com.stifflered.containerfaker.pool.PoolStore;
import com.stifflered.containerfaker.pool.container.OpenCallback;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractEventListener implements Listener {

    private final MaterialSetTag DISALLOW_BLOCKS = new MaterialSetTag(NamespacedKey.fromString("disallow_blocks", ContainerFaker.INSTANCE))
            .add(Material.COMPOSTER, Material.BREWING_STAND, Material.DRAGON_EGG)
            .add(MaterialSetTag.BEDS.getValues())
            .lock();

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getAction().isLeftClick()) {
            return;
        }

        Block block = event.getClickedBlock();
        OpenCallback type = PoolContainerOverrideHandler.getPoolType(block);
        if (type != null) {
            Location location = block.getLocation();
            if (PoolStore.INSTANCE.isPoolChest(location)) {
                return;
            }

            event.setUseInteractedBlock(Event.Result.DENY);

            type.onOpen(event.getPlayer(), location);
            event.getPlayer().playSound(block.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 0.5f, 1f);
        } else if (DISALLOW_BLOCKS.isTagged(block.getType())) {
            event.setUseInteractedBlock(Event.Result.DENY);
        }
    }

}
