package com.stifflered.containerfaker.pool.fill.impl;

import com.stifflered.containerfaker.pool.PoolType;
import com.stifflered.containerfaker.pool.fill.FillStrategy;
import com.stifflered.containerfaker.util.ItemUtil;
import com.stifflered.containerfaker.util.Randoms;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Fills an entire inventory with random items.
 * <p>
 * Can pick a slot that has been chosen before.
 */
public class RandomFillStrategy implements FillStrategy {

    @Override
    public Map<Integer, ItemStack> fill(PoolType type, Inventory source) {
        Map<Integer, ItemStack> chosenItems = new HashMap<>();

        for (int i = 0; i < source.getSize(); i++) {
            int randomSlot = Randoms.randomNumber(0, source.getSize() - 1);

            ItemStack itemStack = source.getItem(randomSlot);
            chosenItems.put(randomSlot, itemStack);
        }

        return chosenItems;
    }
}
