package com.stifflered.containerfaker;

import com.stifflered.containerfaker.commands.ContainerOverrideCommand;
import com.stifflered.containerfaker.listeners.*;
import com.stifflered.containerfaker.pool.PoolType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ContainerFaker extends JavaPlugin implements Listener {

    public static JavaPlugin INSTANCE;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        INSTANCE = this;
        this.register(new PlayerInteractEventListener(), new PlayerCloseInventoryListener(), new PlayerBreakBlockListener(), new PlayerPlaceBlockListener());
        Bukkit.getCommandMap().register("containerfaker", new ContainerOverrideCommand());

        World world = Bukkit.getWorld(NamespacedKey.fromString(this.getConfig().getString("world")));

        PoolType.init(world);
        for (PoolType type : PoolType.values()) {
            type.refreshPool(world);
        }
    }

    @Override
    public void onDisable() {
    }

    private void register(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

}
