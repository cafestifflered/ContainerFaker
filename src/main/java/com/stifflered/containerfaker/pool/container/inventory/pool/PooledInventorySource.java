package com.stifflered.containerfaker.pool.container.inventory.pool;

import com.stifflered.containerfaker.ContainerFaker;
import com.stifflered.containerfaker.pool.PoolStore;
import com.stifflered.containerfaker.pool.PoolType;
import com.stifflered.containerfaker.pool.container.inventory.InventorySource;
import com.stifflered.containerfaker.pool.fill.FillStrategy;
import com.stifflered.containerfaker.util.ItemUtil;
import com.stifflered.containerfaker.util.Randoms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public abstract class PooledInventorySource implements InventorySource {

    @Nullable
    public abstract PoolType getType(Player player, Location location);

    @Override
    public @Nullable Inventory get(Player player, Location location) {
        if (PoolStore.INSTANCE.isPoolEmpty()) {
            return null;
        }

        PoolType poolType = this.getType(player, location);
        if (poolType == null) {
            return null;
        }
        InventorySource.debug(player, this, poolType);

        MutableBlockInventoryHolder mutableBlockInventoryHolder = new MutableBlockInventoryHolder(location.getBlock());
        Inventory createdInventory = Bukkit.createInventory(mutableBlockInventoryHolder, 27);
        mutableBlockInventoryHolder.setInventory(createdInventory);

        Inventory inventory = Randoms.randomIndex(PoolStore.INSTANCE.getPool(poolType));
        if (inventory == null) {
            return null;
        }

        Map<Integer, ItemStack> chosenItems = poolType.getStrategy().fill(poolType, inventory);

        for (Map.Entry<Integer, ItemStack> entry : chosenItems.entrySet()) {
            ItemStack itemStack = entry.getValue();
            if (itemStack == null) {
                itemStack = new ItemStack(Material.AIR);
            } else {
                itemStack = itemStack.clone();
            }

            createdInventory.setItem(entry.getKey(), ContainerFaker.INSTANCE.getItemRandomizer().modify(itemStack)); // clone the item
        }

        return createdInventory;
    }



    private static class MutableBlockInventoryHolder implements BlockInventoryHolder {

        private final Block block;
        private Inventory inventory;

        public MutableBlockInventoryHolder(Block block) {
            this.block = block;
        }

        @Override
        public @NotNull Block getBlock() {
            return this.block;
        }

        @Override
        public @NotNull Inventory getInventory() {
            return this.inventory;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }
    }

}
