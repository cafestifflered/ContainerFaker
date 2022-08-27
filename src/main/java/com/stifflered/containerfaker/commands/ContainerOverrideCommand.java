package com.stifflered.containerfaker.commands;

import com.stifflered.containerfaker.Main;
import com.stifflered.containerfaker.pool.OpenedChestManager;
import com.stifflered.containerfaker.pool.PoolContainerOverrideHandler;
import com.stifflered.containerfaker.pool.PoolType;
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
import java.util.Arrays;
import java.util.List;

public class ContainerOverrideCommand extends Command {

    private static final List<String> POOLS = new ArrayList<>();

    static {
        for (PoolType tab : PoolType.values()) {
            POOLS.add(tab.toString());
        }
        POOLS.add("remove");
        POOLS.add("check");
    }

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
                    OpenedChestManager.INSTANCE.removeChest(block.getLocation());
                } else {
                    sender.sendMessage(Component.text("Couldn't find a pool override at that location!", NamedTextColor.RED));
                }
            }
            case "check" -> {
                PoolType type = PoolContainerOverrideHandler.getOverriddenPoolType(block);
                sender.sendMessage(Component.text("Pool Override: " + type, NamedTextColor.GRAY));
            }
            default -> {
                PoolType poolType;
                try {
                    poolType = PoolType.valueOf(arg1.toUpperCase());
                } catch (Exception e) {
                    sender.sendMessage(Component.text("Invalid pool provided! Please pick (%s)".formatted(Arrays.toString(PoolType.values())), NamedTextColor.RED));
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
                        }.runTaskLater(Main.INSTANCE, 20);
                    }
                }.runTask(Main.INSTANCE);

                PoolContainerOverrideHandler.setPoolOverride(block, poolType);
                sender.sendMessage(Component.text("Added pool override!", NamedTextColor.GREEN));
            }
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return POOLS;
    }
}
