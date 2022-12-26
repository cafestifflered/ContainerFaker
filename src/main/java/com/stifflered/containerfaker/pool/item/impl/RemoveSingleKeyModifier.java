package com.stifflered.containerfaker.pool.item.impl;

import com.stifflered.containerfaker.pool.item.ItemModifier;
import com.stifflered.containerfaker.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RemoveSingleKeyModifier implements ItemModifier {

    @Override
    public @NotNull ItemStack modify(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return itemStack;
        }

        if (ItemUtil.isSingle(meta)) {
            itemStack.editMeta(ItemUtil::removeSingle);
            List<Component> lore = meta.lore();
            // Is the lore still there?
            if (!lore.isEmpty() && serialize(lore.get(0)).contains(serialize(ItemUtil.getSingleComponent()))) {
                List<Component> subList = lore.subList(1, lore.size());
                if (subList.isEmpty()) {
                    itemStack.lore(null); // Ensure list isn't empty so that no empty list is written
                } else {
                    itemStack.lore(subList);
                }
            }

        }

        return itemStack;
    }

    private static String serialize(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}
