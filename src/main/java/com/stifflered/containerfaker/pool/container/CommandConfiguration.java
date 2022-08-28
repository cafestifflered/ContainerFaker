package com.stifflered.containerfaker.pool.container;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandConfiguration implements OpenCallback {

    private final Map<SenderType, List<String>> commands = new HashMap<>();

    public CommandConfiguration(ConfigurationSection configurationSection) {
        ConfigurationSection commandSection = configurationSection.getConfigurationSection("commands");
        if (commandSection == null) {
            return;
        }

        for (SenderType senderType : SenderType.values()) {
            String key = "as-" + senderType.name().toLowerCase();
            if (!commandSection.contains(key)) {
                continue;
            }

            this.commands.put(senderType, commandSection.getStringList(key));
        }
    }

    @Override
    public void onOpen(Player player, Location location) {
        for (Map.Entry<SenderType, List<String>> entry : this.commands.entrySet()) {
            entry.getKey().execute(player, entry.getValue());
        }
    }

    enum SenderType {
        PLAYER {
            @Override
            public void execute(Player sender, List<String> commands) {
                for (String command : commands) {
                    sender.performCommand(SenderType.format(sender, command));
                }
            }
        },
        CONSOLE {
            @Override
            public void execute(Player sender, List<String> commands) {
                for (String command : commands) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), SenderType.format(sender, command));
                }
            }
        };

        public abstract void execute(Player sender, List<String> commands);

        private static String format(Player sender, String command) {
            return command.replaceAll("\\{player}", sender.getName());
        }
    }

}
