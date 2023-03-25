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
 * Picks a certain number of slots, then will iterate for that number of slots
 * until an item is picked.
 *
 * Picks a slot that hasn't been chosen before.
 */
public class RandomSlottedFillStrategy implements FillStrategy {

    private static final int UNSAFE_ITERATION_AMOUNT = 999999;

    private boolean isSingleDuplicate(Set<ItemStack> chosen, ItemStack toChoose) {
        if (toChoose == null) {
            return false;
        }

        if (chosen.contains(toChoose)) {
            return ItemUtil.isSingle(toChoose.getItemMeta());
        }

        return false;
    }

    @Override
    public Map<Integer, ItemStack> fill(PoolType type, Inventory source) {
        Set<ItemStack> alreadyChosen = new HashSet<>();
        Map<Integer, ItemStack> chosenItems = new HashMap<>();

        for (int i = 0; i < type.getRandomCount(); i++) {
            int randomSlot;

            int iterations = 0;
            do {
                iterations++;
                randomSlot = Randoms.randomNumber(0, source.getSize() - 1);
                if (iterations > UNSAFE_ITERATION_AMOUNT) {
                    Bukkit.getLogger().log(Level.WARNING, "Dangerous iteration amount for " + this + " for pooled inventory " + type.getName() + ". Check to make sure there are enough valid items!");
                    break;
                }
            } while (chosenItems.containsKey(randomSlot) || this.isSingleDuplicate(alreadyChosen, source.getItem(randomSlot)));

            ItemStack itemStack = source.getItem(randomSlot);
            chosenItems.put(randomSlot, itemStack);
            alreadyChosen.add(itemStack);
        }

        return chosenItems;
    }
}
