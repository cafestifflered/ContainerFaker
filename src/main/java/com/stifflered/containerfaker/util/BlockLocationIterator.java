package com.stifflered.containerfaker.util;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BlockLocationIterator implements Iterable<Location>, Iterator<Location> {

    private final Location baseLocation;
    private final int maxX, maxY, maxZ;
    private final int volume;
    private int volumeIterated = 0;
    private int x = -1, y = 0, z = 0;

    public BlockLocationIterator(Location loc1, Location loc2) {
        int minX, minY, minZ;

        minX = loc1.getBlockX();
        this.maxX = loc2.getBlockX() - minX;
        if (maxX < 0) {
            this.x = 1;
        }

        minY = loc1.getBlockY();
        this.maxY = loc2.getBlockY() - minY;

        minZ = loc1.getBlockZ();
        this.maxZ = loc2.getBlockZ() - minZ;

        this.volume = (Math.abs(maxX) + 1) * (Math.abs(maxY) + 1) * (Math.abs(maxZ) + 1);
        this.baseLocation = new Location(loc1.getWorld(), minX, minY, minZ).toCenterLocation();
    }

    @NotNull
    @Override
    public Iterator<Location> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return volumeIterated < volume;
    }

    @Override
    public Location next() {
        nextCoord();
        return baseLocation.clone().add(x, y, z);
    }

    private void nextCoord() {
        volumeIterated++;

        if (maxX < 0) {
            x--;
            if (x < maxX) {
                x = 0;
                if (maxZ < 0) {
                    z--;
                } else {
                    z++;
                }
            }
        } else {
            x++;
            if (x > maxX) {
                x = 0;
                if (maxZ < 0) {
                    z--;
                } else {
                    z++;
                }
            }
        }
        if (maxZ < 0) {
            if (z < maxZ) {
                z = 0;
                if (maxY < 0) {
                    y--;
                } else {
                    y++;
                }
            }
        } else if (z > maxZ) {
            z = 0;
            if (maxY < 0) {
                y--;
            } else {
                y++;
            }
        }
    }
}

