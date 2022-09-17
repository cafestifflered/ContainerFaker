package com.stifflered.containerfaker.pool.item.impl;

import com.stifflered.containerfaker.pool.item.ItemModifier;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MMOItemModifier implements ItemModifier {
    @Override
    public @NotNull ItemStack modify(@NotNull ItemStack itemStack) {
        try {
            Type itemType = MMOItems.getType(itemStack);
            String identifier = MMOItems.getID(itemStack);
            if (itemType == null || identifier == null) {
                return itemStack;
            }

            MMOItem mmoitem = MMOItems.plugin.getMMOItem(itemType, identifier);
            if (mmoitem != null) {
                ItemStack built = mmoitem.newBuilder().build();
                if (built == null) {
                    built = new ItemStack(Material.AIR);
                }

                return built;
            }
        } catch (Throwable throwable) {
            return itemStack;
        }

        return itemStack;
    }
}
