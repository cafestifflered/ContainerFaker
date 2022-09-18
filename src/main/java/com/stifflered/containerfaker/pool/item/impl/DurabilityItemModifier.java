package com.stifflered.containerfaker.pool.item.impl;

import com.stifflered.containerfaker.pool.item.ItemModifier;
import com.stifflered.containerfaker.util.Randoms;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.lib.api.util.ui.SilentNumbers;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.stat.data.DoubleData;
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

        // MMO Items
        try {
            Type itemType = MMOItems.getType(itemStack);
            String identifier = MMOItems.getID(itemStack);
            if (itemType == null || identifier == null) {
                return itemStack;
            }

            MMOItem mmoitem = MMOItems.plugin.getMMOItem(itemType, identifier).clone();

            if (mmoitem.hasData(ItemStats.UNBREAKABLE)) {
                return itemStack;
            }

            int maxDurability = mmoitem.hasData(ItemStats.MAX_DURABILITY) ? SilentNumbers.round(((DoubleData) mmoitem.getData(ItemStats.MAX_DURABILITY)).getValue()) : -1;
            if (maxDurability > 0) {
                mmoitem.setDamage(Randoms.randomNumber((int) (maxDurability * this.minPercent), maxDurability));
            }
            return mmoitem.newBuilder().build();
        } catch (Throwable exception) {
        }

        // Vanilla
        itemStack.editMeta((meta) -> {
            // Vanilla behavior
            short maxDurability = itemStack.getType().getMaxDurability();
            if (maxDurability > 0 && meta instanceof Damageable damageable) {
                damageable.setDamage(Randoms.randomNumber((int) (maxDurability * this.minPercent), maxDurability));
            }
        });


        return itemStack;
    }
}
