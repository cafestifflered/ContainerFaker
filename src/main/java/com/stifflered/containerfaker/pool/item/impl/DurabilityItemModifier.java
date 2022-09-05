package com.stifflered.containerfaker.pool.item.impl;

import com.stifflered.containerfaker.pool.item.ItemModifier;
import com.stifflered.containerfaker.util.Randoms;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

public class DurabilityItemModifier implements ItemModifier {

    private final boolean isActive;
    private final double minPercent;

    public DurabilityItemModifier(ConfigurationSection section) {
        this.isActive = section.getBoolean("randomize-durability");
        this.minPercent = section.getInt("random-min-durability-percent") / 100F;
    }

    @Override
    public @NotNull ItemStack modify(@NotNull ItemStack itemStack) {
        if (!this.isActive) {
            return itemStack;
        }

        itemStack.editMeta((meta) -> {
            // Vanilla behavior
            boolean isCustomItemstack;
            try {
                CustomStack customStack = CustomStack.byItemStack(itemStack);
                isCustomItemstack = customStack != null;
            } catch (Throwable exception) {
                isCustomItemstack = false;
            }
            if (!isCustomItemstack) {
                short maxDurability = itemStack.getType().getMaxDurability();
                if (maxDurability > 0 && meta instanceof Damageable damageable) {
                    damageable.setDamage(Randoms.randomNumber((int) (maxDurability * this.minPercent), maxDurability));
                }
            }
        });

        // Use custom itemstack durability
        try {
            CustomStack customStack = CustomStack.byItemStack(itemStack);
            int maxDurability = customStack.getMaxDurability();
            if (maxDurability > 1) {
                customStack.setDurability(Randoms.randomNumber((int) (maxDurability * this.minPercent), maxDurability));
            }

            return customStack.getItemStack();
        } catch (Throwable exception) {
        }

        return itemStack;
    }
}
