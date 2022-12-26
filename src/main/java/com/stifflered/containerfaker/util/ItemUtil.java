package com.stifflered.containerfaker.util;

import com.stifflered.containerfaker.ContainerFaker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtil {

    private static final NamespacedKey singleKey = new NamespacedKey(ContainerFaker.INSTANCE, "single");

    public static boolean isSingle(PersistentDataHolder container) {
        return container.getPersistentDataContainer().has(singleKey);
    }

    public static void setSingle(PersistentDataHolder container) {
        container.getPersistentDataContainer().set(singleKey, PersistentDataType.BYTE, (byte) 1);
    }

    public static void removeSingle(PersistentDataHolder container) {
        container.getPersistentDataContainer().remove(singleKey);
    }

    public static Component getSingleComponent() {
        return Component.text("Configured as Single Item", NamedTextColor.GREEN);
    }

}
