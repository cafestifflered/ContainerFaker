package com.stifflered.containerfaker.commands;

import com.stifflered.containerfaker.ContainerFaker;
import com.stifflered.containerfaker.pool.PoolContainerOverrideHandler;
import com.stifflered.containerfaker.pool.PoolType;
import com.stifflered.containerfaker.pool.container.inventory.cache.InventoryCacheStorage;
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

public class ContainerOverrideCommand extends Command {

    public ContainerOverrideCommand() {
        super("containeroverride");
        this.setPermission("containerfaker.command.containeroverride");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        Player player = (Player) sender;
        Block block = player.getTargetBlock(5);
        if (block == null) {
            sender.sendMessage(Component.text("You must be looking at a block!", NamedTextColor.RED));
            return true;
        }

        String arg1 = args[0];
        switch (arg1) {
            case "remove" -> {
                if (PoolContainerOverrideHandler.removePoolOverride(block)) {
                    sender.sendMessage(Component.text("Removed pool override!", NamedTextColor.GREEN));
                    InventoryCacheStorage.INSTANCE.remove(block.getLocation());
                } else {
                    sender.sendMessage(Component.text("Couldn't find a pool override at that location!", NamedTextColor.RED));
                }
            }
            default -> {
                PoolType poolType = PoolType.get(arg1);
                if (poolType == null) {
                    sender.sendMessage(Component.text("Invalid pool provided! Please pick (%s)".formatted(PoolType.keys()), NamedTextColor.RED));
                    return true;
                }

                new BukkitRunnable() {

                    @Override
                    public void run() {
                        player.sendBlockChange(block.getLocation(), Bukkit.createBlockData(Material.GREEN_CONCRETE));
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                player.sendBlockChange(block.getLocation(), block.getBlockData());
                            }
                        }.runTaskLater(ContainerFaker.INSTANCE, 20);
                    }
                }.runTask(ContainerFaker.INSTANCE);

                PoolContainerOverrideHandler.setPoolOverride(block, poolType);
                sender.sendMessage(Component.text("Added pool override!", NamedTextColor.GREEN));
            }
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length > 1) {
            return List.of();
        }

        List<String> pools = new ArrayList<>(PoolType.keys());
        pools.add("remove");
        return pools;
    }
}
