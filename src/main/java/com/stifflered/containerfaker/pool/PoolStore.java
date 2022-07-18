package com.stifflered.containerfaker.pool;

import com.stifflered.containerfaker.Main;
import com.stifflered.containerfaker.util.BlockLocationIterator;
import com.stifflered.containerfaker.util.Randoms;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class PoolStore {

    public static final PoolStore INSTANCE = new PoolStore();

    private final Map<PoolType, List<Inventory>> poolStorage = new EnumMap<>(PoolType.class);
    private final Map<Location, PoolType> poolChests = new HashMap<>();

    public PoolStore() {
        for (PoolType type : PoolType.values()) {
            this.poolStorage.put(type, new ArrayList<>());
        }
    }

    public CompletableFuture<Void> loadPool(World world, PoolType type) {
        Main.INSTANCE.getLogger().log(Level.INFO, "Loading pool: " + type);
        Location min = type.getMin().toLocation(world);
        Location max = type.getMax().toLocation(world);
        return loadChunksBetween(min, max).thenAccept((v) -> {
            List<Inventory> inventories = new ArrayList<>();
            this.poolStorage.put(type, inventories);
            this.poolChests.values().removeIf((typeToRemove) -> typeToRemove == type);

            for (Location location : new BlockLocationIterator(min, max)) {
                Block block = location.getBlock();
                if (block.getType() == Material.CHEST && block.getState() instanceof Chest chest) {
                    inventories.add(chest.getBlockInventory());
                    poolChests.put(chest.getLocation(), type);
                }
            }
            Main.INSTANCE.getLogger().log(Level.INFO, "Loaded pool: " + type + " WITH " + inventories.size());
        });
    }

    public Inventory randomFromPool(Location location, PoolType type) {
        if (this.poolStorage.isEmpty()) {
            return null;
        }

        Map<Integer, ItemStack> chosenItems = new HashMap<>();
        Inventory createdInventory = Bukkit.createInventory(new BlockInventoryHolder() {

            @Override
            public @NotNull Inventory getInventory() {
                return OpenedChestManager.INSTANCE.getInventory(location);
            }

            @Override
            public @NotNull Block getBlock() {
                return location.getBlock();
            }
        }, 27);

        Inventory inventory = Randoms.randomIndex(this.poolStorage.get(type));
        if (inventory == null) {
            return null;
        }

        for (int i = 0; i < 4; i++) {
            int randomSlot;
            do {
                randomSlot = Randoms.randomNumber(0, inventory.getSize() - 1);
            } while (chosenItems.containsKey(randomSlot));

            chosenItems.put(randomSlot, inventory.getItem(randomSlot));
        }

        for (Map.Entry<Integer, ItemStack> entry : chosenItems.entrySet()) {
            ItemStack itemStack = entry.getValue();
            if (itemStack == null) {
                itemStack = new ItemStack(Material.AIR);
            } else {
                itemStack = itemStack.clone();
            }

            createdInventory.setItem(entry.getKey(), this.applyChanges(itemStack)); // clone the item
        }

        return createdInventory;
    }

    public void removePoolChest(Location location) {
        PoolType type = this.poolChests.remove(location);
        if (type != null) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    type.refreshPool(location.getWorld());
                }
            }.runTaskLater(Main.INSTANCE, 1);
        }
    }

    public void addPossiblePoolChest(Location location) {
        for (PoolType type : PoolType.values()) {
            if (isWithinArea(type.getMin(), type.getMax(), location)) {
                type.refreshPool(location.getWorld());
                break;
            }
        }

    }

    public boolean isPoolChest(Location location) {
        return this.poolChests.containsKey(location);
    }

    public PoolType getPoolFromLocation(Location location) {
        return this.poolChests.get(location);
    }

    private ItemStack applyChanges(ItemStack itemStack) {
        itemStack.editMeta((meta) -> {
            // random stack size
            if (itemStack.getMaxStackSize() != 1 && !itemStack.getType().isAir()) {
                itemStack.setAmount(Randoms.randomNumber(0, 3));
            }
            // random durability
            short maxDurability = itemStack.getType().getMaxDurability();
            if (maxDurability > 0 && meta instanceof Damageable damageable) {
                damageable.setDamage(Randoms.randomNumber(10, maxDurability));
            }
        });
        return itemStack;
    }


    private boolean isWithinArea(Vector min, Vector max, Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= min.getX() && x <= max.getX() && y >= min.getY() && y <= max.getY() && z >= min.getZ() && z <= max.getZ();
    }

    private static CompletableFuture<Void> loadChunksBetween(Location location1, Location location2) {
        List<CompletableFuture<Chunk>> futures = new ArrayList<>();
        int minX = Math.min(location1.getBlockX(), location2.getBlockX());
        int maxX = Math.max(location1.getBlockX(), location2.getBlockX());
        int minZ = Math.min(location1.getBlockZ(), location2.getBlockZ());
        int maxZ = Math.max(location1.getBlockZ(), location2.getBlockZ());

        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                futures.add(location1.getWorld().getChunkAtAsync(x >> 4, z >> 4));
            }
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

}
