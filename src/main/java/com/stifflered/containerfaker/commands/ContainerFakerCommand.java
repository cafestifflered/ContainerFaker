package com.stifflered.containerfaker.commands;

import com.stifflered.containerfaker.ContainerFaker;
import com.stifflered.containerfaker.pool.PoolContainerOverrideHandler;
import com.stifflered.containerfaker.pool.PoolType;
import com.stifflered.containerfaker.pool.container.inventory.InventoryCacheStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
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
        }

        return true;
    }

}
