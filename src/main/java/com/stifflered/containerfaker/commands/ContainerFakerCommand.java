package com.stifflered.containerfaker.commands;

import com.stifflered.containerfaker.pool.container.inventory.InventoryCacheStorage;
import com.stifflered.containerfaker.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContainerFakerCommand extends Command {

    public ContainerFakerCommand() {
        super("containerfaker");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        String arg1 = args[0];
        switch (arg1) {
            case "clearcache" -> {
                if (sender.hasPermission("containerfaker.command.clearcache")) {
                    InventoryCacheStorage.INSTANCE.clear();
                    sender.sendMessage(Component.text("Cleared cache for loaded containers. All containers will now generate new loot.", NamedTextColor.GREEN));
                }
            }
            case "single" -> {
                if (sender.hasPermission("containerfaker.command.single")) {
                    Player player = (Player) sender;
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    itemStack.editMeta((meta) -> {
                        ItemUtil.setSingle(meta);
                        List<Component> lore;
                        if (meta.hasLore()) {
                            lore = meta.lore();
                        } else {
                            lore = new ArrayList<>();
                        }

                        lore.add(0, ItemUtil.getSingleComponent());
                        meta.lore(lore);
                    });

                    sender.sendMessage(Component.text("Item set as a single item. This item will only appear once when a chest is opened."));
                    player.getInventory().setItemInMainHand(itemStack);
                }
            }
        }

        return true;
    }

}
