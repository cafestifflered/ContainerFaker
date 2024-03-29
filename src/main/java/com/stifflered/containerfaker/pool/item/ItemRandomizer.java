package com.stifflered.containerfaker.pool.item;

import com.stifflered.containerfaker.pool.item.impl.DurabilityItemModifier;
import com.stifflered.containerfaker.pool.item.impl.MMOItemModifier;
import com.stifflered.containerfaker.pool.item.impl.RemoveSingleKeyModifier;
import com.stifflered.containerfaker.pool.item.impl.StackableItemModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemRandomizer implements ItemModifier {

    private final ItemModifier[] itemModifiers;

    public ItemRandomizer(ConfigurationSection section) {
        this.itemModifiers = new ItemModifier[]{
                new MMOItemModifier(),
                new DurabilityItemModifier(section),
                new StackableItemModifier(section),
                new RemoveSingleKeyModifier()
        };
    }

    @Override
    public @NotNull ItemStack modify(@NotNull ItemStack itemStack) {
        for (ItemModifier modifier : this.itemModifiers) {
            itemStack = modifier.modify(itemStack);
        }

        return itemStack;
    }

}
