package com.stifflered.containerfaker.pool.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemModifier {

    @NotNull
    ItemStack modify(@NotNull ItemStack itemStack);
}
