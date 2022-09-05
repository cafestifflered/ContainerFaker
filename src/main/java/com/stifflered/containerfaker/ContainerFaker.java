package com.stifflered.containerfaker;

import com.stifflered.containerfaker.commands.ContainerFakerCommand;
import com.stifflered.containerfaker.commands.ContainerOverrideCommand;
import com.stifflered.containerfaker.listeners.*;
import com.stifflered.containerfaker.pool.PoolType;
import com.stifflered.containerfaker.pool.RegionAccessStorage;
import com.stifflered.containerfaker.pool.item.ItemModifier;
import com.stifflered.containerfaker.pool.item.ItemRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ContainerFaker extends JavaPlugin implements Listener {

    public static ContainerFaker INSTANCE;

    private RegionAccessStorage storage;
    private ItemModifier itemModifier;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();

        INSTANCE = this;
        this.register(new PlayerInteractEventListener(), new PlayerCloseInventoryListener(), new PlayerBreakBlockListener(), new PlayerPlaceBlockListener());
        Bukkit.getCommandMap().register("containerfaker", new ContainerOverrideCommand());
        Bukkit.getCommandMap().register("containerfaker", new ContainerFakerCommand());

        World world = Bukkit.getWorld(this.getConfig().getString("world"));

        PoolType.init(world);
        for (PoolType type : PoolType.values()) {
            type.refreshPool(world);
        }
        this.storage = new RegionAccessStorage(this.getConfig());
        this.itemModifier = new ItemRandomizer(this.getConfig());
    }

    @Override
    public void onDisable() {
    }

    private void register(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    public RegionAccessStorage getRegionAccessStorage() {
        return this.storage;
    }

    public ItemModifier getItemRandomizer() {
        return this.itemModifier;
    }
}
