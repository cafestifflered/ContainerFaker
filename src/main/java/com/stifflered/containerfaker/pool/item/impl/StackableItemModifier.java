package com.stifflered.containerfaker.pool.item.impl;

import com.stifflered.containerfaker.pool.item.ItemModifier;
import com.stifflered.containerfaker.util.Randoms;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class StackableItemModifier implements ItemModifier {

    private final Set<Material> stackableItems = new HashSet<>();
    private final int min;
    private final int max;

    public StackableItemModifier(ConfigurationSection section) {
        this.min = section.getInt("stack-minimum");
        this.max = section.getInt("stack-maximum");

        for (String item : section.getStringList("stackable-items")) {
            this.stackableItems.add(Material.getMaterial(item));
        }
    }

    @Override
    public @NotNull ItemStack modify(@NotNull ItemStack itemStack) {
        // random stack size
        if (this.stackableItems.contains(itemStack.getType())) {
            if (itemStack.getMaxStackSize() != 1) {
                itemStack.setAmount(Randoms.randomNumber(this.min, this.max));
            }
        }

        return itemStack;
    }
}
