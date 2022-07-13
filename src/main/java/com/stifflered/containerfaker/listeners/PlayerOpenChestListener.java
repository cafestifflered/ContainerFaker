package com.stifflered.containerfaker.listeners;

import com.destroystokyo.paper.MaterialSetTag;
import com.stifflered.containerfaker.Main;
import com.stifflered.containerfaker.pool.OpenedChestManager;
import com.stifflered.containerfaker.pool.PoolStore;
import com.stifflered.containerfaker.pool.PoolType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerOpenChestListener implements Listener {

    private final MaterialSetTag OPENABLES = new MaterialSetTag(NamespacedKey.fromString("openables", Main.INSTANCE))
            .add(Material.CHEST, Material.TRAPPED_CHEST, Material.BARREL, Material.HOPPER, Material.DROPPER, Material.DISPENSER, Material.BLAST_FURNACE, Material.FURNACE, Material.FLETCHING_TABLE, Material.LOOM, Material.SMITHING_TABLE, Material.SMOKER, Material.DEAD_BRAIN_CORAL, Material.DEAD_TUBE_CORAL, Material.DEAD_HORN_CORAL)
            .add(MaterialSetTag.SHULKER_BOXES.getValues())
            .lock();

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getAction().isLeftClick()) {
            return;
        }

        Block block = event.getClickedBlock();
        if (OPENABLES.isTagged(block.getType())) {
            if (!canNormallyOpenContainer(event.getPlayer(), block)) {
                event.setUseInteractedBlock(Event.Result.DENY);
                return;
            }

            Location location = block.getLocation();
            if (PoolStore.INSTANCE.isPoolChest(location)) {
                return;
            }

            event.setUseInteractedBlock(Event.Result.DENY);
            OpenedChestManager.INSTANCE.openChest(event.getPlayer(), location, PoolType.FOOD);
            event.getPlayer().playSound(block.getLocation(), Sound.BLOCK_CHEST_OPEN, 2, 1);
        }
    }

    public boolean canNormallyOpenContainer(Player player, Block block) {
        if (player.isSneaking()) {
            return false;
        }

        BlockData data = block.getBlockData();
        Material material = data.getMaterial();

        if (material == Material.CHEST) {
            if (!block.getLocation().add(0, 1, 0).getBlock().getType().isAir()) {
                return false; // can't open if block is above
            }
        } else if (MaterialSetTag.SHULKER_BOXES.isTagged(material)) {
            if (data instanceof Directional directional && !block.getLocation().add(directional.getFacing().getDirection()).getBlock().getType().isAir()) {
                return false; // can't open if block is obstructing shulker box opening
            }
        }
        return true;
    }
}
