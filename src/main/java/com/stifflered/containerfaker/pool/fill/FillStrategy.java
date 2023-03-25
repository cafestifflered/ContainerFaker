package com.stifflered.containerfaker.pool.fill;

import com.stifflered.containerfaker.pool.PoolType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface FillStrategy {

    Map<Integer, ItemStack> fill(PoolType type, Inventory source);
}
