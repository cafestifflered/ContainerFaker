package com.stifflered.containerfaker.pool;

import com.destroystokyo.paper.MaterialSetTag;
import com.stifflered.containerfaker.util.Randoms;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class ItemRandomizer {

    private static final MaterialSetTag STACKABLES = new MaterialSetTag(NamespacedKey.minecraft("stackables"))
            .add(Tag.ITEMS_ARROWS.getValues())
            .add(Material.BONE, Material.STICK, Material.FLINT, Material.FEATHER);

    public static ItemStack applyChanges(ItemStack itemStack) {
        itemStack.editMeta((meta) -> {
            // random stack size
            if (STACKABLES.isTagged(itemStack) && itemStack.getMaxStackSize() != 1) {
                itemStack.setAmount(Randoms.randomNumber(0, 3));
            }

            // random durability
            CustomStack customStack = CustomStack.byItemStack(itemStack);
            if (customStack == null) {
                // Vanilla behavior
                short maxDurability = itemStack.getType().getMaxDurability();
                if (maxDurability > 0 && meta instanceof Damageable damageable) {
                    damageable.setDamage(Randoms.randomNumber(10, maxDurability));
                }
            } else {
                int maxDurability = customStack.getMaxDurability();
                if (maxDurability > 0) {
                    customStack.setDurability(Randoms.randomNumber(10, maxDurability));
                }
            }
        });
        return itemStack;
    }
}
